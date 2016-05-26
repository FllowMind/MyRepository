package com.example.administrator.kok_music_player.services.musicService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;

/**
 * Created by Administrator on 2016/5/26.
 */
public class MyPhoneStateListener extends PhoneStateListener {

    private static final String TAG = "PhoneStatReceiver";

    public MyPhoneStateListener() {
    }

    public interface PhoneState{
        void phoneIdle();
        void phoneRinging();
        void phoneOffhook();
    }

}
