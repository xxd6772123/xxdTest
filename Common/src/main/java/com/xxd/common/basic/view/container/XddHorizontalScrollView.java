package com.xxd.common.basic.view.container;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:
 */
public class XddHorizontalScrollView extends HorizontalScrollView {

    public XddHorizontalScrollView(@NonNull Context context) {
        this(context, null);
    }

    public XddHorizontalScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public XddHorizontalScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        XddContainerUtil.setViewOutline(this, attrs);
    }

}
