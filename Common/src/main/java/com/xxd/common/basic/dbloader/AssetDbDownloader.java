package com.xxd.common.basic.dbloader;

import android.content.Context;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.Utils;
import com.xxd.common.basic.utils.RxUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:
 */
public abstract class AssetDbDownloader implements IDbDownloader {

    @Override
    public void downloadDbTo(String dbPath, IDownloadCallback callback) {
        RxUtil.create(new RxUtil.Callback<Boolean>() {
            @Override
            public void doBackground(io.reactivex.ObservableEmitter<Boolean> emitter) {

                Context context = Utils.getApp();
                boolean ret = false;
                try {
                    InputStream is = context.getAssets().open(assetFileName());
                    ret = FileIOUtils.writeFileFromIS(dbPath, is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                emitter.onNext(ret);
            }

            @Override
            public void accept(Boolean ret) {
                if (callback != null) {
                    callback.onResult(ret);
                }
            }
        });
    }

    /**
     * 待下载文件在asset目录下的路径名称
     *
     * @return
     */
    protected abstract String assetFileName();
}
