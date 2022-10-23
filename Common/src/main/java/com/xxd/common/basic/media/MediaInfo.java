package com.xxd.common.basic.media;

import android.text.TextUtils;

import com.xxd.common.basic.bean.SelBean;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:媒体信息实体类
 */
public class MediaInfo extends SelBean {

    //媒体素材所在路径（即文件路径）
    private String path;
    //文件名称
    private String name;
    //文件大小
    private long size;
    //修改日期
    private long dateModified;
    //文件的uri
    private String uri;
    //文件所属的MimeType
    private String mimeType;

    public MediaInfo() {
    }

    public MediaInfo(String path, String name, long size, long dateModified, String uri, String mimeType) {
        this.path = path;
        this.name = name;
        this.size = size;
        this.dateModified = dateModified;
        this.uri = uri;
        this.mimeType = mimeType;

        //处理name，测试发现在360手机上获取到的这个name是为空的
        if (TextUtils.isEmpty(name) && path != null) {
            int index = path.lastIndexOf("/");
            if (index >= 0) {
                this.name = path.substring(index + 1);
            } else {
                this.name = path;
            }
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        if (!TextUtils.isEmpty(name)) {
            return name;
        }
        return "Unknown";
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDateModified() {
        return dateModified;
    }

    public void setDateModified(long dateModified) {
        this.dateModified = dateModified;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public boolean isImage() {
        return checkMimeType("image");
    }

    public boolean isVideo() {
        return checkMimeType("video");
    }

    public boolean isAudio() {
        return checkMimeType("audio");
    }

    private boolean checkMimeType(String prefix) {
        if (mimeType == null) {
            return false;
        }
        return mimeType.startsWith(prefix);
    }
}
