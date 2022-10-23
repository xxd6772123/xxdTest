package com.xxd.common.basic.media.bean;

import com.xxd.common.basic.media.MediaInfo;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:视频素材实体类
 */
public class VideoBean extends MediaInfo {

    //视频画面宽度
    private int width;
    //视频画面高度
    private int height;
    //视频时长
    private long duration;

    public VideoBean() {
        super();
    }

    public VideoBean(String path, String name, long size, long dateModified, String uri, String mimeType,
                     int width, int height, long duration) {
        super(path, name, size, dateModified, uri, mimeType);
        this.width = width;
        this.height = height;
        this.duration = duration;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
