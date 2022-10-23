package com.xxd.common.basic.view.container;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TableLayout;

import androidx.annotation.RequiresApi;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:自定义表格布局
 */
public class XddTableLayout extends TableLayout {

    public XddTableLayout(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public XddTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        XddContainerUtil.setViewOutline(this, attrs);
    }
}
