package com.zhengsr.zimglib.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Looper;

import java.io.Closeable;
import java.io.IOException;

import static android.content.pm.ApplicationInfo.FLAG_LARGE_HEAP;

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

    public static void close(Closeable... closeables)  {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int calculateMemoryCacheSize(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //找到是否为 largeheap
        boolean largeHeap = (context.getApplicationInfo().flags & FLAG_LARGE_HEAP) != 0;
        int memoryClass = largeHeap ? am.getLargeMemoryClass() : am.getMemoryClass();
        // Target ~15% of the available heap.
        return (int) (1024L * 1024L * memoryClass / 7);
    }
}
