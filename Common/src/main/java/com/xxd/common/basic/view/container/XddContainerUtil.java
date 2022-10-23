package com.xxd.common.basic.view.container;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.xxd.common.basic.R;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:Stk为前缀的容器控件工具类
 */
public class XddContainerUtil {


    /**
     * 设置控件的Outline
     *
     * @param view
     * @param attrs
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setViewOutline(@NonNull View view, AttributeSet attrs) {
        Context context = view.getContext();
        if (context == null) {
            return;
        }
        final float defValue = -1;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.StkContainerStyle);

        float cornerRadius = array.getDimension(R.styleable.StkContainerStyle_stk_corner_radius, defValue);
        float ltRadius = array.getDimension(R.styleable.StkContainerStyle_stk_lt_radius, defValue);
        float rtRadius = array.getDimension(R.styleable.StkContainerStyle_stk_rt_radius, defValue);
        float lbRadius = array.getDimension(R.styleable.StkContainerStyle_stk_lb_radius, defValue);
        float rbRadius = array.getDimension(R.styleable.StkContainerStyle_stk_rb_radius, defValue);
        if (cornerRadius == defValue && ltRadius == defValue && rtRadius == defValue
                && lbRadius == defValue && rbRadius == defValue) {
            array.recycle();
            return;
        }

        view.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                Drawable drawable = view.getBackground();
                RectF rectF = new RectF();
                if (drawable != null) {
                    Rect bounds = drawable.getBounds();
                    if (bounds != null && !bounds.isEmpty()) {
                        rectF.set(bounds);
                    } else {
                        rectF.set(0, 0, view.getWidth(), view.getHeight());
                    }
                } else {
                    rectF.set(0, 0, view.getWidth(), view.getHeight());
                }

                //不起作用，所以暂时先注释掉
                /*Path path = new Path();
                float[] radii = new float[8];
                for (int i = 0; i < radii.length; i++) {
                    radii[i] = 0f;
                }
                //left top corner
                if (ltRadius >= 0) {
                    radii[0] = radii[1] = ltRadius;
                } else if (cornerRadius >= 0) {
                    radii[0] = radii[1] = cornerRadius;
                }

                //right top corner
                if (rtRadius >= 0) {
                    radii[2] = radii[3] = rtRadius;
                } else if (cornerRadius >= 0) {
                    radii[2] = radii[3] = cornerRadius;
                }

                //right bottom corner
                if (rbRadius >= 0) {
                    radii[4] = radii[5] = rbRadius;
                } else if (cornerRadius >= 0) {
                    radii[4] = radii[5] = cornerRadius;
                }

                //left bottom corner
                if (lbRadius >= 0) {
                    radii[6] = radii[7] = lbRadius;
                } else if (cornerRadius >= 0) {
                    radii[6] = radii[7] = cornerRadius;
                }

                path.addRoundRect(rectF, radii, Path.Direction.CW);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    outline.setPath(path);
                } else {
                    outline.setConvexPath(path);
                }*/

                Rect rect = new Rect();
                rect.left = (int) rectF.left;
                rect.top = (int) rectF.top;
                rect.right = (int) rectF.right;
                rect.bottom = (int) rectF.bottom;
                outline.setRoundRect(rect, cornerRadius);

                outline.setAlpha(0f);
            }
        });
        view.setClipToOutline(true);

        array.recycle();
    }

}
