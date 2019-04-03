package com.yuramax.rxjavademo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.yuramax.rxjavademo.annotations.MaxBind
import com.yuramax.rxjavademo.annotations.initContentView
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import android.graphics.drawable.Drawable
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe



@initContentView(R.layout.activity_kotlin)
class KotlinActivity : AppCompatActivity() {

    private val tag = KotlinActivity::class.java.simpleName

    private var disposable: Disposable? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MaxBind.bind(this)

//        testObserver()
//        ls1()
        timerDemo()

//        loadImg()
    }



    private fun timerDemo() {
        Observable.create(object :ObservableOnSubscribe<Int>{
            override fun subscribe(emitter: ObservableEmitter<Int>) {
                emitter.onNext(1)
                Thread.sleep(1000)
                emitter.onNext(2)
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Consumer<Int> {
                    override fun accept(t: Int) {
                        Log.e(tag,".....$t")
                    }
                },object :Consumer<Throwable>{
                    override fun accept(t: Throwable) {
                        Log.e(tag,".....${t.message}")
                    }
                },object :Action{
                    override fun run() {
                        Log.e(tag,".....run")
                    }
                })
    }

    private fun ls1() {
        Observable.create(object : ObservableOnSubscribe<String>{
            override fun subscribe(emitter: ObservableEmitter<String>) {
                emitter.onNext("链式111")
                emitter.onNext("链式222")
                emitter.onNext("链式333")
                emitter.onComplete()
            }
        })
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribeOn(Schedulers.io())//执行在io线程
                .subscribe(object :Observer<String>{
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                        Log.e(tag,"Observer --> onSubscribe")
                    }

                    override fun onNext(s: String) {
                        if (s == tag + "444") {
                            disposable!!.dispose()
                            return
                        }
                        Log.e(tag, "Observer --> onNext：$s")
                    }

                    override fun onError(e: Throwable) {
                        Log.e(tag, "Observer --> onError：${e.message}")
                    }

                    override fun onComplete() {
                        Log.e(tag,"Observer --> onComplete")
                    }
                })
    }

    //创建被观察者
    var observable = Observable.create(object : ObservableOnSubscribe<String> {
        @Throws(Exception::class)
        override fun subscribe(emitter: ObservableEmitter<String>) {
            emitter.onNext(tag + "111")
            emitter.onNext(tag + "222")
            emitter.onNext(tag + "333")
            emitter.onComplete()
        }
    })

    private fun testSubscriber() {
        val subscriber = object : Subscriber<String> {
            override fun onSubscribe(s: Subscription) {
                Log.e(tag,"Subscriber --> onSubscribe")
            }

            override fun onNext(s: String) {
                Log.e(tag,"Subscriber --> onNext")
            }

            override fun onError(t: Throwable) {
                Log.e(tag,"Subscriber --> onError")
            }

            override fun onComplete() {
                Log.e(tag,"Subscriber --> onComplete")
            }

        }
    }

    private fun testObserver() {
        val observer = object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
                disposable = d
                Log.e(tag,"Observer --> onSubscribe")
            }

            override fun onNext(s: String) {
                if (s == tag + "444") {
                    disposable!!.dispose()
                    return
                }
                Log.e(tag, "Observer --> onNext：$s")
            }

            override fun onError(e: Throwable) {
                Log.e(tag, "Observer --> onError：${e.message}")
            }

            override fun onComplete() {
                Log.e(tag,"Observer --> onComplete")
            }
        }
        //建立订阅关系
        observable.subscribe(observer)
    }

    //    private fun loadImg() {
//        Observable.create(ObservableOnSubscribe<Drawable> { emitter ->
//            for (i in 0 until drawableRes.length) {
//                val drawable = theme.getDrawable(drawableRes[i])
//                //第6个图片延时3秒后架子
//                if (i == 5) {
//                    Thread.sleep(3000)
//                }
//                //复制第7张图片到sd卡
//                if (i == 6) {
//                    val bitmap = (drawable as BitmapDrawable).bitmap
//                    saveBitmap(bitmap, "test.png", Bitmap.CompressFormat.PNG)
//                }
//                //上传到网络
//                if (i == 7) {
//                    updateImg(drawable)
//                }
//                emitter.onNext(drawable)
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    //回调后在UI界面上展示出来
//                }
//    }
}
