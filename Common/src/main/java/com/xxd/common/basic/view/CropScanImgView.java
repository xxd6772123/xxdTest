package com.xxd.common.basic.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ImageUtils;
import com.xxd.common.basic.R;
import com.xxd.common.basic.utils.DensityUtil;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:剪切并带扫描剪切区域的控件
 */
public class CropScanImgView extends View {
    private static int STATUS_IDLE = 1;// 空闲状态
    private static int STATUS_MOVE = 2;// 移动状态
    private static int STATUS_SCALE = 3;// 缩放状态

    private static final long DEF_SCAN_DURATION = 1500;

    private static final int CIRCLE_WIDTH = 46;
    private static final float HALF_CIRCLE_WIDTH = CIRCLE_WIDTH / 2f;
    private float oldx, oldy;
    private int status = STATUS_IDLE;
    private int selectedControllerCicle;
    private RectF backUpRect = new RectF();// 上
    private RectF backLeftRect = new RectF();// 左
    private RectF backRightRect = new RectF();// 右
    private RectF backDownRect = new RectF();// 下

    private RectF mCropRect = new RectF();// 剪切矩形

    private Paint mBackgroundPaint;// 背景Paint
    private Paint mLinePaint;
    private Paint mScanPaint;
    private Bitmap mCornerBmp;
    private Rect circleRect = new Rect();
    private RectF leftTopCircleRect;
    private RectF rightTopCircleRect;
    private RectF leftBottomRect;
    private RectF rightBottomRect;

    private RectF imageRect = new RectF();// 存贮图片位置信息
    private RectF tempRect = new RectF();// 临时存贮矩形数据

    private float ratio = -1;// 剪裁缩放比率

    private boolean isScaning = false;
    private RectF mScanRect;
    private Animator mScanAnimator;

    public CropScanImgView(Context context) {
        this(context, null);
    }

    public CropScanImgView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CropScanImgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        float lineWidth = DensityUtil.dip2px(1);
        int lineColor = Color.WHITE;
        int scanColor = Color.parseColor("#700000FF");
        int shadowColor = Color.parseColor("#B0000000");
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CropScanImgView);
            Drawable drawable = array.getDrawable(R.styleable.CropScanImgView_cornerBmp);
            if (drawable != null) {
                mCornerBmp = ImageUtils.drawable2Bitmap(drawable);
            }
            lineWidth = array.getDimensionPixelSize(R.styleable.CropScanImgView_lineWidth, (int) lineWidth);
            lineColor = array.getColor(R.styleable.CropScanImgView_lineColor, Color.WHITE);
            scanColor = array.getColor(R.styleable.CropScanImgView_scanColor, scanColor);
            shadowColor = array.getColor(R.styleable.CropScanImgView_shadowColor, shadowColor);
            array.recycle();
        }

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(shadowColor);
        mBackgroundPaint.setAntiAlias(true);

        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(lineWidth);
        mLinePaint.setColor(lineColor);
        mLinePaint.setAntiAlias(true);

        mScanPaint = new Paint();
        mScanPaint.setAntiAlias(true);
        mScanPaint.setColor(scanColor);

        if (mCornerBmp == null) {
            mCornerBmp = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ic_common_corner_spike);
        }
        circleRect.set(0, 0, mCornerBmp.getWidth(), mCornerBmp.getHeight());
        leftTopCircleRect = new RectF(0, 0, CIRCLE_WIDTH, CIRCLE_WIDTH);
        rightTopCircleRect = new RectF(leftTopCircleRect);
        leftBottomRect = new RectF(leftTopCircleRect);
        rightBottomRect = new RectF(leftTopCircleRect);
    }

    /**
     * 重置剪裁面
     *
     * @param rect
     */
    public void setCropRect(RectF rect, float scale) {
        if (rect == null)
            return;

        imageRect.set(rect);
        mCropRect.set(rect);
        scaleRect(mCropRect, scale);
        invalidate();
    }

    public void setRatioCropRect(RectF rect, float r) {
        this.ratio = r;
        if (r < 0) {
            setCropRect(rect, 0.5f);
            return;
        }

        imageRect.set(rect);
        mCropRect.set(rect);
        // setCropRect(rect);
        // 调整Rect

        float h, w;
        if (mCropRect.width() >= mCropRect.height()) {// w>=h
            h = mCropRect.height() / 2;
            w = this.ratio * h;
            if (w > rect.width()) {
                w = rect.width();
                h = w / this.ratio;
            }
        } else {// w<h
            w = rect.width() / 2;
            h = w / this.ratio;
            if (h > rect.height()) {
                h = rect.height();
                w = h * this.ratio;
            }
        }// end if
        float scaleX = w / mCropRect.width();
        float scaleY = h / mCropRect.height();
        scaleRect(mCropRect, scaleX, scaleY);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) {
            return;
        }

        // 绘制黑色背景
        backUpRect.set(0, 0, w, mCropRect.top);
        backLeftRect.set(0, mCropRect.top, mCropRect.left, mCropRect.bottom);
        backRightRect.set(mCropRect.right, mCropRect.top, w, mCropRect.bottom);
        backDownRect.set(0, mCropRect.bottom, w, h);

        canvas.drawRect(backUpRect, mBackgroundPaint);
        canvas.drawRect(backLeftRect, mBackgroundPaint);
        canvas.drawRect(backRightRect, mBackgroundPaint);
        canvas.drawRect(backDownRect, mBackgroundPaint);

        //绘制裁剪矩形及线
        drawCropRectAndLine(canvas);

        //绘制扫描区域
        drawScanRect(canvas);

        // 绘制四个控制点
        int radius = (int) HALF_CIRCLE_WIDTH;
        leftTopCircleRect.set(mCropRect.left - radius, mCropRect.top - radius,
                mCropRect.left + radius, mCropRect.top + radius);
        rightTopCircleRect.set(mCropRect.right - radius, mCropRect.top - radius,
                mCropRect.right + radius, mCropRect.top + radius);
        leftBottomRect.set(mCropRect.left - radius, mCropRect.bottom - radius,
                mCropRect.left + radius, mCropRect.bottom + radius);
        rightBottomRect.set(mCropRect.right - radius, mCropRect.bottom - radius,
                mCropRect.right + radius, mCropRect.bottom + radius);

        canvas.drawBitmap(mCornerBmp, circleRect, leftTopCircleRect, null);
        canvas.drawBitmap(mCornerBmp, circleRect, rightTopCircleRect, null);
        canvas.drawBitmap(mCornerBmp, circleRect, leftBottomRect, null);
        canvas.drawBitmap(mCornerBmp, circleRect, rightBottomRect, null);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopScan();
    }

    private void drawCropRectAndLine(Canvas canvas) {
        //rect
        canvas.drawRect(mCropRect, mLinePaint);

        float startX = 0f;
        float startY = 0f;
        float stopX = 0f;
        float stopY = 0f;
        //vertical line 1
        startX = mCropRect.left + mCropRect.width() / 3;
        startY = mCropRect.top;
        stopX = startX;
        stopY = mCropRect.bottom;
        canvas.drawLine(startX, startY, stopX, stopY, mLinePaint);
        //vertical line 2
        startX = mCropRect.left + mCropRect.width() * 2 / 3;
        startY = mCropRect.top;
        stopX = startX;
        stopY = mCropRect.bottom;
        canvas.drawLine(startX, startY, stopX, stopY, mLinePaint);

        //horizontal line 1
        startX = mCropRect.left;
        startY = mCropRect.top + mCropRect.height() / 3;
        stopX = mCropRect.right;
        stopY = startY;
        canvas.drawLine(startX, startY, stopX, stopY, mLinePaint);
        //horizontal line 2
        startX = mCropRect.left;
        startY = mCropRect.top + mCropRect.height() * 2 / 3;
        stopX = mCropRect.right;
        stopY = startY;
        canvas.drawLine(startX, startY, stopX, stopY, mLinePaint);

    }

    private void drawScanRect(Canvas canvas) {
        if (!isScaning) {
            return;
        }
        if (mScanRect != null && mScanRect.width() > 0 && mScanRect.height() > 0) {
            canvas.drawRect(mScanRect, mScanPaint);
        }
    }

    /**
     * 触摸事件处理
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isScaning) {
            return false;
        }
        boolean ret = super.onTouchEvent(event);// 是否向下传递事件标志 true为消耗
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                int selectCircle = isSeletedControllerCircle(x, y);
                if (selectCircle > 0) {// 选择控制点
                    ret = true;
                    selectedControllerCicle = selectCircle;// 记录选中控制点编号
                    status = STATUS_SCALE;// 进入缩放状态
                } else if (mCropRect.contains(x, y)) {// 选择缩放框内部
                    ret = true;
                    status = STATUS_MOVE;// 进入移动状态
                } else {// 没有选择

                }// end if
                break;
            case MotionEvent.ACTION_MOVE:
                if (status == STATUS_SCALE) {// 缩放控制
                    // System.out.println("缩放控制");
                    scaleCropController(x, y);
                } else if (status == STATUS_MOVE) {// 移动控制
                    // System.out.println("移动控制");
                    translateCrop(x - oldx, y - oldy);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                status = STATUS_IDLE;// 回归空闲状态
                break;
        }// end switch

        // 记录上一次动作点
        oldx = x;
        oldy = y;

        return ret;
    }

    /**
     * 移动剪切框
     *
     * @param dx
     * @param dy
     */
    private void translateCrop(float dx, float dy) {
        tempRect.set(mCropRect);// 存贮原有数据，以便还原

        translateRect(mCropRect, dx, dy);
        // 边界判定算法优化
        float mdLeft = imageRect.left - mCropRect.left;
        if (mdLeft > 0) {
            translateRect(mCropRect, mdLeft, 0);
        }
        float mdRight = imageRect.right - mCropRect.right;
        if (mdRight < 0) {
            translateRect(mCropRect, mdRight, 0);
        }
        float mdTop = imageRect.top - mCropRect.top;
        if (mdTop > 0) {
            translateRect(mCropRect, 0, mdTop);
        }
        float mdBottom = imageRect.bottom - mCropRect.bottom;
        if (mdBottom < 0) {
            translateRect(mCropRect, 0, mdBottom);
        }

        this.invalidate();
    }

    /**
     * 移动矩形
     *
     * @param rect
     * @param dx
     * @param dy
     */
    private static final void translateRect(RectF rect, float dx, float dy) {
        rect.left += dx;
        rect.right += dx;
        rect.top += dy;
        rect.bottom += dy;
    }

    /**
     * 操作控制点 控制缩放
     *
     * @param x
     * @param y
     */
    private void scaleCropController(float x, float y) {
        tempRect.set(mCropRect);// 存贮原有数据，以便还原
        switch (selectedControllerCicle) {
            case 1:// 左上角控制点
                mCropRect.left = x;
                mCropRect.top = y;
                break;
            case 2:// 右上角控制点
                mCropRect.right = x;
                mCropRect.top = y;
                break;
            case 3:// 左下角控制点
                mCropRect.left = x;
                mCropRect.bottom = y;
                break;
            case 4:// 右下角控制点
                mCropRect.right = x;
                mCropRect.bottom = y;
                break;
        }// end switch

        if (ratio < 0) {// 任意缩放比
            // 边界条件检测
            validateCropRect();
            invalidate();
        } else {
            // 更新剪切矩形长宽
            // 确定不变点
            switch (selectedControllerCicle) {
                case 1:// 左上角控制点
                case 2:// 右上角控制点
                    mCropRect.bottom = (mCropRect.right - mCropRect.left) / this.ratio
                            + mCropRect.top;
                    break;
                case 3:// 左下角控制点
                case 4:// 右下角控制点
                    mCropRect.top = mCropRect.bottom
                            - (mCropRect.right - mCropRect.left) / this.ratio;
                    break;
            }// end switch

            // validateCropRect();
            if (mCropRect.left < imageRect.left
                    || mCropRect.right > imageRect.right
                    || mCropRect.top < imageRect.top
                    || mCropRect.bottom > imageRect.bottom
                    || mCropRect.width() < CIRCLE_WIDTH
                    || mCropRect.height() < CIRCLE_WIDTH) {
                mCropRect.set(tempRect);
            }
            invalidate();
        }// end if
    }

    /**
     * 边界条件检测
     */
    private void validateCropRect() {
        if (mCropRect.width() < CIRCLE_WIDTH) {
            mCropRect.left = tempRect.left;
            mCropRect.right = tempRect.right;
        }
        if (mCropRect.height() < CIRCLE_WIDTH) {
            mCropRect.top = tempRect.top;
            mCropRect.bottom = tempRect.bottom;
        }
        if (mCropRect.left < imageRect.left) {
            mCropRect.left = imageRect.left;
        }
        if (mCropRect.right > imageRect.right) {
            mCropRect.right = imageRect.right;
        }
        if (mCropRect.top < imageRect.top) {
            mCropRect.top = imageRect.top;
        }
        if (mCropRect.bottom > imageRect.bottom) {
            mCropRect.bottom = imageRect.bottom;
        }
    }

    /**
     * 是否选中控制点
     * <p>
     * -1为没有
     *
     * @param x
     * @param y
     * @return
     */
    private int isSeletedControllerCircle(float x, float y) {
        if (leftTopCircleRect.contains(x, y))// 选中左上角
            return 1;
        if (rightTopCircleRect.contains(x, y))// 选中右上角
            return 2;
        if (leftBottomRect.contains(x, y))// 选中左下角
            return 3;
        if (rightBottomRect.contains(x, y))// 选中右下角
            return 4;
        return -1;
    }

    /**
     * 返回剪切矩形
     *
     * @return
     */
    public RectF getCropRect() {
        return new RectF(this.mCropRect);
    }

    /**
     * 缩放指定矩形
     *
     * @param rect
     */
    private static void scaleRect(RectF rect, float scaleX, float scaleY) {
        float w = rect.width();
        float h = rect.height();

        float newW = scaleX * w;
        float newH = scaleY * h;

        float dx = (newW - w) / 2;
        float dy = (newH - h) / 2;

        rect.left -= dx;
        rect.top -= dy;
        rect.right += dx;
        rect.bottom += dy;
    }

    /**
     * 缩放指定矩形
     *
     * @param rect
     * @param scale
     */
    private static void scaleRect(RectF rect, float scale) {
        scaleRect(rect, scale, scale);
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    /**
     * 根据画面的宽高来调整该控件的大小
     *
     * @param imgWidth
     * @param imgHeight
     */
    public void adjustForImg(int imgWidth, int imgHeight) {
        float radius = HALF_CIRCLE_WIDTH;
        ViewGroup.LayoutParams layoutParamsCrop = getLayoutParams();
        layoutParamsCrop.width = imgWidth + CIRCLE_WIDTH;
        layoutParamsCrop.height = imgHeight + CIRCLE_WIDTH;
        setLayoutParams(layoutParamsCrop);
        RectF rectF = new RectF(radius, radius, radius + imgWidth, radius + imgHeight);
        setCropRect(rectF, 1.0f);
    }

    /**
     * 将该控件的裁剪矩形区域映射到显示图像画面上的矩形区域
     *
     * @return
     */
    public RectF getCropRectForImg() {
        RectF rectF = new RectF();
        if (mCropRect != null) {
            rectF.set(mCropRect);
            float offset = HALF_CIRCLE_WIDTH;
            rectF.offset(-offset, -offset);
        }
        return rectF;
    }

    /**
     * 开始扫描
     */
    public void startScan() {
        if (isScaning) {
            return;
        }
        mScanRect = new RectF(mCropRect);
        float start = mScanRect.top + 1;
        mScanRect.bottom = start;
        ValueAnimator animator = ValueAnimator.ofFloat(start, mCropRect.bottom);
        mScanAnimator = animator;
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mScanRect.bottom = value;
                invalidate();
            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(DEF_SCAN_DURATION);
        animator.start();
        isScaning = true;
    }

    /**
     * 停止扫描
     */
    public void stopScan() {
        if (!isScaning) {
            return;
        }
        if (mScanAnimator != null) {
            mScanAnimator.removeAllListeners();
            mScanAnimator.cancel();
        }
        isScaning = false;
        invalidate();
    }

    public boolean isScaning() {
        return isScaning;
    }
}
