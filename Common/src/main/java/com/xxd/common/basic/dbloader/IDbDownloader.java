package com.xxd.common.basic.dbloader;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:数据库下载器接口
 */
public interface IDbDownloader {

    /**
     * 下载回调接口
     */
    interface IDownloadCallback {

        /**
         * 下载结果回调
         *
         * @param success true：下载成功
         */
        void onResult(boolean success);

    }

    /**
     * 将数据库下载到对应的路径下
     *
     * @param dbPath   存储数据库文件的路径
     * @param callback 下载结果回调
     */
    void downloadDbTo(final String dbPath, IDownloadCallback callback);

}
