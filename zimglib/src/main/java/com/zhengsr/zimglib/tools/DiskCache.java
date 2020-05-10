package com.zhengsr.zimglib.tools;

import android.content.Context;
import android.graphics.Bitmap;

import com.zhengsr.zimglib.cache.DiskLruCache;
import com.zhengsr.zimglib.callback.ICache;

import java.io.File;
import java.io.IOException;

public class DiskCache implements ICache {
    private final int DISK_CACHE_INDEX = 0;

    public DiskCache(Context context) {
        String path = context.getFilesDir().getAbsolutePath()+ File.separator + "bitmap";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //todo 先判断大小
        try {
            DiskLruCache mCache = DiskLruCache.open(dir,1,1,DISK_CACHE_INDEX);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void put(String key, Bitmap bitmap) {

    }

    @Override
    public Bitmap get(String key) {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public void removeByKey(String key) {

    }
}
