package com.xxd.common.basic.media;

import com.xxd.common.basic.bean.BaseBean;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:
 */
public class MediaMetadataInfo extends BaseBean {

    //码率
    private int bitrate;
    //宽
    private int width;
    //高
    private int height;
    //时长 单位：毫秒
    private int duration;

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
