package com.xxd.common.basic.media.video;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:视频格式
 */
public enum VideoFormat {

    MP4(".mp4"),
    AVI(".avi"),
    MKV(".mkv"),
    FLV(".flv"),
    THREE_GP(".3gp"),
    MOV(".mov"),
    ;

    //视频格式文件对应的后缀
    private String suffix;

    VideoFormat(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }
}
