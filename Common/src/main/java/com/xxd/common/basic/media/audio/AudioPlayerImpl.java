package com.xxd.common.basic.media.audio;

import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:音频播放器接口实现类（带自动获取刷新当前播放时间）
 */
public class AudioPlayerImpl implements IAudioPlayer {

    private static final int DELAY_TIME = 500;
    private IListener mListener;
    private Handler mHandler;
    private MediaPlayer mMediaPlayer;
    private int mAudioDuration = -1;

    private Runnable mTaskUpdateTime = new Runnable() {
        @Override
        public void run() {
            if (mMediaPlayer != null) {
                int curPlayTime = mMediaPlayer.getCurrentPosition();
                if (mListener != null) {
                    if (mAudioDuration < 0) {
                        mAudioDuration = mMediaPlayer.getDuration();
                    }
                    mListener.onUpdatePlayTime(curPlayTime, mAudioDuration);
                }
            }
            mHandler.postDelayed(this, DELAY_TIME);
        }
    };

    public AudioPlayerImpl() {
    }

    @Override
    public void play(String audioPath) {
        stop();

        mAudioDuration = -1;
        MediaPlayer mediaPlayer = new MediaPlayer();
        mMediaPlayer = mediaPlayer;
        mediaPlayer.setOnPreparedListener(mp -> {
            mp.start();
            onPlayOrNot(true);
        });
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.seekTo(0);
            onPlayOrNot(false);
        });
        try {
            mediaPlayer.setDataSource(audioPath);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setListener(IListener listener) {
        mListener = listener;
    }

    @Override
    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            try {
                return mMediaPlayer.isPlaying();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.pause();
            } catch (Exception e) {
                //e.printStackTrace();
            }
            onPlayOrNot(false);
        }
    }

    @Override
    public void resume() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            onPlayOrNot(true);
        }
    }

    @Override
    public void stop() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            } catch (Exception e) {
                //e.printStackTrace();
            }
            mMediaPlayer = null;
            onPlayOrNot(false);
        }
    }

    @Override
    public void seekTo(int time) {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.seekTo(time);
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }

    @Override
    public void setSpeed(float speed) {
        if (mMediaPlayer != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    PlaybackParams params = mMediaPlayer.getPlaybackParams();
                    params.setSpeed(speed);
                    mMediaPlayer.setPlaybackParams(params);
                    //根据官方接口描述，当改变速度时，将会从之前的暂停状态变为播放状态，所以下面做了如下判断处理
                    if (mMediaPlayer.isPlaying()) {
                        onPlayOrNot(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void setPitch(float pitch) {
        if (mMediaPlayer != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    PlaybackParams params = mMediaPlayer.getPlaybackParams();
                    params.setPitch(pitch);
                    mMediaPlayer.setPlaybackParams(params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void onPlayOrNot(boolean isPlaying) {
        if (mListener != null) {
            mListener.onPlayChange(isPlaying);
        }

        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        if (isPlaying) {
            mHandler.removeCallbacks(mTaskUpdateTime);
            mHandler.post(mTaskUpdateTime);
        } else {
            mHandler.removeCallbacks(mTaskUpdateTime);
        }
    }
}
