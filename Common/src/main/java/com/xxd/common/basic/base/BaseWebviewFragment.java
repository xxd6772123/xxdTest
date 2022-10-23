package com.xxd.common.basic.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xxd.common.basic.utils.LogUtil;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:链接显示
 */
public class BaseWebviewFragment extends Fragment {

    public static final String KEY_URL="key_url";
    private WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout layout = new RelativeLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);
        mWebView = new WebView(getContext());
        layout.addView(mWebView, params);

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String url = getArguments().getString(KEY_URL);
        if (url != null){
            mWebView.loadUrl(url);
        }else{
            LogUtil.e("请在Bundle中传入要打开的链接");
        }
    }

}

