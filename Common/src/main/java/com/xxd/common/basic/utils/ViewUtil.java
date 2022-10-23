package com.xxd.common.basic.utils;

import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:涉及到View相关的工具类
 */
public class ViewUtil {


    /**
     * 根据实际画面比例的大小来获取view的符合该比例的大小
     * （例如在根据视频实际比例画面大小来设置VideoView的大小）
     *
     * @param view
     * @param imgWidth
     * @param imgHeight
     * @return
     */
    public static Point getMatchImgRatioSize(View view, int imgWidth, int imgHeight) {
        Point point = new Point();
        point.x = view.getWidth();
        point.y = view.getHeight();

        float realRatio = imgWidth * 1.0f / imgHeight;
        if (point.x * 1f / point.y > realRatio) {
            point.x = (int) (point.y * realRatio);
        } else {
            point.y = (int) (point.x / realRatio);
        }
        return point;
    }

    /**
     * 给TextView控件设置文本
     *
     * @param textView
     * @param text
     */
    public static void setViewText(TextView textView, CharSequence text) {
        if (textView != null) {
            textView.setText(text);
        }
    }

    /**
     * 设置控件显示或隐藏
     *
     * @param view
     * @param visibility
     */
    public static void setViewVisibility(View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    /**
     * 给控件设置点击事件监听器
     *
     * @param view
     * @param listener
     */
    public static void setViewClickListener(View view, View.OnClickListener listener) {
        if (view != null) {
            view.setOnClickListener(listener);
        }
    }

    /**
     * 给控件设置触摸事件监听器
     *
     * @param view
     * @param listener
     */
    public static void setViewTouchListener(View view, View.OnTouchListener listener) {
        if (view != null) {
            view.setOnTouchListener(listener);
        }
    }

    /**
     * 计算view上的矩形区域rectF在实际画面中的矩形区域
     *
     * @param view          显示画面的view
     * @param realImgWidth  画面的实际宽度（如图片、视频文件的实际宽度）
     * @param realImgHeight 画面的实际高度（如图片、视频文件的实际高度）
     * @param rectInView    view上矩形区域
     * @return rectInView对应的在实际画面中的矩形区域
     */
    public static RectF getRealRect(@NonNull View view, int realImgWidth, int realImgHeight,
                                    @NonNull RectF rectInView) {
        return getRealRect(view.getWidth(), view.getHeight(), realImgWidth, realImgHeight, rectInView);
    }

    /**
     * 计算在显示画面中的矩形区域对应在实际画面中的矩形区域
     *
     * @param imgShowWidth  显示画面的宽
     * @param imgShowHeight 显示画面的高
     * @param realImgWidth  实际画面的宽
     * @param realImgHeight 实际画面的高
     * @param rectInShowImg 在显示画面中的矩形区域
     * @return rectInShowImg对应的在实际画面中的区域
     */
    public static RectF getRealRect(int imgShowWidth, int imgShowHeight, int realImgWidth, int realImgHeight,
                                    @NonNull RectF rectInShowImg) {
        float scaleW = realImgWidth * 1f / imgShowWidth;
        float scaleH = realImgHeight * 1f / imgShowHeight;

        RectF realRect = new RectF();
        realRect.left = rectInShowImg.left * scaleW;
        realRect.right = rectInShowImg.right * scaleW;
        realRect.top = rectInShowImg.top * scaleH;
        realRect.bottom = rectInShowImg.bottom * scaleH;

        return realRect;
    }

    /**
     * 获取ImageView中画面显示的宽高
     *
     * @param imageView
     * @return Point Point.x为画面宽 Point.y为画面高
     */
    public static Point getImgSize(@NonNull ImageView imageView) {
        Point point = new Point();
        Drawable drawable = imageView.getDrawable();
        if (drawable == null) {
            return point;
        }
        //图片的真实宽高
        int oriWidth = drawable.getBounds().width();
        int oriHeight = drawable.getBounds().height();

        //变换矩阵
        Matrix matrix = imageView.getImageMatrix();
        float[] values = new float[10];
        matrix.getValues(values);

        //宽、高缩放因子
        float sW = values[0];
        float sH = values[4];

        //ImageView实际绘制的画面的宽高
        point.x = (int) (oriWidth * sW);
        point.y = (int) (oriHeight * sH);
        return point;
    }

    /**
     * 判断位置坐标是否处于view控件区域内
     *
     * @param view
     * @param x    相对于屏幕的横坐标
     * @param y    相对于屏幕的纵坐标
     * @return
     */
    public static boolean isLocateInView(@NonNull View view, float x, float y) {
        int[] loc = new int[2];
        //view.getLocationInWindow(loc);
        view.getLocationOnScreen(loc);
        RectF rectF = new RectF();
        rectF.left = loc[0];
        rectF.right = rectF.left + view.getWidth();
        rectF.top = loc[1];
        rectF.bottom = rectF.top + view.getHeight();
        return rectF.contains(x, y);
    }

    /**
     * 将View从其父控件中移除掉
     *
     * @param view
     */
    public static void removeViewFromParent(View view) {
        if (view == null) return;
        ViewParent viewParent = view.getParent();
        if (viewParent == null) return;
        if (!(viewParent instanceof ViewGroup)) return;
        ((ViewGroup) viewParent).removeView(view);
    }
}
