package com.zhengsr.zimglib.tools;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;

public class ImageResize {


    /**
     * 拿到数据bitmap
     */
    public static Bitmap decodeBitmap(Resources res,int resId,int reWidth,int reHeight){
        return decodeBitmap(res,resId,Bitmap.Config.ARGB_8888,reWidth,reHeight);
    }

    public static Bitmap decodeBitmap(Resources res,int resId,Bitmap.Config bitmapConfig,int reWidth,int reHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        BitmapFactory.decodeResource(res,resId,options);
        options.inJustDecodeBounds = true;
        options.inSampleSize = calculateInSampleSize(options,reWidth,reHeight);
        options.inPreferredConfig = bitmapConfig;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res,resId,options);
    }

    /**
     * 拿到数据bitmap
     */
    public static Bitmap decodeBitmap(FileDescriptor fd, int reWidth, int reHeight){
        return decodeBitmap(fd,Bitmap.Config.ARGB_8888,reWidth,reHeight);
    }

    public static Bitmap decodeBitmap(FileDescriptor fd, Bitmap.Config bitmapConfig,int reWidth, int reHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        BitmapFactory.decodeFileDescriptor(fd,null,options);
        options.inJustDecodeBounds = true;
        options.inSampleSize = calculateInSampleSize(options,reWidth,reHeight);
        options.inPreferredConfig = bitmapConfig;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd,null,options);
    }

    /**
     * 计算缩放比例
     * @param options
     * @param reWidth
     * @param reHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reWidth, int reHeight) {
        int w = options.outWidth;
        int h = options.outHeight;
        int inSampleSize = 1;
        if (w > reWidth || h > reHeight) {
            int halfWidth = w / 2;
            int halfHeight = h / 2;
            while ((halfHeight / inSampleSize) >= reHeight &&
                    (halfWidth / inSampleSize) >= reWidth) {
                //2的倍数
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
