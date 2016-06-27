package com.example.administrator.kok_music_player.services.musicService;

import android.app.AlarmManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.animation.AnimationUtils;

import com.example.administrator.kok_music_player.MusicPlayerInterface;
import com.example.administrator.kok_music_player.PlayMusicActivity;
import com.example.administrator.kok_music_player.R;
import com.example.administrator.kok_music_player.Utils.musicutils.MediaUtil;
import com.example.administrator.kok_music_player.Utils.musicutils.MusicInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/19.
 */
public class MusicService extends Service  {

    private MediaPlayer mediaPlayer;
    private MusicPlayerInterface.Stub musicPlayerBinder;
    private int CURRENT_POSITION = 0;
    private int MUSIC_ID = 0;
    private File[] musicFiles;
    private String musicdir = "/storage/emulated/0/我的歌曲/";
    private final static String BROADCAST_ACTION = "MUSIC_ACTION";
    private final static String MUSICID = "music_id";
    private final static String PLAY_STATE = "play_state";
    private final static String PLAY_PROGRESS_ACTION = "play_progress";
    private final static String SONG_CHANGED = "song_changed";
    private final static String PROGRESS = "progress";
    private final static String UPDATE_PROGRESS_ACTION = "update_progress_action";
    private final static String MODEL_CHANGED_ACTION = "MODEL_CHANGED";
    private final static String PLAY_MODEL_KEY = "play_model_key";
    private boolean isplaying = false;//判断是否正在播放
    private long duration = 0;
    private int PLAY_MODEL = 1;//播放模式
    private List musicInfos;
    private final String TAG = this.getClass().getName();
    private boolean isInterrupted = false;//用于判断是否被来电等中断
    private static final String PREFERNENCE_NAME = "preferences_name";
    private static final String MUSIC_ID_INFO= "music_id_info";
    private static final String MUSIC_PROGRESS_INFO = "music_progress_info";
    private static final String MUSIC_PLAYMODE_INFO = "music_playmode_info";

    private  SharedPreferences preferences;




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return musicPlayerBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        saveAllStates();
        mediaPlayer.stop();
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        musicInfos = MediaUtil.getMp3Infos(this);
        File file = new File("/storage/emulated/0/我的歌曲/");
        musicFiles = file.listFiles();
        mediaPlayer = new MediaPlayer();
        //监听来电去电
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new MyPhoneStateListener(new MyPhoneStateListener.HandlePhoneChangedInterface() {
            @Override
            public void phoneIdle() {
                if (isInterrupted == true) {
                    try {
                        musicPlayerBinder.continueToPlay();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    isInterrupted = false;
                }
            }

            @Override
            public void phoneRinging() {
                Log.i(TAG, "phoneRinging");
                if (isplaying==true) {
                    try {
                        musicPlayerBinder.pause();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    isInterrupted = true;
                }
            }
            @Override
            public void phoneOffhook() {
                if (isplaying==true) {
                    try {
                        musicPlayerBinder.pause();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    isInterrupted = true;
                }
            }
        }),PhoneStateListener.LISTEN_CALL_STATE);

        musicPlayerBinder = new MusicPlayerInterface.Stub() {
            @Override
            public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

            }

            @Override
            public void start() throws RemoteException {
                choosePositonToPlay(MUSIC_ID, CURRENT_POSITION);
            }

            @Override
            public void pause() throws RemoteException {
                musicpause();
            }

            @Override
            public void next() throws RemoteException {
                nextmusic();
            }

            @Override
            public void previous() throws RemoteException {
                previousmusic();
            }

            @Override
            public void stop() throws RemoteException {
                mediaPlayer.stop();
                mediaPlayer.release();
            }

            @Override
            public void chooseNewSong(int MUSIC_ID) throws RemoteException {
                chooseToPlay(MUSIC_ID);
            }

            @Override
            public void setPlayModel(int model) throws RemoteException {

            }

            @Override
            public boolean isplaying() throws RemoteException {
                return isplaying;
            }

            @Override
            public int getDuration() throws RemoteException {

                return mediaPlayer.getDuration();
            }

            @Override
            public int getMusicId() throws RemoteException {
                return MUSIC_ID;
            }

            @Override
            public int getcurrentposition() throws RemoteException {

                return mediaPlayer.getCurrentPosition();
            }

            @Override
            public void continueToPlay()throws RemoteException {
                choosePositonToPlay(MUSIC_ID, CURRENT_POSITION);

            }

            /*选择播放的位置*/
            @Override
            public void choosePositonToPlay(int musicId, int position) throws RemoteException {
                choosePositionPlay(musicId, position);

            }

            /*改变播放模式*/
            @Override
            public void changePlayModel() throws RemoteException {
                changeModel();
            }


            /*获取音乐名*/
            @Override
            public String getMusicName() throws RemoteException {
                return getMp3Info(MUSIC_ID).getTitle();
            }

            /*请求当前状态*/
            @Override
            public void requestState() throws RemoteException {
                notyUser(false);
            }

            @Override
            public void savaAllState() throws RemoteException {
                saveAllStates();
            }
        };
        autoMusic();//自动播放下一首歌曲

        preferences = getSharedPreferences(PREFERNENCE_NAME, Context.MODE_PRIVATE);
        setStates();
    }

    //无处理主要用于定时器
    private Handler handler = new Handler() {

    };

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 选择播放音乐
     * @param music_id 播放音乐的Id
     */
    private void chooseToPlay(int music_id) {
        Uri uri = Uri.parse(getMusicPath(music_id));
//        Uri uri = Uri.parse(musicdir + musicFiles[music_id].getName());
        isplaying = true;


        MUSIC_ID = music_id;
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(getApplicationContext(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();  //进行缓冲

        mediaPlayer.setOnPreparedListener(new PreparedListener(0));//注册一个监听器
        notyUser(true);
    }

    /**
     * 选择播放的位置
     * @param music_id
     * @param position
     */
    private void choosePositionPlay(int music_id, int position) {
        Uri uri = Uri.parse(getMusicPath(music_id));
//        Uri uri = Uri.parse(musicdir + musicFiles[music_id].getName());
        isplaying = true;


        MUSIC_ID = music_id;
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(getApplicationContext(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();  //进行缓冲
        mediaPlayer.setOnPreparedListener(new PreparedListener(position));//注册一个监听器
        notyUser(true);

    }

    /**
     * 暂停音乐
     */
    private void musicpause() {

        if (mediaPlayer.isPlaying() == true) {
            mediaPlayer.pause();
            CURRENT_POSITION = mediaPlayer.getCurrentPosition();

            isplaying = false;
            notyUser(true);
        }
    }



    /**
     * 获取歌曲的全部信息
     *
     * @param position
     * @return
     */
    private MusicInfo getMp3Info(int position) {
        MusicInfo mp3Info = (MusicInfo) musicInfos.get(position);
        return mp3Info;
    }

    /**
     * 获取歌曲URL
     * @param position
     * @return 歌曲URL
     */

    private String getMusicPath(int position) {
        MusicInfo mp3Info = (MusicInfo) musicInfos.get(position);
        return mp3Info.getUrl();
//        return "http://172.29.125.1:8080/KOKServer/Musics/DJ.mp3";
    }



    /**
     * 自动播放下一首歌曲
     */
    private void autoMusic(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int progress = (int) ((double) (mediaPlayer.getCurrentPosition() * 1000) / (double) (mediaPlayer.getDuration()));
                if (isplaying == true) {
                    if (progress >= 998) {
                        Log.i(TAG, "next song");
                        autoNextMusicSetting();//自动播放下一首
                    } else {
                        progressChangeNoty(progress);//发送更新进度广播
                    }
                }
                handler.postDelayed(this, 500);

            }
        };

        handler.postDelayed(runnable, 500);
    }

    /**
     * 自动播放下一首歌的设置
     */
    private void autoNextMusicSetting() {
        int musicSize = musicInfos.size();
        if (PLAY_MODEL == 1) {
            //循环顺序播放
            CURRENT_POSITION = 0;
            if (MUSIC_ID < musicSize - 1) {
                MUSIC_ID++;
            } else if (MUSIC_ID == musicSize - 1) {
                MUSIC_ID = 0;
            }

        } else if (PLAY_MODEL == 2) {
            //随机播放
            Double double1 = Math.random();
            MUSIC_ID = (int) (double1 * musicSize);
            Log.i(TAG, "MUSIC_ID:" + MUSIC_ID);
        } else if (PLAY_MODEL == 3) {
            //单曲循环
            MUSIC_ID = MUSIC_ID;
        }

        chooseToPlay(MUSIC_ID);
    }

    /**
     * 手动播放下一首歌
     */
    private void nextmusic() {

        int musicSize = musicInfos.size();
        //循环顺序播放
        CURRENT_POSITION = 0;
        if (MUSIC_ID < musicSize - 1) {
            MUSIC_ID++;
        } else if (MUSIC_ID == musicSize - 1) {
            MUSIC_ID = 0;
        }

        chooseToPlay(MUSIC_ID);

    }

    /**
     * 上一曲
     */
    private void previousmusic() {
        int musicSize = musicInfos.size();
        CURRENT_POSITION = 0;
        if (MUSIC_ID > 0) {
            MUSIC_ID--;
        } else if (MUSIC_ID == 0) {
            MUSIC_ID = musicSize - 1;
        }
        chooseToPlay(MUSIC_ID);

    }

    /**
     * 通知使用服务者
     * @param songchanged
     */

    private void notyUser(boolean songchanged) {
        Intent broadcastintent = new Intent(BROADCAST_ACTION);
        broadcastintent.putExtra(MUSICID, MUSIC_ID);
        broadcastintent.putExtra(PLAY_STATE, isplaying);
        broadcastintent.putExtra(SONG_CHANGED, songchanged);
        sendBroadcast(broadcastintent);
    }

    /**
     * 改变播放模式
     */
    private void changeModel() {
        if (PLAY_MODEL < 3) {
            PLAY_MODEL++;
        } else {
            PLAY_MODEL = 1;
        }
        modelChangedNoty();
    }

    /**
     * 该方法用于发送通知模式改变的广播
     */
    private void modelChangedNoty() {
        Intent intent = new Intent(MODEL_CHANGED_ACTION);
        intent.putExtra(PLAY_MODEL_KEY, PLAY_MODEL);
        sendBroadcast(intent);
    }

    /**
     * 进度更新广播
     * @param progress 当前进度
     */

    private void progressChangeNoty(int progress) {

        Intent intent = new Intent(UPDATE_PROGRESS_ACTION
        );
        intent.putExtra(PROGRESS, progress);
        sendBroadcast(intent);
    }

    /**
     * 保存状态信息
     */
    private void saveAllStates(){
        Log.i("test", "save");
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(MUSIC_ID_INFO, MUSIC_ID);
        editor.putInt(MUSIC_PLAYMODE_INFO, PLAY_MODEL);
        editor.putInt(MUSIC_PROGRESS_INFO, mediaPlayer.getCurrentPosition());
        editor.commit();
    }

    private void setStates(){
        MUSIC_ID= preferences.getInt(MUSIC_ID_INFO, 0);
        CURRENT_POSITION = preferences.getInt(MUSIC_PROGRESS_INFO, 0);
        PLAY_MODEL = preferences.getInt(MUSIC_PLAYMODE_INFO, 1);
        Log.i("test", "Musid" + MUSIC_ID);
    }


    /**
     * 歌曲准备状态监听内部类
     */
    private final class PreparedListener implements MediaPlayer.OnPreparedListener {
        private int positon;

        public PreparedListener(int positon) {
            this.positon = positon;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            duration = mediaPlayer.getDuration();
            mediaPlayer.start();    //开始播放

            if (positon > 0) {    //如果音乐不是从头播放
                mediaPlayer.seekTo(positon);
            }
        }
    }



}
