package com.xxd.common.basic.sound;

import android.media.SoundPool;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.ThreadUtils;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:音效管理器基类
 */
public abstract class BaseSoundManager {

    protected static final int DEF_DELAY_TIME = 500;
    protected static final int INIT_VALUE = 0;

    protected SoundPool mSoundPool;
    //能否发声的标志
    protected boolean canPlay = true;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void initSoundPool() {
        if (mSoundPool == null) {
            SoundPool.Builder builder = null;
            builder = new SoundPool.Builder();
            mSoundPool = builder.build();
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (mSoundPool != null) {
            mSoundPool.release();
            mSoundPool = null;
        }
    }

    protected void playSound(int soundId) {
        playSound(soundId, DEF_DELAY_TIME);
    }

    protected void playSound(int soundId, long delayTime) {
        if (!canPlay) {
            return;
        }
        if (mSoundPool == null) {
            return;
        }
        ThreadUtils.runOnUiThreadDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSoundPool == null) return;
                mSoundPool.play(soundId, 1f, 1f, 1, 0, 1);
            }
        }, delayTime);
    }

    public void setCanPlay(boolean canPlay) {
        this.canPlay = canPlay;
    }

    public boolean isCanPlay() {
        return canPlay;
    }
}
