package com.zhengsr.zimglib.entrance;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.zhengsr.zimglib.callback.LifeListenerAdapter;
import com.zhengsr.zimglib.fragment.LifeFragment;
import com.zhengsr.zimglib.fragment.SupportLifeFragment;
import com.zhengsr.zimglib.util.ZUtils;
import com.zhengsr.zimglib.util.LggUtils;

import java.io.File;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe:
 */
public class ZCyclerDelegate {
    static final String FRAGMENT_TAG = "com.zhengsr.zimglib.entrance.manager";
    private static final ZCyclerDelegate INSTANCE = new ZCyclerDelegate();
    private Context mContext;
    public static ZCyclerDelegate get(){
        return INSTANCE;
    }


    public ZCyclerDelegate get(Context context){
        mContext = context;
        if (context == null) {
            throw new IllegalArgumentException("You cannot start a load on a null Context");
        } else if (ZUtils.isOnMainThread() && !(context instanceof Application)) {
            if (context instanceof FragmentActivity) {
                return get((FragmentActivity) context);
            } else if (context instanceof Activity) {
                return get((Activity) context);
            } else if (context instanceof ContextWrapper) {
                return get(((ContextWrapper) context).getBaseContext());
            }
        }
        //如果是 application，则不用管生命周期了，肯定跟着整个应用走了
        return this;
    }



    public ZCyclerDelegate get(Activity activity){
        if (ZUtils.isOnBackgroundThread()) {
            return get(activity.getApplicationContext());
        } else {
            assertNotDestroyed(activity);
            android.app.FragmentManager fm = activity.getFragmentManager();
            return fragmentGet(activity, fm);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public ZCyclerDelegate get(android.app.Fragment fragment) {
        if (fragment.getActivity() == null) {
            throw new IllegalArgumentException("You cannot start a load on a fragment before it is attached");
        }
        if (ZUtils.isOnBackgroundThread() ) {
            return get(fragment.getActivity().getApplicationContext());
        } else {
            //用 childfragmanager 拿到fragment 的堆栈
            android.app.FragmentManager fm = fragment.getChildFragmentManager();
            return fragmentGet(fragment.getActivity(), fm);
        }
    }

    public ZCyclerDelegate get(FragmentActivity activity){
        LggUtils.d("ZCyclerDelegate -get:" +ZUtils.isOnBackgroundThread());
        if (ZUtils.isOnBackgroundThread()) {
            return get(activity.getApplicationContext());
        } else {
            assertNotDestroyed(activity);
            FragmentManager fm = activity.getSupportFragmentManager();
            return fragmentGet(activity, fm);
        }
    }

    public ZCyclerDelegate get(Fragment fragment) {
        if (fragment.getActivity() == null) {
            throw new IllegalArgumentException("You cannot start a load on a fragment before it is attached");
        }
        if (ZUtils.isOnBackgroundThread()) {
            return get(fragment.getActivity().getApplicationContext());
        } else {
            FragmentManager fm = fragment.getChildFragmentManager();
            return fragmentGet(fragment.getActivity(), fm);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void assertNotDestroyed(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
        }
    }




    private ZCyclerDelegate fragmentGet(Context context, android.app.FragmentManager  fm){
        mContext = context;
        LifeFragment fragment = (LifeFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new LifeFragment();
            fragment.registerListener(new LifeListener());
            fm.beginTransaction().add(fragment,FRAGMENT_TAG).commitAllowingStateLoss();
        }
        return this;
    }

    private ZCyclerDelegate fragmentGet(Context context, FragmentManager  fm){
        mContext = context;
        SupportLifeFragment fragment = (SupportLifeFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new SupportLifeFragment();
            fragment.registerListener(new LifeListener());
            fm.beginTransaction().add(fragment,FRAGMENT_TAG).commitAllowingStateLoss();
        }
        return this;
    }


    //====================================================
    //                    load method
    //====================================================

    public ZRequestCreator load(@DrawableRes int resId){
        if (resId == 0) {
            throw new IllegalArgumentException("Resource ID must not be zero.");
        }
        return new ZRequestCreator(this,null,resId);
    }

    public ZRequestCreator load(@Nullable Uri uri) {
        return new ZRequestCreator( this,uri, 0);
    }

    public ZRequestCreator load(@NonNull File file) {
        if (file == null) {
            return new ZRequestCreator( this,null, 0);
        }
        return load(Uri.fromFile(file));
    }

    public ZRequestCreator load(@Nullable String path) {
        if (path == null) {
            return new ZRequestCreator( this,null, 0);
        }
        if (path.trim().length() == 0) {
            throw new IllegalArgumentException("Path must not be empty.");
        }
        return load(Uri.parse(path));
    }


    public Context getContext(){
        return mContext;
    }

    class  LifeListener extends LifeListenerAdapter{
        @Override
        public void onStart() {
            super.onStart();
            LggUtils.d("LifeListener - onStart:" );
        }

        @Override
        public void onPause() {
            super.onPause();
            LggUtils.d("LifeListener - onPause:" );
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            LggUtils.d("LifeListener - onDestroy:" );
        }
    }
}
