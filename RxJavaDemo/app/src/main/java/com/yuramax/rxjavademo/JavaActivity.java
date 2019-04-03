package com.yuramax.rxjavademo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.yuramax.rxjavademo.annotations.BindView;
import com.yuramax.rxjavademo.annotations.MaxBind;
import com.yuramax.rxjavademo.annotations.initContentView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：weijun
 * 日期：2019/4/3
 * 作用：
 */

@initContentView(R.layout.activity_java)
public class JavaActivity extends AppCompatActivity {

    private static final String tag = JavaActivity.class.getSimpleName();

    @BindView(R.id.btnHW)
    private Button btnHW;

    private Disposable disposable;
    private Observable observable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MaxBind.bind(this);

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("链式111");
                emitter.onNext("链式222");
                emitter.onNext("链式333");
                emitter.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribeOn(Schedulers.io())//执行在io线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        Log.e(tag,"Observer --> onSubscribe");
                    }

                    @Override
                    public void onNext(String s) {
                        if (s.equals(tag + "444")){
                            disposable.dispose();
                            return;
                        }
                        Log.e(tag,"Observer --> onNext：" + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(tag,"Observer --> onError：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e(tag,"Observer --> onComplete");
                    }
                });



//        initListener();
//        testObserver();
    }

    private void initListener() {
        //创建被观察者
        observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                emitter.onNext(tag + "111");
                emitter.onNext(tag + "222");
                emitter.onNext(tag + "333");
                emitter.onComplete();
            }
        });
    }

    private void testSubscriber() {
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }

        };
    }

    private void testObserver() {
        //创建观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                Log.e(tag,"Observer --> onSubscribe");
            }

            @Override
            public void onNext(String s) {
                if (s.equals(tag + "444")){
                    disposable.dispose();
                    return;
                }
                Log.e(tag,"Observer --> onNext：" + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(tag,"Observer --> onError：" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(tag,"Observer --> onComplete");
            }
        };
        //建立订阅关系
        observable.subscribe(observer);
    }
}
