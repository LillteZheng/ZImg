package com.zhengsr.zimglib.entrance;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Px;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe:
 */
public class ZRequest {


    /**
     * 不公布出去
     */
    private ZRequest(){

    }

    /**
     * builder for creating {@link ZRequest} instances
     */

    public static class  Builder {
        private Uri uri;
        private int resourceId;
        private boolean centerCrop;
        private boolean centerInside;
        private Bitmap.Config config;
        private ZRequestCreator.Priority priority;
        private int targetWidth;
        private int targetHeight;

        Builder(Uri uri, int resourceId, Bitmap.Config bitmapConfig) {
            this.uri = uri;
            this.resourceId = resourceId;
            this.config = bitmapConfig;
        }

        boolean hasImage() {
            return uri != null || resourceId != 0;
        }

        boolean hasSize() {
            return targetWidth != 0 || targetHeight != 0;
        }

        boolean hasPriority() {
            return priority != null;
        }


        Builder reSize(@Px int targetWidth, @Px int targetHeight) {
            if (targetWidth < 0) {
                throw new IllegalArgumentException("Width must be positive number or 0.");
            }
            if (targetHeight < 0) {
                throw new IllegalArgumentException("Height must be positive number or 0.");
            }
            if (targetHeight == 0 && targetWidth == 0) {
                throw new IllegalArgumentException("At least one dimension has to be positive number.");
            }
            this.targetWidth = targetWidth;
            this.targetHeight = targetHeight;
            return this;
        }


        public Uri getUri() {
            return uri;
        }

        public int getResourceId() {
            return resourceId;
        }
    }

}
