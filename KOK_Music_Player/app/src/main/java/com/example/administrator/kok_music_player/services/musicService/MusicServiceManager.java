package com.example.administrator.kok_music_player.services.musicService;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.administrator.kok_music_player.MusicPlayerInterface;

/**
 * Created by Administrator on 2016/5/22.
 */
public class MusicServiceManager {
    private Context context;
    private MusicPlayerInterface playerInterface;
    private final static String BROADCAST_ACTION = "MUSIC_ACTION";


    public MusicServiceManager(Context context) {
        this.context = context;
        connectToService();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            playerInterface = (MusicPlayerInterface) service;

            try {
                playerInterface.requestState();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                playerInterface.stop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    //连接到服务
    private void connectToService() {
        Intent intent = new Intent(context, MusicService.class);
        context.bindService(intent, serviceConnection, context.BIND_AUTO_CREATE);
    }

    //取消捆绑服务
    public void unbindService(){
        context.unbindService(serviceConnection);
    }

    //暂停音乐
    public void pauseMusic(){
        try {
            playerInterface.pause();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //播放音乐
    public void playMusic(){
        try {
            playerInterface.start();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /*上一曲*/
    public void previousMusic(){
        try {
            playerInterface.previous();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /*下一曲*/

    public void nextMusic(){
        try {
            playerInterface.next();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /*选择音乐*/
    public void chooseMusicToPlay(int musicId) {
        try {
            playerInterface.chooseNewSong(musicId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /*继续播放*/
    public void continueToPlay(int musicid) {
        try {
            playerInterface.continueToPlay(musicid);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /*选择播放的位置*/
    public void choosePostionToPlay(int position) {
        try {
            playerInterface.choosePositonToPlay(playerInterface.getMusicId(),position);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /*得到当前音乐进度*/
    public int getMusicProgress(){
        int progress = 0;
        try {
        progress = (int) ((double) (playerInterface.getcurrentposition() * 1000) / (double) (playerInterface.getDuration()));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return progress;
    }

    /*当前播放状态*/
    public boolean getPlaySate(){
        try {
            return playerInterface.isplaying();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
          return false;
    }
    /*当前音乐总时长*/
    public int getDuration(){
        try {
            return playerInterface.getDuration();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }
    /*当前音乐播放位置*/
    public int getCurrentPosition(){
        try {
            return playerInterface.getcurrentposition();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /*当前音乐名*/
    public String getMusicName(){
        try {
            return playerInterface.getMusicName();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*当前音乐ID*/
    public int getMusicId(){
        try {
            return playerInterface.getMusicId();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void changePlayModel(){
        try {
            playerInterface.changePlayModel();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
