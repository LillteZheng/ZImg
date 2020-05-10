package com.zhengsr.zimglib.callback;

import android.graphics.Bitmap;

public interface ICache {
    void put(String key, Bitmap bitmap);
    Bitmap get(String key);
    void clear();
    void removeByKey(String key);
}
