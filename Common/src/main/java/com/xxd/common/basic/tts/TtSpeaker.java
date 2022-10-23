package com.xxd.common.basic.tts;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.Utils;
import com.xxd.common.basic.utils.FileUtil;
import com.xxd.common.basic.utils.MD5Utils;

import java.io.File;
import java.io.IOException;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:tts播放器
 */
@Keep
public class TtSpeaker {

    private static final String TAG = TtSpeaker.class.getSimpleName();

    private TextToSpeech mTts;
    private String mFilePath;
    private boolean isInitOk = false;
    //是否播放tts生成的音频文件
    private boolean playSpeak = true;
    private State mState = State.IDLE;
    private MediaPlayer mMediaPlayer;

    public TtSpeaker(IInitListener initListener) {
        mTts = new TextToSpeech(Utils.getApp(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                isInitOk = true;
            } else {
                isInitOk = false;
            }
            if (initListener != null) {
                initListener.onInit(isInitOk, mTts);
            }
        });
        mTts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
            }

            @Override
            public void onDone(String utteranceId) {
                if (playSpeak) {
                    startPlay();
                } else {
                    onStateChanged(mState, State.IDLE);
                }

                if (mSpeakCallback != null) {
                    mSpeakCallback.onSpeakCreateFilePath(mFilePath);
                }
            }

            @Override
            public void onError(String utteranceId) {
                onStateChanged(mState, State.IDLE);
            }
        });
    }

    private void startPlay() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    onStateChanged(mState, State.PLAYING);
                }
            });
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    onStateChanged(mState, State.IDLE);
                    return true;
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.seekTo(0);
                    onStateChanged(mState, State.PAUSE);
                }
            });
        }
        try {
            mMediaPlayer.setDataSource(mFilePath);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPlay() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
                onStateChanged(mState, State.IDLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 该播报方法将会把text先合成为文件后，再通过MediaPlayer来播放。
     *
     * @param text
     */
    public void speak(@NonNull CharSequence text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            speak(text, true, null);
        }
    }

    /**
     * 该播报方法将会把text先合成为文件，然后根据playSpeak变量来控制是否通过MediaPlayer来播放，生成的文件将通过callback回调给调用方
     *
     * @param text
     * @param playSpeak
     * @param callback
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void speak(@NonNull CharSequence text, boolean playSpeak, ISpeakCallback callback) {
        mSpeakCallback = callback;
        this.playSpeak = playSpeak;
        if (mState == State.SYNTHESIZING) {
            //正在合成中，直接返回不处理
            if (callback != null) {
                callback.onSpeakCreateFilePath(null);
            }
            return;
        }
        if (mState == State.PLAYING || mState == State.PAUSE) {
            stopPlay();
        }

        if (mFilePath == null) {
            mFilePath = FileUtil.generateFilePathByName("tts", "tmpTtx.mp3");
        }
        String utteranceId = MD5Utils.strToMd5By16(text.toString());
        Bundle params = new Bundle();
        //params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
        File file = new File(mFilePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            onStateChanged(mState, State.SYNTHESIZING);
            mTts.synthesizeToFile(text, params, file, utteranceId);
        } catch (IOException e) {
            e.printStackTrace();
            onStateChanged(mState, State.IDLE);
            if (callback != null) {
                callback.onSpeakCreateFilePath(null);
            }
        }
    }

    /**
     * 简单播报。该播报方法不会将text合成为文件后在通过MediaPlayer来播放。
     *
     * @param text
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void simpleSpeak(@NonNull CharSequence text) {
        if (!isInitOk) {
            Log.e(TAG, "simpleSpeak: TextToSpeech have not init success.");
            return;
        }
        String utteranceId = MD5Utils.strToMd5By16(text.toString());
        mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);

    }

    public State getState() {
        return mState;
    }

    public void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            onStateChanged(mState, State.PAUSE);
        }
    }

    public void resume() {
        if (mState == State.PAUSE) {
            if (mMediaPlayer != null) {
                mMediaPlayer.start();
                onStateChanged(mState, State.PLAYING);
            }
        }
    }

    public void stop() {
        mTts.stop();
        stopPlay();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void onStateChanged(@NonNull State oldState, @NonNull State newState) {
        if (newState != oldState) {
            mState = newState;
            if (stateListener != null) {
                stateListener.onStateChanged(oldState, newState);
            }
        }
    }

    /**
     * 当前发生器的状态
     */
    @Keep
    public enum State {

        IDLE,               //空闲状态
        SYNTHESIZING,       //合成中状态
        PLAYING,            //播放中状态
        PAUSE,              //暂停状态

    }

    /**
     * 初始化监听器
     */
    public interface IInitListener {

        /**
         * TextToSpeech初始化完成时回调该方法
         *
         * @param initOk
         * @param tts
         */
        void onInit(boolean initOk, TextToSpeech tts);

    }

    private IStateListener stateListener;

    public void setStateListener(IStateListener stateListener) {
        this.stateListener = stateListener;
    }

    public interface IStateListener {

        void onStateChanged(State oldState, State newState);

    }

    private ISpeakCallback mSpeakCallback;

    public interface ISpeakCallback {

        void onSpeakCreateFilePath(@Nullable String filePath);

    }
}
