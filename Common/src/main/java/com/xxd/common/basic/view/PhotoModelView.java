package com.xxd.common.basic.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ImageUtils;
import com.xxd.common.basic.utils.ViewUtil;

/**
 * @author:XiaoDan
 * @time:2022/8/15
 * @desc:该控件为适用于上层为带镂空孔的模板图片，z轴方向底部显示在镂空处的图片的场景
 */
public class PhotoModelView extends FrameLayout {

    /**
     * 上层模版控件的透明化时的默认透明度
     */
    private static final float MODEL_VIEW_ALPHA = 0.5F;
    private TransformImageView mTfImgView;
    private ImageView mModelImgView;
    private OnClickListener mClickListener;
    private float mModelViewAlpha = MODEL_VIEW_ALPHA;

    public PhotoModelView(Context context) {
        this(context, null);
    }

    public PhotoModelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoModelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        //底下可移动、缩放、旋转变换的图片控件
        TransformImageView tfImageView = new TransformImageView(getContext(), attrs);
        mTfImgView = tfImageView;
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        tfImageView.setLayoutParams(layoutParams);
        addView(tfImageView);

        //上面带镂空区域的模板图片控件
        ImageView imageView = new ImageView(getContext(), attrs);
        mModelImgView = imageView;
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        imageView.setLayoutParams(layoutParams);
        addView(imageView);
        imageView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //下面这里的处理是将底层显示图片控件的大小调整为上层模板view的显示画面的大小
                Drawable drawable = imageView.getDrawable();
                if (drawable != null) {
                    Point size = ViewUtil.getImgSize(imageView);
                    LayoutParams tfLayoutParams = (LayoutParams) tfImageView.getLayoutParams();
                    if ((size.x > 0 || size.y > 0) && (tfLayoutParams.width != size.x || tfLayoutParams.height != size.y)) {
                        tfLayoutParams.width = size.x;
                        tfLayoutParams.height = size.y;
                        tfImageView.setLayoutParams(tfLayoutParams);
                    }

                }
            }
        });

    }

    private GestureDetector mGestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (mClickListener != null) {
                mClickListener.onClick(PhotoModelView.this);
                return true;
            }
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mModelImgView.setAlpha(mModelViewAlpha);
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            //长按该控件时，要将上层的模板控件透明化，以能看到底下的图片控件
            mModelImgView.setAlpha(mModelViewAlpha);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    });

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_POINTER_DOWN:
                //双指按下时，要将上层的模板控件透明化，以能看到底下的图片控件
                mModelImgView.setAlpha(mModelViewAlpha);
                break;
            case MotionEvent.ACTION_UP:
                mModelImgView.setAlpha(1.0f);
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener listener) {
        super.setOnClickListener(listener);
        mClickListener = listener;
    }

    /**
     * 获取上层模板控件
     *
     * @return
     */
    public ImageView getModelImgView() {
        return mModelImgView;
    }

    /**
     * 获取位于模板控件z轴底部的图片变换控件
     *
     * @return
     */
    public TransformImageView getTfImgView() {
        return mTfImgView;
    }

    /**
     * 设置上层模板控件在触摸时显示的透明度
     *
     * @param modelViewAlpha
     */
    public void setModelViewAlpha(float modelViewAlpha) {
        this.mModelViewAlpha = modelViewAlpha;
    }

    /**
     * 获取该模板控件的结果位图
     *
     * @return
     */
    public Bitmap getRetBitmap() {
        Bitmap viewBmp = ImageUtils.view2Bitmap(this);
        if (viewBmp == null) return null;

        Bitmap retBmp = viewBmp;
        try {
            Point imgSize = ViewUtil.getImgSize(mModelImgView);
            //mModelImgView画面实际大小与mModelImgView控件大小的缩放因子
            float scaleX = imgSize.x * 1f / mModelImgView.getWidth();
            float scaleY = imgSize.y * 1f / mModelImgView.getHeight();

            //实际结果画面宽、高
            int realBmpWidth = (int) (viewBmp.getWidth() * scaleX);
            int realBmpHeight = (int) (viewBmp.getHeight() * scaleY);

            int x = (viewBmp.getWidth() - realBmpWidth) / 2;
            int y = (viewBmp.getHeight() - realBmpHeight) / 2;
            retBmp = Bitmap.createBitmap(viewBmp, x, y, realBmpWidth, realBmpHeight);
            viewBmp.recycle();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return retBmp;
    }
}
