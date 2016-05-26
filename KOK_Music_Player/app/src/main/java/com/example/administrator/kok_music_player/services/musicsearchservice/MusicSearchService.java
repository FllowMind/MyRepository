package com.example.administrator.kok_music_player.services.musicsearchservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.example.administrator.kok_music_player.MusicSearchInterface;
import com.example.administrator.kok_music_player.Utils.musicutils.MediaUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public class MusicSearchService extends Service {

    private MusicSearchInterface.Stub musicSearchBinder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicSearchBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        musicSearchBinder = new MusicSearchInterface.Stub() {
            @Override
            public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

            }

            @Override
            public List getAllMusicInfro() throws RemoteException {
                return getAllMusic();
            }


        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private List getAllMusic(){
        return MediaUtil.getMp3Infos(this);
    }


}
