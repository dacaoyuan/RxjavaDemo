package com.example.rxjavaandrxandroiddamo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "SecondActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        method5();

    }

    /**
     * 创建操作符 interval[ɪntəv(ə)l](间隔) 的用法
     * <p>
     * 每间隔3秒就会调用call方法
     */
    private void method4() {
        Observable.interval(3, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.i(TAG, "accept: aLong=" + aLong);
                    }
                });


    }

    /**
     * 创建操作符 form 的用法
     * <p>
     */
    private void method5() {
        List<String> mList = new ArrayList<>();
        mList.add("1q");
        mList.add("2w");
        mList.add("3e");


        Observable.fromIterable(mList)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String strings) throws Exception {
                        Log.i(TAG, "accept: strings=" + strings);
                    }
                });

    }


    /**
     * subscribeOn ：用于指定Observable自身在哪个线程中运行
     * observeOn :用于指定Observer 所运行的线性，也就是发射出来数据在哪个线程中使用
     */
    private void method3() {
        Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                Log.i(TAG, "subscribe: ");
                emitter.onNext("helloWold");
                //emitter.onError(new Exception("发生错误了"));
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        Log.i(TAG, "onNext: s=" + s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i(TAG, "onError: e=" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                    }
                });


    }

    //简单的使用
    private void method2() {

        Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("helloWold");
                //emitter.onError(new Exception("发生错误了"));
                emitter.onComplete();
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String s) {
                Log.i(TAG, "onNext: s=" + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.i(TAG, "onError: e=" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }
        });


        //更简单的使用
       /* Observable.just("ypk1", "ypk2")
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });*/


    }

    //基本使用
    private void method1() {

        Observable observable = Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("helloWold");
                emitter.onError(new Exception("发生错误了"));
                emitter.onComplete();
            }
        });


        Observer observer = new Observer() {


            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Object o) {
                System.out.println("SecondActivity.onNext o=" + o);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("SecondActivity.onError e=" + e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("SecondActivity.onComplete");
            }
        };


        Subscriber subscriber = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(String o) {
                System.out.println("SecondActivity.onNext o=" + o);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {
                System.out.println("SecondActivity.onComplete");
            }
        };

        //observable.subscribe((Observer) subscriber);
        observable.subscribe(observer);


    }
}
