package com.zhengsr.zimglib.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.zhengsr.zimglib.callback.ICache;
import com.zhengsr.zimglib.util.ZUtils;

public class MemoryCache implements ICache {
    private LruCache<String, Bitmap> mCache;

    public MemoryCache(Context context) {
        mCache = new LruCache<String, Bitmap>(ZUtils.calculateMemoryCacheSize(context)) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        ZUtils.checkNull(key, "key cannot be null");
        ZUtils.checkNull(bitmap, "bitmap cannot be null");
        synchronized (this) {
            if (mCache != null) {
                mCache.put(key, bitmap);

            }
        }
    }

    @Override
    public Bitmap get(String key) {
        ZUtils.checkNull(key, "key cannot be null");
        synchronized (this) {
            if (mCache != null) {
                return mCache.get(key);
            }
        }
        return null;
    }

    @Override
    public synchronized void clear() {
        if (mCache != null) {

            mCache.evictAll();
        }
    }

    @Override
    public synchronized void removeByKey(String key) {
        if (mCache != null) {

            mCache.remove(key);
        }
    }
}
