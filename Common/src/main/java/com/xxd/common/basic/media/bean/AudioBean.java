package com.xxd.common.basic.media.bean;

import com.xxd.common.basic.media.MediaInfo;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:音频素材实体类
 */
public class AudioBean extends MediaInfo {

    //音频时长
    private long duration;

    public AudioBean() {
        super();
    }

    public AudioBean(String path, String name, long size, long dateModified, String uri, String mimeType,
                     long duration) {
        super(path, name, size, dateModified, uri, mimeType);
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
