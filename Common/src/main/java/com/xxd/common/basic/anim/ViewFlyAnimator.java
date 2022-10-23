package com.xxd.common.basic.anim;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:一个控件飞到另外一个控件位置处的动画类
 */
public class ViewFlyAnimator {

    private Activity mActivity;
    //是否正在执行动画的标志
    private boolean isAnimRunning = false;
    //需执行动画控件的数量
    private int animViewCount;
    //动画期间是否允许触摸的标志
    private boolean canTouch = true;

    public ViewFlyAnimator(@NonNull Activity activity) {
        mActivity = activity;
    }

    public void start(@NonNull View startView, @NonNull View endView, IListener listener) {
        start(startView, endView, 300, null, listener);
    }

    public void start(@NonNull View startView, @NonNull View endView, long duration, Interpolator interpolator, IListener listener) {
        List<View> startViews = new ArrayList<>();
        startViews.add(startView);
        List<View> endViews = new ArrayList<>();
        endViews.add(endView);
        start(startViews, endViews, duration, interpolator, listener);
    }

    public void start(@NonNull List<View> startViews, @NonNull List<View> endViews, IListener listener) {
        start(startViews, endViews, 300, null, listener);
    }

    public void start(@NonNull List<View> startViews, @NonNull List<View> endViews, long duration, Interpolator interpolator, IListener listener) {
        isAnimRunning = true;
        ViewGroup decorView = (ViewGroup) mActivity.getWindow().getDecorView();

        //anim layout
        FrameLayout animLayout = new FrameLayout(mActivity);
        animLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //背景设置为透明
        animLayout.setBackgroundColor(Color.parseColor("#00000000"));
        //处理触摸
        if (!canTouch) {
            //动画期间，不允许触摸，则内部直接消费掉触摸事件
            animLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
        decorView.addView(animLayout);

        animViewCount = 0;
        for (int i = 0; i < startViews.size() && i < endViews.size(); i++) {
            animViewCount++;
            startInternal(animLayout, startViews.get(i), endViews.get(i), duration, interpolator, new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    animViewCount--;
                    if (animViewCount > 0) {
                        return;
                    }
                    //都结束了
                    decorView.removeView(animLayout);
                    if (listener != null) {
                        listener.onAnimEnd();
                    }
                    decorView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isAnimRunning = false;
                        }
                    }, 50);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    private void startInternal(ViewGroup animLayout, @NonNull View startView, @NonNull View endView, long duration, Interpolator interpolator, Animation.AnimationListener listener) {
        //anim view
        int animViewWidth = startView.getWidth();
        int animViewHeight = startView.getHeight();
        int[] startViewLoc = new int[2];
        int[] endViewLoc = new int[2];
        startView.getLocationInWindow(startViewLoc);
        endView.getLocationInWindow(endViewLoc);
        Bitmap animBmp = ImageUtils.view2Bitmap(startView);

        ImageView animView = new ImageView(mActivity);
        FrameLayout.LayoutParams animViewLayoutParams = new FrameLayout.LayoutParams(animViewWidth, animViewHeight);
        animViewLayoutParams.leftMargin = startViewLoc[0];
        animViewLayoutParams.topMargin = startViewLoc[1];
        animView.setLayoutParams(animViewLayoutParams);
        animView.setImageBitmap(animBmp);
        animLayout.addView(animView);

        float fromX, toX, fromY, toY;
        AnimationSet animSet = new AnimationSet(true);

        fromX = 1.0f;
        fromY = 1.0f;
        toX = endView.getWidth() * 1f / startView.getWidth();
        toY = endView.getHeight() * 1f / startView.getHeight();
        ScaleAnimation scaleAnimation = new ScaleAnimation(fromX, toX, fromY, toY);
        animSet.addAnimation(scaleAnimation);

        float endX = endViewLoc[0] - startViewLoc[0];
        float endY = endViewLoc[1] - startViewLoc[1];
        TranslateAnimation translateAnimation = new TranslateAnimation(0, endX, 0, endY);
        animSet.addAnimation(translateAnimation);

        animSet.setDuration(duration);
        if (interpolator != null) {
            animSet.setInterpolator(interpolator);
        }
        animSet.setAnimationListener(listener);
        animView.startAnimation(animSet);
    }

    public boolean isAnimRunning() {
        return isAnimRunning;
    }

    /**
     * 设置动画期间，是否允许触摸
     *
     * @param canTouch
     */
    public void setCanTouch(boolean canTouch) {
        this.canTouch = canTouch;
    }

    public static abstract class IListener {

        public void onAnimStart() {
        }

        public void onAnimEnd() {
        }
    }

}
