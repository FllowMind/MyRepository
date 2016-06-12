package com.example.administrator.kok_music_player.Utils.infoutil;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.method.NumberKeyListener;

/**
 * 存储数据到sharePreference
 * Created by Administrator on 2016/6/2.
 */
public class StoreData {
    private static final String PREFERENCE_NAME = "MY_DATA";
    private static final String MUSIC_NUM = "MUSICNUM";

    //保存当前歌词列表的歌曲数目
    public static void  saveMusicNum(Context context,int num){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(MUSIC_NUM, num);
        editor.commit();
    }

    //获取当前歌词列表的歌曲数目
    public static int getMusicNum(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE);
        return  preferences.getInt(MUSIC_NUM, 0);
    }
}
