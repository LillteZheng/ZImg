package com.zhengsr.zimglib.entrance;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.zhengsr.zimglib.bean.ImgBean;
import com.zhengsr.zimglib.util.ZUtils;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe: 封装 request 对象，把数据放到这个类中
 */
public class ZRequestCreator {

    private ImgBean mPlaceholderBean;
    private ImgBean mErrorImgBean;
    private final ZRequest.Builder mRequest;
    private ZCyclerDelegate mDelegate;
    public ZRequestCreator(ZCyclerDelegate delegate,Uri uri,int resId) {
        mDelegate = delegate;

        mRequest = new ZRequest.Builder(uri, resId, Bitmap.Config.ARGB_8888);
    }


    public ZRequestCreator placehoder(@DrawableRes int resId){
        if (mPlaceholderBean == null) {
            mPlaceholderBean = new ImgBean();
        }
        mPlaceholderBean.resId = resId;
        return this;
    }

    public ZRequestCreator placehoder(@NonNull Drawable drawable){
        if (mPlaceholderBean == null) {
            mPlaceholderBean = new ImgBean();
        }
        mPlaceholderBean.drawable = drawable;
        return this;
    }


    public ZRequestCreator error(@DrawableRes int resId){
        if (mErrorImgBean == null) {
            mErrorImgBean = new ImgBean();
        }
        mErrorImgBean.resId = resId;
        return this;
    }

    public ZRequestCreator error(@NonNull Drawable drawable){
        if (mErrorImgBean == null) {
            mErrorImgBean = new ImgBean();
        }
        mErrorImgBean.drawable = drawable;
        return this;
    }

    public ZRequestCreator priority(Priority priority){

        return this;
    }



    public void into(ImageView target) {
        into(target, null);
    }


    public void into(ImageView target,Object listener){
        /**
         * check main thread
         */
        if (!ZUtils.isOnMainThread()){
            throw new IllegalStateException("ZImg should happen from the main thread.");
        }

        ZUtils.checkNull(target,"Target must not be null.");

        /**
         * check for picture
         */
        if (!mRequest.hasImage()) {
            //todo cancel request
            //set placeholder to target
            setPlaceholder(target,mPlaceholderBean);
            return;
        }


        if (!mRequest.hasSize()){
            int width = target.getWidth();
            int height = target.getHeight();
            // 需要自己计算了
            if (width == 0 && height == 0){
                //set placeholder to target
                setPlaceholder(target,mPlaceholderBean);
                calculateSize(target);
                return;
            }
            mRequest.reSize(width,height);
        }
        //todo 如果重写了大小，那么不用管了，直接裁剪

        //todo 设置缓存类的数据


        //todo 创建好数据流Request，给另一个数据，比如action，用来执行bitmap的一些操作

        //todo 把这个action交给线程池去操作，执行完成后，再去加载数据

        target.setImageResource(mRequest.getResourceId());
    }

    /**
     * 计算 target的大小
     * @param target
     */
    private void calculateSize(@NonNull final ImageView target) {
        target.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = target.getWidth();
                int height = target.getHeight();
                if (width != 0 && height != 0){
                    into(target,null);
                    target.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }


    /**
     * set placehodler to  imageview
     * @param target
     * @param placeholderBean
     */
    private void setPlaceholder(ImageView target, ImgBean placeholderBean) {
        if (placeholderBean != null) {
            if (placeholderBean.resId != 0) {
                placeholderBean.drawable = mDelegate.getContext().getResources().getDrawable(placeholderBean.resId);
            }
            if (placeholderBean.drawable != null) {
                target.setImageDrawable(placeholderBean.drawable);
            }
        }
    }


    /**
     * the priority of  request
     */
    public enum Priority {
        LOW,
        NORMAL,
        HIGH
    }
}
