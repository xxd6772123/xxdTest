package com.xxd.common.basic.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.xxd.common.basic.R;

import java.util.List;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:
 */

public class IndexBar extends View {
    private static final String TAG = IndexBar.class.getSimpleName();
    private Paint mPaint;
    private int mIndex = -1;
    @Nullable
    private List<String> mLetters;
    private int mCellWidth;
    private float mCellHeight;
    private int mNormalColor;
    private int mSelecColor;
    private float mIndexTextSize;
    /**
     * 字母与字母间的间隔
     */
    private float mIndexInterval;

    public IndexBar(Context context) {
        this(context, null);
    }

    public IndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.IndexBar, defStyleAttr, 0);
        mNormalColor = a.getColor(R.styleable.IndexBar_normalColor, Color.GRAY);
        mSelecColor = a.getColor(R.styleable.IndexBar_selectColor, Color.BLUE);
        mIndexTextSize = a.getDimensionPixelSize(R.styleable.IndexBar_indexSize, sp2px(14));
        mIndexInterval = a.getDimensionPixelSize(R.styleable.IndexBar_indexInterval, sp2px(2));
        a.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mNormalColor);
        mPaint.setTextSize(mIndexTextSize);
        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        //加10是考虑到汉字的高度
        mCellHeight = Math.abs(fontMetrics.bottom) + Math.abs(fontMetrics.top) + 10;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLetters != null) {
            for (int i = 0; i < mLetters.size(); i++) {
                String text = mLetters.get(i);
                float x = mCellWidth / 2f;
                float y = getPaddingTop() + mCellHeight / 2 + (mCellHeight + mIndexInterval) * i;
                mPaint.setColor(mIndex == i ? mSelecColor : mNormalColor);
                if (mOnDrawLetterBgListener != null && mIndex == i) {
                    RectF rectF = new RectF();
                    rectF.left = getPaddingLeft();
                    rectF.top = y - mCellHeight / 2;
                    rectF.right = mCellWidth - getPaddingRight();
                    rectF.bottom = y + mCellHeight / 2;
                    mOnDrawLetterBgListener.onDrawLetterBg(canvas, rectF, mIndex, text);
                }
                canvas.drawText(text, x, y, mPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mLetters == null || mLetters.size() == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int height = (int) (mCellHeight * mLetters.size() +
                    mIndexInterval * (mLetters.size() - 1) + getPaddingTop() + getPaddingBottom());
            int width = (int) (getLetterMaxWidth() + getPaddingLeft() + getPaddingRight());
            mCellWidth = width;
            setMeasuredDimension(width, height);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //getParent().requestDisallowInterceptTouchEvent(true);
                y = event.getY();
                checkIndex(y);
                return true;
            case MotionEvent.ACTION_MOVE:
                /*y = event.getY();
                checkIndex(y);
                break;*/
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mOnDrawLetterBgListener == null) {
                    mIndex = -1;
                }
                invalidate();
                break;
            default:
                break;
        }
        return false;
    }

    @Nullable
    public List<String> getLetters() {
        return mLetters;
    }

    public void setLetters(@Nullable List<String> letters) {
        if (letters == null || letters.size() == 0) {
            setVisibility(GONE);
            return;
        }
        if (getVisibility() == View.GONE) {
            setVisibility(View.VISIBLE);
        }
        this.mLetters = letters;
        requestLayout();
    }

    private float getLetterMaxWidth() {
        float width = 0;
        for (String letter : mLetters) {
            width = Math.max(width, mPaint.measureText(letter));
        }
        return width;
    }

    private void checkIndex(float y) {
        if (y < getPaddingTop()) {
            return;
        }
        int currentIndex = (int) ((y - getPaddingTop()) / (mCellHeight + mIndexInterval));
        if (currentIndex != mIndex) {
            if (mOnLetterChangeListener != null) {
                if (mLetters != null && currentIndex < mLetters.size()) {
                    mOnLetterChangeListener.onLetterChange(currentIndex, mLetters.get(currentIndex));
                    mIndex = currentIndex;
                }
            }
            invalidate();
        }
    }

    public int sp2px(float sp) {
        float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scaledDensity + 0.5f);
    }

    public void clearIndex() {
        mIndex = -1;
        invalidate();
    }

    /**
     * 监听器
     */
    public interface OnLetterChangeListener {

        /**
         * 所选字母改变时回调的方法
         *
         * @param position
         * @param letter
         */
        void onLetterChange(int position, String letter);

    }

    private OnLetterChangeListener mOnLetterChangeListener;

    public void setOnLetterChangeListener(OnLetterChangeListener onLetterChangeListener) {
        mOnLetterChangeListener = onLetterChangeListener;
    }

    private OnDrawLetterBgListener mOnDrawLetterBgListener;

    public void setOnDrawLetterBgListener(OnDrawLetterBgListener onDrawLetterBgListener) {
        this.mOnDrawLetterBgListener = onDrawLetterBgListener;
    }

    /**
     * 提供给外部在绘制索引关键词之前绘制其背景的接口
     */
    public interface OnDrawLetterBgListener {

        /**
         * @param canvas
         * @param letterRegion 索引词所在可供绘制背景的矩形区域
         * @param letterIndex  索引词对应的索引值，从0开始
         * @param letter       索引词
         */
        void onDrawLetterBg(Canvas canvas, RectF letterRegion, int letterIndex, String letter);

    }
}
