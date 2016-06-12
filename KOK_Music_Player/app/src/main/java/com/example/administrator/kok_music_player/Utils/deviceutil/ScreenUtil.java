package com.example.administrator.kok_music_player.Utils.deviceutil;

import android.content.Context;

/**
 * Created by Administrator on 2016/6/2.
 */
public class ScreenUtil {

    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
    /**
     * 获取屏幕的高度
     * @param context
     * @return
     */
    public static int getScreenHeigth(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取设备密度
     * @param context
     * @return
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 把密度转换为像素
     */
    public static int dip2px(Context context, float px) {
        final float scale = getScreenDensity(context);
        return (int) (px * scale + 0.5);
    }

}

