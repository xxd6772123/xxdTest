package com.xxd.common.basic.media.bean;

import com.xxd.common.basic.media.MediaInfo;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:图片素材实体类
 */
public class ImgBean extends MediaInfo {

    //图片宽度
    private int width;
    //图片高度
    private int height;

    public ImgBean() {
        super();
    }

    public ImgBean(String path, String name, long size, long dateModified, String uri, String mimeType,
                   int width, int height) {
        super(path, name, size, dateModified, uri, mimeType);
        this.width = width;
        this.height = height;
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
}
