package com.zhengsr.zimglib.util;

import android.os.Looper;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe:
 */
public class ZUtils {


    /**
     * 检查某个属性是否为空
     */
    public static <T> T checkNull(T arg,String msg){
        if (arg == null) {
            throw new  NullPointerException(msg);
        }
        return arg;
    }

    /**
     * Returns {@code true} if called on the main thread, {@code false} otherwise.
     */
    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * Returns {@code true} if called on the main thread, {@code false} otherwise.
     */
    public static boolean isOnBackgroundThread() {
        return !isOnMainThread();
    }
}
