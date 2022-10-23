package com.xxd.common.basic.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * @author:XiaoDan
 * @time:2022/8/26
 * @desc:上边2个圆角，下方是2个直角
 */
public class TopRoundImageView extends AppCompatImageView {

    float width, height;

    public TopRoundImageView(Context context) {
        this(context, null);
        init(context, null);
    }

    public TopRoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    public TopRoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //width是图片的宽，height是图片的高
        if (width >= 50 && height > 50) {
            Path path = new Path();
            //四个圆角原代码
           /* path.moveTo(50, 0);
            path.lineTo(width - 50, 0);
            path.quadTo(width, 0, width, 50);
            path.lineTo(width, height - 50);
            path.quadTo(width, height, width - 50, height);
            path.lineTo(50, height);
            path.quadTo(0, height, 0, height - 50);
            path.lineTo(0, 50);
            path.quadTo(0, 0, 50, 0);*/

            path.moveTo(50, 0);//起始位置50是左角的圆
            path.lineTo(width - 50, 0);//画直线，这2句代码是在图片的最上方画一条留了左右圆角半径的直线
            path.quadTo(width, 0, width, 50);//画右上圆角
            path.lineTo(width, height);//画 ‘画右上圆角’后的直线一直到右下角的边界
            path.lineTo(0, height);//从右下角的边界一直画到左下角的直线
            path.lineTo(0, 50);//从左下角一直画到左上角的画圆的开始位置
            path.quadTo(0, 0, 50, 0);//画左上角的圆

            canvas.clipPath(path);
        }
        super.onDraw(canvas);
    }
}
