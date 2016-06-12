package com.example.administrator.kok_music_player.services.musicsearchservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.administrator.kok_music_player.MusicSearchInterface;

import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public class MusicSearchServiceManager {

    Context context;
    private MusicSearchInterface musicSearchInterface;

    public MusicSearchServiceManager(Context context) {

        this.context = context;
        connectToService();
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("test", "inm1");
            musicSearchInterface = (MusicSearchInterface) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private void connectToService(){

        Intent i = new Intent(context, MusicSearchService.class);
        context.bindService(i, connection, context.BIND_AUTO_CREATE);
    }


    public List getAllMusicInDataBase() {
        try {
            return musicSearchInterface.getAllMusicInfro();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 开始扫描
     */
    public void startSearch(){
        try {
            musicSearchInterface.startSearch();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public void stopSearch(){
        try {
            musicSearchInterface.stopSearch();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void unbind(){
        context.unbindService(connection);
    }
}
