package com.yuramax.rxjavademo.annotations;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 佛祖保佑  永无BUG
 * 作者：weijun
 * 日期：2019/1/19
 * 作用：具体处理
 */

public class MaxBind {

    public static void bind(Activity activity){
        bindView(activity);
        bindLayout(activity);
    }

    private static void bindLayout(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        initContentView contentView = clazz.getAnnotation(initContentView.class);
        if (contentView != null){
            int layoutId = contentView.value();
            try {
                Method method = clazz.getMethod("setContentView", int.class);
                method.invoke(activity,layoutId);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static void bindView(Activity activity) {
        Class<?> clazz = activity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            BindView bindView = field.getAnnotation(BindView.class);
            if (bindView != null) {
                int id = bindView.value();
                try {
                    Method method = clazz.getMethod("findViewById", int.class);
                    Object invoke = method.invoke(activity, id);
                    field.setAccessible(true);
                    field.set(activity, invoke);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
