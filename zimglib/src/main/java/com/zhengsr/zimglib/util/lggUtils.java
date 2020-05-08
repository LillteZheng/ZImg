package com.zhengsr.zimglib.util;

import android.util.Log;

public class lggUtils {
    private static final String TAG = "lggUtils";
    private static final boolean ENABLE = true;

    public static void d(String msg){
        Log.d(TAG, "zsr -> "+msg);
    }
    public static void e(String msg){
        Log.e(TAG, "zsr -> "+msg);
    }
}
