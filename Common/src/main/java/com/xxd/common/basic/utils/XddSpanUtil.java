package com.xxd.common.basic.utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.xxd.common.basic.view.NoUnderlineSpan;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:
 */
public class XddSpanUtil {

    /**
     * 对出现在span中的子文本subText进行进行高亮显示
     *
     * @param span
     * @param subText
     * @param color
     * @param noUnderline   true:表示去除下划线
     * @param clickableSpan
     * @return
     */
    public static SpannableString highLightText(@NonNull SpannableString span, String subText, @ColorInt int color, boolean noUnderline, ClickableSpan clickableSpan) {
        if (TextUtils.isEmpty(subText)) {
            return span;
        }
        String fullText = span.toString();
        int startPos = fullText.indexOf(subText);
        int endPos = startPos + subText.length();
        span.setSpan(clickableSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (noUnderline) {
            span.setSpan(new NoUnderlineSpan(), startPos, endPos, Spanned.SPAN_MARK_MARK);
        }
        span.setSpan(new ForegroundColorSpan(color), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }
}
