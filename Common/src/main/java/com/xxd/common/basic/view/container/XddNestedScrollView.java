package com.xxd.common.basic.view.container;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:
 */
public class XddNestedScrollView extends NestedScrollView {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public XddNestedScrollView(@NonNull Context context) {
        this(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public XddNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public XddNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        XddContainerUtil.setViewOutline(this, attrs);
    }

}
