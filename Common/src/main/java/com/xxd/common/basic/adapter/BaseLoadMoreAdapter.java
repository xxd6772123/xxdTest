package com.xxd.common.basic.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.blankj.utilcode.util.ThreadUtils;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.LoadMoreModule;

import java.util.List;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:加载更多数据适配器基类
 */
public abstract class BaseLoadMoreAdapter<Data, DB extends ViewDataBinding> extends BaseDBRVAdapter<Data, DB> implements LoadMoreModule,
        OnLoadMoreListener {

    private final String TAG;
    //默认的每页加载数量
    private static final int DEF_PAGE_SIZE = 10;
    //每页加载的数量
    protected final int PAGE_SIZE;
    //当前加载页
    protected int mCurPage = 1;

    public BaseLoadMoreAdapter(int itemId, int id) {
        super(itemId, id);
        TAG = getClass().getSimpleName();

        int pageSize = getPageSize();
        PAGE_SIZE = pageSize >= 1 ? pageSize : DEF_PAGE_SIZE;
        mCurPage = getStartPage();

        //下拉加载
        getLoadMoreModule().setPreLoadNumber(getPreLoadNumber());
        getLoadMoreModule().setOnLoadMoreListener(this);
    }

    /**
     * 起始页（即第一页，有的接口第一页为是0 有的第一页是1），子类按需去重写提供对应的第一页
     *
     * @return
     */
    protected int getStartPage() {
        return 1;
    }

    /**
     * 子类可覆盖重写提供每页要加载的数量
     *
     * @return
     */
    protected int getPageSize() {
        return DEF_PAGE_SIZE;
    }

    /**
     * 获取预加载数量，子类可重写该方法，设置对应的预加载数量
     *
     * @return
     */
    protected int getPreLoadNumber() {
        //默认预加载数量与加载一页的数量相同
        return PAGE_SIZE;
    }

    /**
     * 请求第一页数据
     *
     * @param callback
     */
    public void reqFirstPageData(@Nullable ILoadDataCallback<Data> callback) {
        Log.i(TAG, "reqFirstPageData");
        mCurPage = getStartPage();
        reqLoadData(mCurPage, PAGE_SIZE, new ILoadDataCallback<Data>() {
            @Override
            public void onLoadedData(boolean success, @Nullable List<Data> dataList) {
                if (callback != null) {
                    callback.onLoadedData(success, dataList);
                }

                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onLoadedFirstPageData(success, dataList);
                    }
                });
            }
        });
    }

    @Override
    public void onLoadMore() {
        mCurPage++;
        Log.i(TAG, "onLoadMore: is load " + mCurPage + " page");
        reqLoadData(mCurPage, PAGE_SIZE, new ILoadDataCallback<Data>() {
            @Override
            public void onLoadedData(boolean success, @Nullable List<Data> dataList) {
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onLoadedMoreData(success, dataList);
                    }
                });
            }
        });
    }

    /**
     * 刷新数据处理方法
     *
     * @param success
     * @param dataList
     */
    private void onLoadedFirstPageData(boolean success, @Nullable List<Data> dataList) {
        Log.i(TAG, "onLoadedFirstPageData: load " + (success ? "success" : "failure") +
                ", data size = " + (dataList == null ? 0 : dataList.size()));
        if (!success) {
            return;
        }
        if (dataList == null || dataList.size() == 0) {
            return;
        }
        setList(dataList);
    }

    /**
     * 加载更多数据处理方法
     *
     * @param success
     * @param dataList
     */
    private void onLoadedMoreData(boolean success, @Nullable List<Data> dataList) {
        Log.i(TAG, "onLoadedMoreData: load " + (success ? "success" : "failure") +
                ", data size = " + (dataList == null ? 0 : dataList.size()));
        if (!success) {
            //接口获取数据失败
            mCurPage--;
            getLoadMoreModule().loadMoreFail();
            return;
        }

        if (dataList == null || dataList.size() < PAGE_SIZE) {
            //获取了最后一页数据，没有更多数据了
            if (dataList != null) {
                addData(dataList);
            }
            getLoadMoreModule().loadMoreEnd();
            return;
        }

        //获取到一页数据
        addData(dataList);
        getLoadMoreModule().loadMoreComplete();

    }

    /**
     * 请求加载数据，子类来实现该方法去加载想要加载的数据
     *
     * @param page
     * @param pageSize
     * @param callback
     */
    protected abstract void reqLoadData(int page, int pageSize, @NonNull ILoadDataCallback<Data> callback);

    /**
     * 加载到一页数据回调接口
     *
     * @param <Data>
     */
    public interface ILoadDataCallback<Data> {

        /**
         * 加载到一页数据回调方法
         *
         * @param success  加载数据请求成功
         * @param dataList 加载到的数据列表
         */
        void onLoadedData(boolean success, @Nullable List<Data> dataList);

    }
}
