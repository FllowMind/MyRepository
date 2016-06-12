package com.example.administrator.kok_music_player.customview;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;


/**
 * Created by Administrator on 2016/4/24.
 */
public class MyStatuBar {

    /*
    * 传入activity 和 SystemBar 的color
    * */
    public static void initSystemBar(Activity activity,int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            setTranslucentStatus(activity, true);

        }



        SystemBarTintManager tintManager = new SystemBarTintManager(activity);

        tintManager.setStatusBarTintEnabled(true);

// 使用颜色资源

        tintManager.setStatusBarTintResource(color);

    }







    private static void setTranslucentStatus(Activity activity, boolean on) {

        Window win = activity.getWindow();

        WindowManager.LayoutParams winParams = win.getAttributes();

        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

        if (on) {

            winParams.flags |= bits;

        } else {

            winParams.flags &= ~bits;

        }

        win.setAttributes(winParams);

    }
}
