package com.xxd.common.basic.view.container;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:自定义RadioGroup
 */
public class XddRadioGroup extends RadioGroup {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public XddRadioGroup(@NonNull Context context) {
        this(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public XddRadioGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        XddContainerUtil.setViewOutline(this, attrs);
    }


}
