package com.xxd.common.basic.retrofit;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.xxd.common.basic.lifecycle.DisposeLifecycleEventObserver;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:封装基础的Retrofit
 */
public abstract class BaseApi {

    public static String HOST = "https://service.starkos.cn";

    /**
     * 初始化Retrofit
     */
    public Retrofit initRetrofit(String baseUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
        //支持返回Call<String>
        builder.addConverterFactory(ScalarsConverterFactory.create());
        //支持直接格式化json返回Bean对象
        builder.addConverterFactory(GsonConverterFactory.create());
        //支持RxJava
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        builder.baseUrl(baseUrl);
        OkHttpClient client = setClient();
        if (client != null) {
            builder.client(client);
        } else {
            builder.client(setNoProxyClient());
        }
        return builder.build();
    }

    /**
     * 设置OkHttpClient，添加拦截器等
     *
     * @return 可以返回为null
     */
    protected abstract OkHttpClient setClient();

    /**
     * 设置OkHttpClient，无代理，防止 Fiddle 和 HttpCanary 抓包
     *
     * @return 可以返回为null
     */
    private OkHttpClient setNoProxyClient() {
        OkHttpClient.Builder okClientBuilder = new OkHttpClient.Builder();
        okClientBuilder.proxySelector(new ProxySelector() {
            @Override
            public List<Proxy> select(URI uri) {
                return Collections.singletonList(Proxy.NO_PROXY);
            }

            @Override
            public void connectFailed(URI uri, SocketAddress socketAddress, IOException e) {

            }
        });
        return okClientBuilder.build();
    }

    /**
     * 给可观察对象做订阅处理
     *
     * @param observable
     * @param callback
     * @param <T>
     */
    @Deprecated
    public static <T> void handleObservable(@androidx.annotation.NonNull Observable<T> observable,
                                            IObserverCallback callback) {
        handleObservable(null, observable, callback);
    }

    /**
     * 给可观察对象做订阅处理
     *
     * @param lifecycleOwner
     * @param observable
     * @param callback
     * @param <T>
     */
    public static <T> void handleObservable(LifecycleOwner lifecycleOwner, @androidx.annotation.NonNull Observable<T> observable,
                                            IObserverCallback callback) {
        observable.subscribeOn(Schedulers.io())
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
                        if (callback != null) {
                            callback.onResult(true, "success", t);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (callback != null) {
                            callback.onResult(false, e.getMessage(), null);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    /**
     * 观察者回调接口
     *
     * @param <T>
     */
    protected interface IObserverCallback<T> {

        void onResult(boolean success, String msg, T result);

    }
}
