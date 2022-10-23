package com.xxd.common.basic.view.recycler;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:
 */
public class XddPagerLayoutManager extends LinearLayoutManager {


    private static final String TAG = XddPagerLayoutManager.class.getSimpleName();

    private PagerSnapHelper mPagerSnapHelper;
    private OnPageChangeListener onPageChangeListener;
    //位移，用来判断移动方向
    private int mDrift;

    public XddPagerLayoutManager(Context context, int orientation) {
        this(context, orientation, false);
    }

    public XddPagerLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        init();
    }

    private void init() {
        mPagerSnapHelper = new PagerSnapHelper();
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        mPagerSnapHelper.attachToRecyclerView(view);
        view.addOnChildAttachStateChangeListener(mChildAttachStateChangeListener);
    }

    /**
     * 滑动状态的改变
     * 缓慢拖拽-> SCROLL_STATE_DRAGGING
     * 快速滚动-> SCROLL_STATE_SETTLING
     * 空闲状态-> SCROLL_STATE_IDLE
     *
     * @param state
     */
    @Override
    public void onScrollStateChanged(int state) {
        switch (state) {
            case RecyclerView.SCROLL_STATE_IDLE:
                updatePage();
                break;
        }
    }

    private void updatePage() {
        View view = mPagerSnapHelper.findSnapView(this);
        if (view == null) {
            return;
        }
        int position = getPosition(view);
        if (onPageChangeListener != null && getChildCount() == 1) {
            onPageChangeListener.onPageSelected(position, position == getItemCount() - 1);
        }
    }

    /**
     * 监听竖直方向的相对偏移量
     *
     * @param dy
     * @param recycler
     * @param state
     * @return
     */
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dy;
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    /**
     * 监听水平方向的相对偏移量
     *
     * @param dx
     * @param recycler
     * @param state
     * @return
     */
    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dx;
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    /**
     * 设置监听
     *
     * @param onPageChangeListener
     */
    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    private final RecyclerView.OnChildAttachStateChangeListener mChildAttachStateChangeListener = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(@NotNull View view) {
            if (onPageChangeListener != null) {
                int childCount = getChildCount();
                if (childCount == 1) {
                    int position = getPosition(view);
                    onPageChangeListener.onPageSelected(position, position == getItemCount() - 1);
                } else if (childCount == 2) {
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            updatePage();
                        }
                    });
                }
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(@NotNull View view) {
            if (onPageChangeListener == null) {
                return;
            }
            int position = getPosition(view);
            if (mDrift >= 0) {
                onPageChangeListener.onPageRelease(true, position);
            } else {
                onPageChangeListener.onPageRelease(false, position);
            }
        }
    };

    /**
     * 监听器
     */
    public interface OnPageChangeListener {

        /**
         * 选中的监听以及判断是否滑动到底部
         *
         * @param position
         * @param isBottom
         */
        void onPageSelected(int position, boolean isBottom);

        /**
         * 释放的监听
         *
         * @param isNext
         * @param position
         */
        void onPageRelease(boolean isNext, int position);

    }
}
