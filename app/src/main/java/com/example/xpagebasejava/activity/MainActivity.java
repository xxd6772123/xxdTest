package com.example.xpagebasejava.activity;

import android.view.View;

import androidx.annotation.NonNull;

import com.example.xpagebasejava.R;
import com.example.xpagebasejava.common.BaseTabFragmentHomeActivity;
import com.example.xpagebasejava.databinding.ActivityMainBinding;
import com.example.xpagebasejava.fragment.FeatureFragment;
import com.example.xpagebasejava.fragment.HomeFragment;
import com.example.xpagebasejava.fragment.ToolFragment;
import com.example.xxd.XxdTest;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseTabFragmentHomeActivity<ActivityMainBinding> {
    @NonNull
    @Override
    protected List<FragmentViewBinder> getFragmentViewBinders() {
        List<FragmentViewBinder> binders = new ArrayList<>();
        binders.add(new FragmentViewBinder(HomeFragment.class, R.id.tvHome));
        binders.add(new FragmentViewBinder(ToolFragment.class, R.id.tvTool));
        binders.add(new FragmentViewBinder(FeatureFragment.class, R.id.tvFeature));
        return binders;
    }

    @Override
    protected void initView() {
        super.initView();
        int calculate = new XxdTest().calculation(512);
    }

    @Override
    protected void onFragmentViewClick(View view) {
        clearSelect();
        switch (view.getId()) {
            case R.id.tvHome:
                mDataBinding.tvHome.setAlpha(1);
                break;
            case R.id.tvTool:
                mDataBinding.tvTool.setAlpha(1);
                break;
            case R.id.tvFeature:
                mDataBinding.tvFeature.setAlpha(1);
                break;
            default:
                break;
        }

    }

    private void clearSelect() {
        mDataBinding.tvHome.setAlpha((float) 0.7);
        mDataBinding.tvTool.setAlpha((float) 0.7);
        mDataBinding.tvFeature.setAlpha((float) 0.7);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.flFragment;
    }

    @Override
    protected int onCreate() {
        return R.layout.activity_main;
    }

    @Override
    public void onClick(View v) {

    }
}