package com.example.app2;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Schedulers.immediate()：直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
 * Schedulers.newThread()：总是启用新线程，并在新线程执行操作。
 * Schedulers.io()： I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。
 * Schedulers.computation()：计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
 * AndroidSchedulers.mainThread()：它指定的操作将在 Android 主线程运行。
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        method11();
    }

    /**
     * debounce(1, TimeUnit.SECONDS) 语法 去抖动
     */
    private void method11() {
        Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    Log.i(TAG, "call: 取消订阅");
                } else {
                    Log.i(TAG, "call: 没有取消订阅");
                }
               // subscriber.onNext("ypk 1");
                try {
                    subscriber.onNext("ypk 1");
                    Thread.sleep(200);
                    subscriber.onNext("ypk 2");
                    Thread.sleep(200);
                    subscriber.onNext("ypk 3");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }).subscribeOn(Schedulers.io())
                .debounce(1, TimeUnit.SECONDS)//去抖动,发射间隔小于1s,都不会回调到call方法内
                .observeOn(AndroidSchedulers.mainThread())// 指定 Subscriber 的回调发生在主线程
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String result) {
                        Log.i(TAG, "call: result=" + result);
                    }
                });


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
                //.delay(3, TimeUnit.SECONDS)
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
     * 创建操作符 form 的用法
     * <p>
     */
    private void method10() {


        List<String> mList = new ArrayList<>();
        mList.add("1");
        mList.add("2");
        mList.add("3");


        Observable.from(mList)
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
                        Log.i(TAG, "onNext: s=" + s);
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
     * 变换操作符 map  的用法
     */
    private void method06() {
        final String Host = "http://blog.csdn.net/";
        Observable.just("itachi85")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return Host + s;
                    }
                }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "method06 call: s=" + s);
            }
        });


    }


    /**
     * 变换操作符 flatMap cast  的用法
     */
    private void method07() {
        final String Host = "http://blog.csdn.net/";

        List<String> mList = new ArrayList<>();
        mList.add("itachi85");
        mList.add("itachi86");
        mList.add("itachi87");


        for (String s : mList) {
            String s1 = Host + s;
            Log.i(TAG, "method06 call: s1=" + s1);
        }


        //flatMap 的合并允许交叉，也就是说可能会交错的发送事件，最终结果的顺序可能并不是原始Observable发送时的顺序。
        Observable.from(mList)
                .flatMap(new Func1<String, Observable<?>>() {
                    @Override
                    public Observable<?> call(String s) {
                        return Observable.just(Host + s);
                    }
                }).cast(String.class).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "method06 call: s=" + s);
            }
        });


    }

    /**
     * 辅助操作符
     * <p>
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


    /**
     * 辅助操作符 delay  的用法
     * <p>
     * delay 操作符让原始 Observable 在发射每项数据之前都暂停一段指定的时间段
     */
    private void method08() {
        Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                long l = System.currentTimeMillis() / 1000;
                subscriber.onNext(l);
            }
        }).delay(3, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Log.i(TAG, "call: delay:" + (System.currentTimeMillis() / 1000 - aLong));
            }
        });


    }


}
