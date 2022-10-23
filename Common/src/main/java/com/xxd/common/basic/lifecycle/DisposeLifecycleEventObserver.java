package com.xxd.common.basic.lifecycle;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.Disposable;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:专门用于在生命周期拥有这生命周期结束时取消相关事件处理的观察者类
 */
public class DisposeLifecycleEventObserver implements LifecycleEventObserver {

    private static final String TAG = DisposeLifecycleEventObserver.class.getSimpleName();

    private WeakReference<Disposable> disposableWeakReference;
    private WeakReference<io.reactivex.rxjava3.disposables.Disposable> disposableWeakReference3;

    public DisposeLifecycleEventObserver(Disposable disposable) {
        disposableWeakReference = new WeakReference<>(disposable);
    }

    public DisposeLifecycleEventObserver(io.reactivex.rxjava3.disposables.Disposable disposable) {
        disposableWeakReference3 = new WeakReference<>(disposable);
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        switch (event) {
            case ON_DESTROY:
                if (disposableWeakReference != null) {
                    Disposable disposable = disposableWeakReference.get();
                    if (disposable != null) {
                        disposable.dispose();
                        Log.d(TAG, "dispose");
                    }

                } else if (disposableWeakReference3 != null) {
                    io.reactivex.rxjava3.disposables.Disposable disposable1 = disposableWeakReference3.get();
                    if (disposable1 != null) {
                        disposable1.dispose();
                        Log.d(TAG, "dispose1");
                    }
                }

                break;
        }
    }
}
