package com.example.administrator.kok_music_player.services.musicService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * Created by Administrator on 2016/5/26.
 */
public class MyPhoneStateListener extends PhoneStateListener {

    private static final String TAG = "PhoneStatReceiver";
    private HandlePhoneChangedInterface changedInterface;

    public MyPhoneStateListener(HandlePhoneChangedInterface changedInterface) {
          this.changedInterface = changedInterface;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                changedInterface.phoneIdle();
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                changedInterface.phoneRinging();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                changedInterface.phoneOffhook();
                break;
        }
    }

    public interface HandlePhoneChangedInterface {
        void phoneIdle();

        void phoneRinging();

        void phoneOffhook();
    }

}
