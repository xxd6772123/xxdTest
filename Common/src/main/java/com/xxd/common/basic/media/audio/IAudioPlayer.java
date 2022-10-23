package com.xxd.common.basic.media.audio;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:音频播放器接口
 */
public interface IAudioPlayer {

    /**
     * 开始播放
     *
     * @param audioPath
     */
    void play(String audioPath);

    /**
     * 设置播放器的监听器接口
     *
     * @param listener
     */
    void setListener(IListener listener);

    /**
     * 判断当前是否正在播放接口
     *
     * @return
     */
    boolean isPlaying();

    /**
     * 暂停播放
     */
    void pause();

    /**
     * 恢复播放
     */
    void resume();

    /**
     * 停止播放
     */
    void stop();

    /**
     * 设置到要播放的位置
     *
     * @param time 单位：毫秒
     */
    void seekTo(int time);

    /**
     * 设置播放速度
     *
     * @param speed
     */
    void setSpeed(float speed);

    /**
     * 设置播放音调
     *
     * @param pitch
     */
    void setPitch(float pitch);

    interface IListener {

        /**
         * 通知刷新当前的播放时间
         *
         * @param curPlayTime 当前播放到的时间 单位：毫秒
         * @param duration    当前播放音频的总时长 单位：毫秒
         */
        void onUpdatePlayTime(int curPlayTime, int duration);

        /**
         * 通知播放状态改变
         *
         * @param isPlaying true：正在播放 false：当前没有播放
         */
        void onPlayChange(boolean isPlaying);

    }
}
