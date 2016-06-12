package com.example.administrator.kok_music_player.services.musicsearchservice;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.kok_music_player.MusicSearchInterface;
import com.example.administrator.kok_music_player.Utils.infoutil.StoreData;
import com.example.administrator.kok_music_player.Utils.musicutils.MediaUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public class MusicSearchService extends Service {

    private MusicSearchInterface.Stub musicSearchBinder;
    private List<String> musicpahtList = new ArrayList<>();
    private static final String ACTION_UPDATEPATH = "UPDATEPATH";
    private static final String PATH = "PATH";
    private static final String ACTION_UPDATENUM = "UPDATENUM";
    private static final String NUM = "NUM";
    private static final String ACTION_SCAN_OVER = "SCANOVER";
    private static final String ACTION_MEDIA_SCANNER_SCAN_DIR = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";
    private int num = 0;
    private String path;
    private int scanState = -1;//扫描状态 -1为空闲，0为正在扫描，1为扫描结束
    private boolean isStopScan = false;//是否停止扫描

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

            @Override
            public void startSearch() throws RemoteException {
                if (Environment.getExternalStorageDirectory().exists()) {
                    searchmusic();
                }else{
                    Toast.makeText(getApplicationContext(),"扫描失败，无法检测到SD卡！",Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void stopSearch() throws RemoteException {
                isStopScan=true;
            }


        };

        update();
    }

    private Handler handler = new Handler();

    private void update() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 200);
                if (scanState == 0) {
                    updatepath(path);
                    updateNum(num);
                }else if (scanState==1){
                    notyNumChanged(num);
                    notyScanOver();//通知扫描结束
                    num=0;//num重新置0
                    scanState = -1;//扫描结束状态
                }

            }
        };

        handler.postDelayed(r, 200);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private List getAllMusic() {
        return MediaUtil.getMp3Infos(this);
    }

    private void searchmusic() {

        final File rootfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        scanState = 0;//设置正在扫描状态
        isStopScan = false;
        scanDirAsync(rootfile.getAbsolutePath());
        new Thread(new Runnable() {
            @Override
            public void run() {
                scanFile(musicpahtList, rootfile);
                scanState=1;//扫描结束
            }
        }).start();


    }

    private void scanFile(final List<String> filepathList, final File file) {

        if (isStopScan == false) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    path = f.getPath();
                    scanFile(filepathList, f);
                }
            } else {
                String filename = file.getName();
                if (filename.length() > 4 && filename.substring(filename.length() - 4).equalsIgnoreCase(".mp3")) {
                    filepathList.add(file.getAbsolutePath());
                    num++;
                }
            }
        }


    }

    /**
     * 扫描结束，通知扫描结果
     * @param num
     */
    public void notyNumChanged(int num){
        int numbefore = StoreData.getMusicNum(this);
        int numchange=num-numbefore;
        if (numchange > 0) {
            Toast.makeText(this, "新增加" + numchange + "首歌曲",Toast.LENGTH_LONG).show();
        }
        StoreData.saveMusicNum(this,num);
    }


    /**
     * 扫描指定目录
     * @param dir
     */
    public void scanDirAsync(String dir) {
        Intent scanIntent = new Intent(ACTION_MEDIA_SCANNER_SCAN_DIR);
        scanIntent.setData(Uri.fromFile(new File(dir)));
        sendBroadcast(scanIntent);
    }

    /**
     * 更新界面显示的路径
     * @param path
     */
    private void updatepath(String path) {
        Intent intent = new Intent(ACTION_UPDATEPATH);
        intent.putExtra(PATH, path);
        sendBroadcast(intent);
    }

    /**
     * 更新已扫描到的歌曲数量
     * @param num
     */
    private void updateNum(int num) {
        Intent intent = new Intent(ACTION_UPDATENUM);
        intent.putExtra(NUM, num);
        sendBroadcast(intent);
    }

    /**
     * 通知扫描结束
     */
    private void notyScanOver(){
        Intent intent = new Intent(ACTION_SCAN_OVER);
        sendBroadcast(intent);
    }


}
