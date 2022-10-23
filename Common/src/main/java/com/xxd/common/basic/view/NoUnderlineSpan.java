package com.xxd.common.basic.view;

import android.text.TextPaint;
import android.text.style.UnderlineSpan;

import androidx.annotation.NonNull;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:去除下划线的span
 */
public class NoUnderlineSpan extends UnderlineSpan {
    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        //super.updateDrawState(ds);
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false);
    }
}
