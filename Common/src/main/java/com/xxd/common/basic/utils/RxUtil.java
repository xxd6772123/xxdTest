package com.xxd.common.basic.utils;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LifecycleOwner;

import com.xxd.common.basic.inf.IRetCallback;
import com.xxd.common.basic.lifecycle.DisposeLifecycleEventObserver;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:为方便使用，减少模板代码，对RxJava进一步封装的工具类
 */

public class RxUtil {

    public interface Callback<T> {

        void doBackground(ObservableEmitter<T> emitter);

        void accept(T obj);
    }

    public static <T> void create(@NonNull Callback<T> callback) {
        create(null, callback);
    }

    public static <T> void create(LifecycleOwner lifecycleOwner, @NonNull Callback<T> callback) {
        Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> emitter) {
                callback.doBackground(emitter);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        if (lifecycleOwner == null || lifecycleOwner.getLifecycle() == null) {
                            return;
                        }
                        lifecycleOwner.getLifecycle().addObserver(new DisposeLifecycleEventObserver(d));
                    }

                    @Override
                    public void onNext(@NonNull T t) {
                        callback.accept(t);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callback.accept(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 动作回调接口
     *
     * @param <T>
     */
    public interface IActionCallback<T> {

        /**
         * 在子线程上执行的动作
         *
         * @return
         */
        @WorkerThread
        T doAction();
    }

    public static <T> void get(IRetCallback<T> retCallback, IActionCallback<T> executeCallback) {
        create(new RxUtil.Callback<T>() {
            @Override
            public void doBackground(ObservableEmitter<T> emitter) {
                emitter.onNext(executeCallback.doAction());
            }

            @Override
            public void accept(T ret) {
                if (retCallback != null) {
                    retCallback.onResult(ret);
                }
            }
        });
    }
}
