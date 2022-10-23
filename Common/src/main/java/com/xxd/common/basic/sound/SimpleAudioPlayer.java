package com.xxd.common.basic.sound;

import android.media.MediaPlayer;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:简易音频播放器
 */
@Keep
public class SimpleAudioPlayer {

    protected MediaPlayer mMediaPlayer;

    /**
     * 播放
     */
    public void play(@NonNull ISourceLoader sourceLoader) {
        stop();

        if (mMediaPlayer == null) {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mMediaPlayer = mediaPlayer;
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (listener != null) {
                        listener.onPlayStart();
                    }
                    mp.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (listener != null) {
                        listener.onPlayEnd();
                    }
                }
            });
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    if (listener != null) {
                        listener.onPlayErr();
                    }
                    return true;
                }
            });
        }
        sourceLoader.onLoadSource(mMediaPlayer);
        mMediaPlayer.prepareAsync();
    }

    /**
     * 停止播放
     */
    public void stop() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (mMediaPlayer != null) {
            stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    protected IListener listener;

    public void setListener(IListener listener) {
        this.listener = listener;
    }

    public interface IListener {

        void onPlayStart();

        void onPlayEnd();

        void onPlayErr();
    }

    /**
     * 播放资源加载接口
     */
    public interface ISourceLoader {

        /**
         * 在实现该方法时对MediaPlayer实现待播放资源的设置
         *
         * @param mediaPlayer
         */
        void onLoadSource(@NonNull MediaPlayer mediaPlayer);

    }

}
