package com.xxd.common.basic.media.audio;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:音频格式
 */
public enum AudioFormat {

    MP3(".mp3"),
    WAV(".wav"),
    AAC(".aac"),
    FLAC(".flac"),
    //WMA(".wma"),          //不支持播放，所以去掉
    //AMR(".amr"),          //当前库中的ffmpeg没有包含amr的编码器，所以去掉
    M4A(".m4a");


    /**音频格式文件对应的后缀*/
    private String suffix;

    AudioFormat(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }
}
