package com.xxd.common.basic.sound;

import android.media.MediaPlayer;
import android.text.TextUtils;

import androidx.annotation.Keep;

import java.io.IOException;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:发音器
 */
@Keep
public class DictSpeaker {

    private static DictSpeaker sInstance;
    private MediaPlayer mMediaPlayer;
    private String mPlayingText;

    private DictSpeaker() {
    }

    public synchronized static DictSpeaker getInstance() {
        if (sInstance == null) {
            sInstance = new DictSpeaker();
        }
        return sInstance;
    }

    /**
     * 英式发音
     *
     * @param text
     */
    public void playByEn(String text) {
        play(text, 1);
    }

    /**
     * 美式发音
     *
     * @param text
     */
    public void playByAm(String text) {
        play(text, 2);
    }

    private void play(String text, int type) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (mPlayingText != null && mPlayingText.equals(text)) {
            return;
        }
        mPlayingText = text;
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (listener != null) {
                        listener.onPlayStart();
                    }
                    mp.start();
                }
            });
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mPlayingText = null;
                    if (listener != null) {
                        listener.onPlayErr();
                    }
                    return true;
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mPlayingText = null;
                    if (listener != null) {
                        listener.onPlayComplete();
                    }
                }
            });
        }
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(generateUrl(text, type));
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            mPlayingText = null;
        }
    }

    /**
     * 停止播放并释放相关资源
     */
    public void stop() {
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
                mPlayingText = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateUrl(String text, int type) {
        //该链接还可处理其它各种语言的发音，很强大。只要把其它语言的text传进来即可
        return "http://dict.youdao.com/dictvoice?audio=" + text + "&type=" + type;
    }

    private IListener listener;

    public void setListener(IListener listener) {
        this.listener = listener;
    }

    public interface IListener {

        void onPlayStart();

        void onPlayComplete();

        void onPlayErr();
    }

}
