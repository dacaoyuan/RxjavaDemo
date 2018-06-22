package com.example.app2;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        method05();
    }


    /**
     * 基本使用
     */
    private void method01() {
        //（1）创建被观察者
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onStart();
                subscriber.onNext("hello world");

                //注意一点： onError 和 onCompleted 的回调，如果其中一个被回调了，另一个就不会回调了
                //subscriber.onError(new Exception("发生错误了"));
                subscriber.onCompleted();

            }
        });


        /*Observer observer=new Observer() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        };*/

        //（2）创建观察者  Subscriber 是在Observer 的基础上进行了扩展
        Subscriber subscriber = new Subscriber() {
            @Override
            public void onStart() {
                Log.i(TAG, "onStart: 可做些准备工作");
            }

            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                Log.i(TAG, "onNext: o=" + o);
            }
        };

        //（3）被观察者 订阅 观察者
        observable.subscribe(subscriber);


    }


    /**
     * 简介的写法介绍
     */
    private void method02() {
        //（1）创建被观察者
       /* Observable observable = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onStart();
                subscriber.onNext("hello world");
                subscriber.onNext("hello world2");

                subscriber.onCompleted();

            }
        });*/

        Observable observable = Observable.just("hello world1", "hello world2");
        // String[] words = {"hello world11", "hello world2"};
        //Observable observable = Observable.just(words);


        //（2）创建观察者  Subscriber 是在Observer 的基础上进行了扩展
        Subscriber subscriber = new Subscriber() {
            @Override
            public void onStart() {
                Log.i(TAG, "onStart: 可做些准备工作");
            }

            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                Log.i(TAG, "onNext: o=" + o);
                // String[] aa = (String[]) o;
                // Log.i(TAG, "onNext: o=" + aa[0]);
            }
        };

        //（3）被观察者 订阅 观察者
        observable.subscribe(subscriber);


    }

    /**
     * 链式调用，更简介的写法
     */
    private void method022() {

        Observable.just("hello world1", "hello world2")
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: ");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.i(TAG, "onNext: ");
                    }
                });


    }


    /**
     * 不完整定义的回调
     */
    private void method03() {
        Observable observable = Observable.just("hello world1", "hello world2");


        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "onNextAction call: s=" + s);
            }
        };

        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.i(TAG, "onErrorAction call: e=" + throwable.getMessage());
            }
        };

        Action0 onCompletedAction = new Action0() {
            @Override
            public void call() {
                Log.i(TAG, "onCompletedAction call: ");
            }
        };

        //（3）被观察者 订阅 观察者
        //observable.subscribe(onNextAction);
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);


    }


    /**
     * 创建操作符 interval[ɪntəv(ə)l](间隔) 的用法
     * <p>
     * 每间隔3秒就会调用call方法
     */
    private void method04() {
        Observable.interval(3, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.i(TAG, "interval call: along=" + aLong);
                    }
                });

    }

    /**
     * 创建操作符 range (范围) 的用法
     */
    private void method044() {
        Observable.range(0, 51)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.i(TAG, "interval call: integer=" + integer);
                    }
                });

    }


    /**
     * 创建操作符 repeat  的用法
     * <p>
     * 创建一个 3 此重复发射特定数据的Observable
     */
    private void method0444() {
        Observable.range(0, 5)
                .repeat(3)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.i(TAG, "interval call: integer=" + integer);
                    }
                });

    }


    /**
     * subscribeOn ：用于指定Observable自身在哪个线程中运行
     * observeOn :用于指定Observer 所运行的线性，也就是发射出来数据在哪个线程中使用
     */
    private void method05() {


        Observable observable1 = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.i(TAG, "call: ");
                subscriber.onStart();
                subscriber.onNext("hello world");
                subscriber.onNext("hello world2");

                subscriber.onCompleted();

            }
        });

        Subscriber subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "onNext: ");
            }
        };

       // 指定Observable自身在新的子线程中运行
        Observable observable2 = observable1.subscribeOn(Schedulers.newThread());

        // 指定 Observer 在主线程中运行
        Observable observable3 = observable2.observeOn(AndroidSchedulers.mainThread());

        observable3.subscribe(subscriber);
        
        
        
         /*// 上面的代码改成链式调用就是下面的代码
        Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onStart();
                subscriber.onNext("hello world");
                subscriber.onNext("hello world2");

                subscriber.onCompleted();

            }})
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: ");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.i(TAG, "onNext: ");
                    }
                });

    }*/


    }


}
