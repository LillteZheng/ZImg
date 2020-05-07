package com.zhengsr.zimglib.entrance;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.zhengsr.zimglib.fragment.SupportLifeFragment;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe:
 */
public class ZReceiver {

    private static final ZReceiver INSTANCE = new ZReceiver();

    public static ZReceiver get(){
        return INSTANCE;
    }

    public ZRequestManager get(Activity activity){
        return fragmentGet(activity,activity.getFragmentManager());
    }

    public ZRequestManager get(FragmentActivity activity){
        return supportFragmentGet(activity,activity.getSupportFragmentManager());
    }

    ZRequestManager supportFragmentGet(Context context, FragmentManager fm) {
        SupportLifeFragment current = fm.findFragmentByTag()
        RequestManager requestManager = current.getRequestManager();
        if (requestManager == null) {
            requestManager = new RequestManager(context, current.getLifecycle(), current.getRequestManagerTreeNode());
            current.setRequestManager(requestManager);
        }
        return ZRequestManager;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    ZRequestManager fragmentGet(Context context, android.app.FragmentManager fm) {
        RequestManagerFragment current = getRequestManagerFragment(fm);
        ZRequestManager requestManager = current.getRequestManager();
        if (requestManager == null) {
            requestManager = new RequestManager(context, current.getLifecycle(), current.getRequestManagerTreeNode());
            current.setRequestManager(requestManager);
        }
        return requestManager;
    }
}
