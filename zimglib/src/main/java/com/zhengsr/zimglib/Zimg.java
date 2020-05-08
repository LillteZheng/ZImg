package com.zhengsr.zimglib;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.zhengsr.zimglib.entrance.ZCyclerDelegate;
import com.zhengsr.zimglib.util.ZUtils;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe: Zimg 是平时用来学习图片加载的，记得有一次也被问道 glide 的源码问题，
 * 我不会啊，怎么办？挂了，想了想，glide 确实强大，也确实要好好学些一下；
 * 所以，在 Zimg 你会看到大量的 glide的影子，是我用来学习的，不是抄袭，读书人的事，
 * 能叫抄吗？这是借鉴！！！ 轻喷，我胆小
 */
public class Zimg {


    public static ZCyclerDelegate with(Context context){
        return getReceiver(context).get(context);
    }

    public static ZCyclerDelegate with(Activity activity){
        return getReceiver(activity).get(activity);
    }

    public static ZCyclerDelegate with(FragmentActivity activity){
        return getReceiver(activity).get(activity);
    }

    public static ZCyclerDelegate with(Fragment fragment){
        return getReceiver(fragment.getActivity()).get(fragment);
    }

    public static ZCyclerDelegate with(android.app.Fragment fragment){
        return getReceiver(fragment.getActivity()).get(fragment);
    }


    private static ZCyclerDelegate getReceiver(Context context){
        ZUtils.checkNull(context,
                "You cannot start a load on a not yet attached View or a Fragment where getActivity() "
                        + "returns null (which usually occurs when getActivity() is called before the Fragment "
                        + "is attached or after the Fragment is destroyed).");
        return ZCyclerDelegate.get();
    }
}
