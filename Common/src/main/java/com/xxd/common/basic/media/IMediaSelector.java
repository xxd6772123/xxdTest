package com.xxd.common.basic.media;

import android.app.Activity;

import java.util.List;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:媒体选择器接口
 */
public interface IMediaSelector {

    enum Type {
        ALL,    //显示全部
        VIDEO,  //只显示视频
        IMAGE   //只显示图片
    }

    /**
     * 媒体选择结果回调接口
     */
    interface IRetCallback {

        /**
         * 媒体选择结果回调方法
         * @param mediaInfoList
         */
        void onResult(List<MediaInfo> mediaInfoList);
    }

    /**
     * 选择媒体素材
     * @param activity 需要选择媒体素材的界面
     * @param type 显示类型
     * @param minCount 最小媒体数量
     * @param maxCount 最大媒体数量
     * @param copyPrivate 选择媒体是否先将媒体素材拷贝到应用专属目录下
     * @param callback 选择媒体结果回调
     */
    void onSelectMedia(Activity activity, Type type, int minCount, int maxCount,
                       boolean copyPrivate, IRetCallback callback);

}
