package com.xxd.common.basic.utils;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.SizeUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.impl.ConfirmPopupView;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.xxd.common.basic.R;
import com.xxd.common.basic.xpopup.XddImageViewerPopup;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:dialog工具类
 */
public class DialogUtil {


    /**
     * 显示取消、确认对话框（实际不是真正的Dialog）
     *
     * @param context
     * @param title           对话框标题，传空串会隐藏标题
     * @param content         对话框内容
     * @param confirmListener 点击确认的监听器
     * @return
     */
    public static BasePopupView asConfirm(Context context, CharSequence title, CharSequence content, OnConfirmListener confirmListener) {
        return new XPopup.Builder(context).asConfirm(title, content, confirmListener).show();
    }

    /**
     * 显示确认对话框（只显示确认按钮，取消按钮不显示）
     *
     * @param context
     * @param title
     * @param content
     * @param confirmText
     * @param confirmListener
     * @return
     */
    public static BasePopupView asConfirm(Context context, CharSequence title, CharSequence content, CharSequence confirmText,
                                          OnConfirmListener confirmListener) {
        return asConfirm(context, title, content, null, confirmText, confirmListener, null, true);
    }

    /**
     * 显示确认对话框（只显示确认按钮，取消按钮不显示，并且按返回键或对话框外面无法隐藏该对话框）
     *
     * @param context
     * @param title
     * @param content
     * @param confirmText
     * @param confirmListener
     * @return
     */
    public static BasePopupView asConfirmNotCancel(Context context, CharSequence title, CharSequence content, CharSequence confirmText,
                                                   OnConfirmListener confirmListener) {
        return asConfirm(context, title, content, null, confirmText, confirmListener, null, true, 0, false);
    }


    /**
     * 显示深色主题的取消、确认对话框（实际不是真正的Dialog）
     *
     * @param context
     * @param title           对话框标题，传空串会隐藏标题
     * @param content         对话框内容
     * @param confirmListener 点击确认的监听器
     * @return
     */
    public static BasePopupView asConfirmDarkTheme(Context context, CharSequence title, CharSequence content, OnConfirmListener confirmListener) {
        return new XPopup.Builder(context).isDarkTheme(true).asConfirm(title, content, confirmListener).show();
    }


    /**
     * 显示取消、确认对话框（实际不是真正的Dialog）
     *
     * @param context
     * @param title           对话框标题，传空串会隐藏标题
     * @param content         对话框内容
     * @param cancelBtnText   取消按钮的文字内容
     * @param confirmBtnText  确认按钮的文字内容
     * @param confirmListener 点击确认的监听器
     * @param cancelListener  点击取消的监听器
     * @param isHideCancel    是否隐藏取消按钮
     * @return
     */
    public static BasePopupView asConfirm(Context context, CharSequence title, CharSequence content,
                                          CharSequence cancelBtnText, CharSequence confirmBtnText,
                                          OnConfirmListener confirmListener, OnCancelListener cancelListener,
                                          boolean isHideCancel) {
        return asConfirm(context, title, content, cancelBtnText, confirmBtnText,
                confirmListener, cancelListener, isHideCancel, 0);
    }

    /**
     * 显示确认和取消对话框
     *
     * @param context
     * @param title           对话框标题，传空串会隐藏标题
     * @param content         对话框内容
     * @param cancelBtnText   取消按钮的文字内容
     * @param confirmBtnText  确认按钮的文字内容
     * @param confirmListener 点击确认的监听器
     * @param cancelListener  点击取消的监听器
     * @param isHideCancel    是否隐藏取消按钮
     * @param bindLayoutId    自定义的布局Id，没有则传0；要求自定义布局中必须包含的TextView以及id有：tv_title，tv_content，tv_cancel，tv_confirm
     * @return
     */
    public static BasePopupView asConfirm(Context context, CharSequence title, CharSequence content,
                                          CharSequence cancelBtnText, CharSequence confirmBtnText,
                                          OnConfirmListener confirmListener, OnCancelListener cancelListener,
                                          boolean isHideCancel, @LayoutRes int bindLayoutId) {
        return asConfirm(context, title, content, cancelBtnText, confirmBtnText, confirmListener, cancelListener,
                isHideCancel, bindLayoutId, true);
    }

    public static BasePopupView asConfirm(Context context, CharSequence title, CharSequence content,
                                          CharSequence cancelBtnText, CharSequence confirmBtnText,
                                          OnConfirmListener confirmListener, OnCancelListener cancelListener,
                                          boolean isHideCancel, @LayoutRes int bindLayoutId, boolean canCancel) {
        return new XPopup.Builder(context)
                .dismissOnBackPressed(canCancel)
                .dismissOnTouchOutside(canCancel)
                .asConfirm(title, content, cancelBtnText, confirmBtnText,
                        confirmListener, cancelListener, isHideCancel, bindLayoutId)
                .show();
    }

    /**
     * 显示带有输入框，确认和取消对话框
     *
     * @param title           对话框标题，传空串会隐藏标题
     * @param content         对话框内容,，传空串会隐藏
     * @param confirmListener 点击确认的监听器
     * @return
     */
    public static BasePopupView asInputConfirm(Context context, CharSequence title, CharSequence content,
                                               OnInputConfirmListener confirmListener) {
        return new XPopup.Builder(context).asInputConfirm(title, content, confirmListener).show();
    }


    /**
     * 显示带有三个点击按钮的对话框
     *
     * @param title           对话框标题，传空串会隐藏标题
     * @param content         对话框内容,，传空串会隐藏
     * @param cancelBtnText   取消按钮的文字内容
     * @param confirmBtnText  确认按钮的文字内容
     * @param middleBtnText   中间按钮文字内容
     * @param confirmListener 点击确认的监听器
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static BasePopupView asYNCConfirm(Context context, CharSequence title, CharSequence content,
                                             CharSequence cancelBtnText, CharSequence confirmBtnText, CharSequence middleBtnText,
                                             OnConfirmListener confirmListener, View.OnClickListener middleListener) {
        return asYNCConfirm(context, title, content, cancelBtnText, confirmBtnText, middleBtnText, confirmListener, middleListener, 0, 0, context.getColor(R.color.stk_xpopup_confirm_color_1));
    }



    /**
     * 显示带有三个点击按钮的对话框
     *
     * @param title           对话框标题，传空串会隐藏标题
     * @param content         对话框内容,，传空串会隐藏
     * @param cancelBtnText   取消按钮的文字内容
     * @param confirmBtnText  确认按钮的文字内容
     * @param middleBtnText   中间按钮文字内容
     * @param confirmListener 点击确认的监听器
     * @param headResId       顶部图标
     * @param confirmBgId     确认按钮背景
     * @param middleColor     中间按钮颜色
     * @return
     */
    public static BasePopupView asYNCConfirm(Context context, CharSequence title, CharSequence content,
                                             CharSequence cancelBtnText, CharSequence confirmBtnText, CharSequence middleBtnText,
                                             OnConfirmListener confirmListener, View.OnClickListener middleListener,
                                             int headResId, int confirmBgId, @ColorInt int middleColor) {

        boolean hideMiddle = TextUtils.isEmpty(middleBtnText);
        ConfirmPopupView cpv = new XPopup.Builder(context).asConfirm(title, content, cancelBtnText, confirmBtnText,
                confirmListener, null, false, R.layout.xxd_xpopup_ync_confirm);
        TextView tvMiddle = cpv.findViewById(R.id.tv_middle);
        if (!hideMiddle) {
            tvMiddle.setVisibility(View.VISIBLE);
            tvMiddle.setTextColor(middleColor);
            tvMiddle.setBackground(XddDrawableUtil.getBgDrawable(2, middleColor, SizeUtils.dp2px(25)));
            tvMiddle.setText(middleBtnText);
            tvMiddle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cpv.dismiss();
                    if (middleListener != null) {
                        middleListener.onClick(view);
                    }
                }
            });
        }
        ImageView ivHeader = cpv.findViewById(R.id.iv_header);
        if (headResId > 0) {
            ivHeader.setImageResource(headResId);
        }
        TextView tvConfirm = cpv.findViewById(R.id.tv_confirm);
        if (confirmBgId > 0) {
            tvConfirm.setBackgroundResource(confirmBgId);
        }
        return cpv.show();
    }

    /**
     * 弹窗显示图片的dialog
     *
     * @param context
     * @param imageUrl
     * @param isDismissOnTouchOutside
     * @return
     */
    public static BasePopupView asImageViewer(Context context, String imageUrl, boolean isDismissOnTouchOutside) {
        return new XPopup.Builder(context).dismissOnTouchOutside(isDismissOnTouchOutside).asCustom(new XddImageViewerPopup(context).setImageUrl(imageUrl)).show();
    }

}
