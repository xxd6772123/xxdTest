package com.xxd.common.basic.adapter;

import androidx.annotation.LayoutRes;
import androidx.databinding.ViewDataBinding;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;

import org.jetbrains.annotations.NotNull;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc: 结合dataBinding的RecyclerView Adapter    https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class BaseDBRVAdapter <Data,DB extends ViewDataBinding> extends BaseQuickAdapter<Data, BaseDataBindingHolder<DB>> implements LoadMoreModule {
    private int mVariable;

    public BaseDBRVAdapter(@LayoutRes int itemId, int id) {
        super(itemId);
        mVariable = id;
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<DB> dbBaseDataBindingHolder, Data data) {
        DB binding = dbBaseDataBindingHolder.getDataBinding();
        if(binding!=null){
            binding.setVariable(mVariable,data);
            binding.executePendingBindings();
        }
    }

    /**
     * 获取实际的数据数量
     *
     * @return
     */
    public int getRealDataCount() {
        try {
            return getDefItemCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
