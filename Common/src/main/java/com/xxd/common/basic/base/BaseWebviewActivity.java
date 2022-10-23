package com.xxd.common.basic.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.xxd.common.basic.R;
import com.xxd.common.basic.retrofit.BaseApi;
import com.xxd.common.basic.utils.AESUtil;
import com.xxd.common.basic.utils.DensityUtil;
import com.xxd.common.basic.utils.IntentUtil;
import com.xxd.common.basic.utils.StatusBarUtils;

import java.io.Serializable;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:链接显示工具
 */
public class BaseWebviewActivity extends AppCompatActivity implements View.OnClickListener {

    public static class TitleTvConfig implements Serializable {
        public int textSize = 18;
        public int leftRightMargin = 30;
        public int textColor = Color.BLACK;
        public int lines = 1;
        public int textAlign = TextView.TEXT_ALIGNMENT_CENTER;
        public TextUtils.TruncateAt ellipsize = TextUtils.TruncateAt.MIDDLE;
    }

    private static final String KEY_URL = "web_url";
    private static final String KEY_TITLE = "web_title";
    private static final String KEY_CONFIG = "web_title_config";
    public static String DEF_PRIVACY = "file:///android_asset/privacy.html";
    public static String DEF_TERMS = "file:///android_asset/terms.html";
    // 全局Title配置
    public static TitleTvConfig sTitleTvConfig = new TitleTvConfig();


    private WebView mWebView;
    private String mTitle;
    private TitleTvConfig mTitleTvConfig;

    /**
     * @param context
     * @param link    https://api.starkos.cn/feedback/1464718214
     */
    public static void open(Context context, String link) {
        open(context, link, "");
    }


    /**
     * 调用系统浏览器来打开网址
     *
     * @param context
     * @param link
     */
    public static void openWithSysBrowser(Context context, String link) {
        IntentUtil.openUri(context, Uri.parse(link));
    }

    /**
     * @param context
     * @param link    对应链接
     * @param title   指定title，不能传空
     */
    public static void open(Context context, String link, String title) {
        open(context, link, title, sTitleTvConfig);
    }

    public static void open(Context context, String link, String title, TitleTvConfig config) {
        Intent intent = new Intent(context, BaseWebviewActivity.class);
        intent.putExtra(BaseWebviewActivity.KEY_URL, link);
        intent.putExtra(BaseWebviewActivity.KEY_TITLE, title);
        intent.putExtra(BaseWebviewActivity.KEY_CONFIG, config);
        context.startActivity(intent);
    }

    public static String getFeedbackUrl(Context context) {
        return BaseApi.HOST + "/a/feedback/" + AESUtil.encrypt(context.getPackageName());
    }

    /**
     * 打开App的意见反馈页面
     *
     * @param context
     */
    public static void openFeedback(Context context) {
        open(context, getFeedbackUrl(context), "");
    }

    /**
     * 打开Asset目录下隐私策略
     *
     * @param context
     */
    public static void openAssetPrivacy(Context context) {
        open(context, DEF_PRIVACY, "");
    }

    /**
     * 打开Asset目录下使用条款
     *
     * @param context
     */
    public static void openAssetTerms(Context context) {
        open(context, DEF_TERMS, "");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.with(this).init();
        StatusBarUtils.setSystemStatusTextColor(true, this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        int density = (int) DensityUtil.getDensity(this);
        RelativeLayout superLayout = new RelativeLayout(this);
        superLayout.setPadding(0, DensityUtil.getStatusBarHeight(this), 0, 0);
        //标题和返回键
        RelativeLayout titleLayout = new RelativeLayout(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            titleLayout.setId(View.generateViewId());
        }
        RelativeLayout.LayoutParams titleLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (50 * density));
        superLayout.addView(titleLayout, titleLayoutParams);

        ImageView ivBack = new ImageView(this);
        ivBack.setImageResource(R.drawable.ajiantouheh);
        ivBack.setOnClickListener(this);
        RelativeLayout.LayoutParams backParams = new RelativeLayout.LayoutParams((int) (30 * density), (int) (30 * density));
        backParams.setMargins((12 * density), 0, 0, 0);
        backParams.addRule(RelativeLayout.CENTER_VERTICAL);
        titleLayout.addView(ivBack, backParams);

        mTitleTvConfig = (TitleTvConfig) getIntent().getSerializableExtra(KEY_CONFIG);
        if (null == mTitleTvConfig) {
            mTitleTvConfig = sTitleTvConfig;
        }
        mTitle = getIntent().getStringExtra(KEY_TITLE);
        final TextView tvTitle = getTitleTextView();
        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        titleParams.setMargins(mTitleTvConfig.leftRightMargin * density, 0, mTitleTvConfig.leftRightMargin * density, 0);
        titleLayout.addView(tvTitle, titleParams);

        mWebView = new WebView(this);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                } else {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (TextUtils.isEmpty(mTitle)) {
                    mTitle = view.getTitle();
                    tvTitle.setText(mTitle);
                }
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);

        RelativeLayout container = new RelativeLayout(this);
        container.setId(View.generateViewId());
        RelativeLayout.LayoutParams containerParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        containerParams.addRule(RelativeLayout.BELOW, titleLayout.getId());
        superLayout.addView(container, containerParams);


        RelativeLayout.LayoutParams webViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webViewParams.addRule(RelativeLayout.BELOW, container.getId());
        superLayout.addView(mWebView, webViewParams);
        setContentView(superLayout);
        String url = getIntent().getStringExtra(BaseWebviewActivity.KEY_URL);
        if (TextUtils.isEmpty(url)) {
            mWebView.loadData("空链接，无法打开", "text/html", "UTF-8");
        } else if (url.endsWith(".mp4")) {
            mWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
            url = String.format("<video controls controlslist=\"nodownload nofullscreen\" width=\"100%%\" height=\"100%%\" src=\"%s\"></video>", url);
            mWebView.loadData(url, "text/html", "UTF-8");
        } else {
            mWebView.loadUrl(url);
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private TextView getTitleTextView() {
        final TextView tvTitle = new TextView(this);
        tvTitle.setText(mTitle);
        tvTitle.setTextColor(mTitleTvConfig.textColor);
        tvTitle.setTextSize(mTitleTvConfig.textSize);
        tvTitle.setLines(mTitleTvConfig.lines);
        tvTitle.setEllipsize(mTitleTvConfig.ellipsize);
        tvTitle.setTextAlignment(mTitleTvConfig.textAlign);
        return tvTitle;
    }
}
