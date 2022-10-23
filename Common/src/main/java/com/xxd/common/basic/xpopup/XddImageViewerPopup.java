package com.xxd.common.basic.xpopup;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.lxj.xpopup.core.CenterPopupView;
import com.xxd.common.basic.R;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:
 */
public class XddImageViewerPopup extends CenterPopupView implements View.OnClickListener {
    public interface XddImageViewerCallBack {
        void onImageClick();
    }

    private boolean isHideClose = false;
    private ImageView mIvViewer, mIvClose;
    private String mImageUrl;
    private int mCloseResId;

    private XddImageViewerCallBack mCallback;

    public XddImageViewerPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.xxd_xpopup_image_viewer;
    }

    @Override
    protected int getMaxHeight() {
        return ScreenUtils.getAppScreenWidth();
    }

    @Override
    protected int getMaxWidth() {
        return ScreenUtils.getAppScreenWidth();
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        mIvViewer = findViewById(R.id.iv_viewer);
        mIvClose = findViewById(R.id.iv_close);

        if (isHideClose) {
            mIvClose.setVisibility(View.GONE);
        } else {
            if (mCloseResId > 0) {
                mIvClose.setImageResource(mCloseResId);
            }
            mIvClose.setOnClickListener(this);
        }
        mIvViewer.setOnClickListener(this);
        Glide.with(this).load(mImageUrl).into(mIvViewer);

    }

    public XddImageViewerPopup setCloseHide(boolean isHide) {
        isHideClose = isHide;
        return this;
    }

    public XddImageViewerPopup setCloseIcon(@IdRes int resId) {
        mCloseResId = resId;
        return this;
    }

    public XddImageViewerPopup setImageUrl(String url) {
        mImageUrl = url;
        return this;
    }

    public XddImageViewerPopup setImageClickCb(XddImageViewerCallBack callback) {
        mCallback = callback;
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v == mIvClose) {
            dismiss();
        } else if (v == mIvViewer) {
            if (mCallback != null) {
                mCallback.onImageClick();
                dismiss();
            }
        }
    }
}
