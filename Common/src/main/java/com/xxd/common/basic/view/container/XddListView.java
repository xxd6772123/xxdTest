package com.xxd.common.basic.view.container;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:自定义ListView
 */
public class XddListView extends ListView {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public XddListView(@NonNull Context context) {
        this(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public XddListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public XddListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        XddContainerUtil.setViewOutline(this, attrs);
    }

}
