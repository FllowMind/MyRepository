package com.example.administrator.kok_music_player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.kok_music_player.customview.MyStatuBar;
import com.example.administrator.kok_music_player.Utils.musicutils.Lyric;
import com.example.administrator.kok_music_player.Utils.musicutils.LyricHelper;
import com.example.administrator.kok_music_player.Utils.musicutils.MediaUtil;
import com.example.administrator.kok_music_player.Utils.musicutils.MusicInfo;
import com.example.administrator.kok_music_player.customview.LyricView;
import com.example.administrator.kok_music_player.services.musicService.MusicServiceManager;
import com.example.administrator.kok_music_player.services.musicsearchservice.MusicSearchServiceManager;

import java.util.ArrayList;
import java.util.List;

public class PlayMusicActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton play_pause, pre_btn, next_btn, model_btn, list_btn;
    private ImageButton back_button;
    private TextView musicName;
    private LyricView lrcView;
    private SeekBar playpro;
    private TextView curren_pro, total_pro;
    private MusicPlayerInterface playerInterface;
    private final static String BROADCAST_ACTION = "MUSIC_ACTION";
    MyMusicBroadcastRecevier recevier;
    private int currentMusic = -1;
    private boolean isplaying = false;
    private final static String MUSICID = "music_id";
    private final static String PLAY_STATE = "play_state";
    private final static String PLAY_PROGRESS = "play_progress";
    private final static String SONG_CHANGED = "song_changed";
    private final static int UPDATE_PLAY_STATE = 1;
    private final static int UPDATE_PROCESS = 2;
    private final static int UPDATE_SONG = 3;
    private final static int UPDATE_PLAY_MODEL = 4;
    private final static String UPDATE_PROGRESS_ACTION = "update_progress_action";
    private final static String MODEL_CHANGED_ACTION = "MODEL_CHANGED";
    private final static String PLAY_MODEL_KEY = "play_model_key";
    private final static String PROGRESS = "progress";
    private MusicServiceManager musicServiceManager;
    private MusicSearchServiceManager mssManager ;
    private final String TAG = this.getClass().getName();
    private List<Lyric> lrcList = new ArrayList<Lyric>(); //存放歌词列表对象
    private LyricHelper lyricHelper ;

    private static final String PREFERNENCE_NAME = "preferences_name";
    private static final String MUSIC_ID_INFO= "music_id_info";
    private static final String MUSIC_PROGRESS_INFO = "music_progress_info";
    private static final String MUSIC_PLAYMODE_INFO = "music_playmode_info";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        MyStatuBar.initSystemBar(this, Color.TRANSPARENT);
        setContentView(R.layout.activity_play_music);



        curren_pro = (TextView) this.findViewById(R.id.played_time);
        total_pro = (TextView) this.findViewById(R.id.total_time);
        play_pause = (ImageButton) this.findViewById(R.id.play_pause);
        play_pause.setOnClickListener(this);
        pre_btn = (ImageButton) this.findViewById(R.id.pre_song);
        pre_btn.setOnClickListener(this);
        next_btn = (ImageButton) this.findViewById(R.id.next_song);
        next_btn.setOnClickListener(this);
        model_btn = (ImageButton) this.findViewById(R.id.play_model);
        model_btn.setOnClickListener(this);
        list_btn = (ImageButton) this.findViewById(R.id.song_list);
        list_btn.setOnClickListener(this);
        back_button = (ImageButton) this.findViewById(R.id.back_button);
        back_button.setOnClickListener(this);
        musicName = (TextView) this.findViewById(R.id.music_name);
        lrcView = (LyricView) this.findViewById(R.id.lyricView);

        musicServiceManager = new MusicServiceManager(this);//初始化音乐管理器
        mssManager = new MusicSearchServiceManager(this);
        recevier = new MyMusicBroadcastRecevier();

        playpro = (SeekBar) this.findViewById(R.id.play_progress);
        playpro.setMax(1000);

        playpro.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int currentposition = (musicServiceManager.getDuration() / 1000) * (playpro.getProgress());
                Log.i(TAG, "position1" + playpro.getProgress() + "duration" + currentposition);
                musicServiceManager.choosePostionToPlay(currentposition);
            }
        });


    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_PROCESS:
                    int progress = (int) msg.obj;
                    updateprogress(progress);

                    break;
                case UPDATE_SONG:
                    break;
                case UPDATE_PLAY_STATE:
                    boolean play_state = (boolean) msg.obj;
                    updatePlayBtn(play_state);
                    changeMusicName();
                    break;
                case UPDATE_PLAY_MODEL:
                    int model = (int) msg.obj;
                    changeModel(model);
                    break;

            }
        }
    };

    //更新进度
    private void updateprogress(int progress) {
        playpro.setProgress(progress);
        curren_pro.setText(MediaUtil.formaTime(musicServiceManager.getCurrentPosition()));
        total_pro.setText(MediaUtil.formaTime(musicServiceManager.getDuration()));
    }

    private void updatePlayBtn(boolean isplaying) {

        if (isplaying == true) {
            play_pause.setImageDrawable(getResources().getDrawable(R.drawable.pause_button2));
        }else{
            play_pause.setImageDrawable(getResources().getDrawable(R.drawable.play_button2));
        }
        initLrc();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.play_pause:

                if (musicServiceManager.getPlaySate() == true) {
                    musicServiceManager.pauseMusic();//暂停音乐
                    updatePlayBtn(true);
                } else {
                    musicServiceManager.playMusic();//播放
                    updatePlayBtn(false);
                }

                break;
            case R.id.pre_song:
                updatePlayBtn(true);
                musicServiceManager.previousMusic();//上一曲
                break;
            case R.id.next_song:
                updatePlayBtn(true);
                musicServiceManager.nextMusic();//下一曲
                break;
            case R.id.play_model:
                musicServiceManager.changePlayModel();//改变播放模式
                break;
            case R.id.song_list:
                break;
            case R.id.back_button:
                finish();
                break;
        }
    }

    private void changeModel(int model) {
        if (model == 1) {
            model_btn.setImageDrawable(getResources().getDrawable(R.drawable.repeat_model2));
            Toast.makeText(this, "顺序播放", Toast.LENGTH_SHORT).show();
        } else if (model == 2) {
            model_btn.setImageDrawable(getResources().getDrawable(R.drawable.shuffle_model2));
            Toast.makeText(this, "随机播放", Toast.LENGTH_SHORT).show();
        } else {
            model_btn.setImageDrawable(getResources().getDrawable(R.drawable.repeat_one_model));
            Toast.makeText(this, "单曲循环", Toast.LENGTH_SHORT).show();
        }

    }

    /*改变音乐名*/
    private void changeMusicName() {
        musicName.setText(musicServiceManager.getMusicName());
    }

    public void initLrc(){
        MusicInfo musicInfo =(MusicInfo) mssManager.getAllMusicInDataBase().get(musicServiceManager.getMusicId());
//        mLrcProcess = new LrcProcess();
//        //读取歌词文件
//        mLrcProcess.readLRC();
        //传回处理后的歌词文件
        lyricHelper = new LyricHelper(musicInfo.getUrl());
        lrcList = lyricHelper.getLyrics();
        lrcView.setLyricContent(lrcList);
//        //切换带动画显示歌词
//        lrcView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.music_text_change));
        handler.post(mRunnable);
    }
    Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            lrcView.setCurrentTime(musicServiceManager.getCurrentPosition(),true);
            lrcView.invalidate();
            handler.postDelayed(mRunnable, 100);
        }
    };

//    /**
//     * 根据时间获取歌词显示的索引值
//     * @return
//     */
//    public int lrcIndex() {
//        int CURRENT_POSITION =0 ;
//        int duration =0;
//        if(musicServiceManager.getPlaySate()) {
//            CURRENT_POSITION = musicServiceManager.getCurrentPosition();
//            duration = musicServiceManager.getDuration();
//        }
//        if(CURRENT_POSITION < duration) {
//            for (int i = 0; i < lrcList.size(); i++) {
//                if (i < lrcList.size() - 1) {
//                    if (CURRENT_POSITION < lrcList.get(i).getLrcTime() && i == 0) {
//                        index = i;
//                    }
//                    if (CURRENT_POSITION > lrcList.get(i).getLrcTime()
//                            && CURRENT_POSITION < lrcList.get(i + 1).getLrcTime()) {
//                        index = i;
//                    }
//                }
//                if (i == lrcList.size() - 1
//                        && CURRENT_POSITION > lrcList.get(i).getLrcTime()) {
//                    index = i;
//                }
//            }
//        }
//        return index;
//    }

    public class MyMusicBroadcastRecevier extends BroadcastReceiver {
        private int musicid;
        private boolean issongchanged;
        private boolean play_state;

        @Override
        public void onReceive(Context context, Intent intent) {

            Message message ;
            issongchanged = intent.getBooleanExtra(SONG_CHANGED, false);
            musicid = intent.getIntExtra(MUSICID, 0);
            play_state = intent.getBooleanExtra(PLAY_STATE, false);

            if (intent.getAction().equals(BROADCAST_ACTION)) {
                message = handler.obtainMessage();
                if (issongchanged == true) {
                    message.what = UPDATE_SONG;
                    message.obj = musicid;
                    handler.sendMessage(message);
                }
                message = handler.obtainMessage();
                message.what = UPDATE_PLAY_STATE;
                message.obj = play_state;
                handler.sendMessage(message);
            } else if (intent.getAction().equals(UPDATE_PROGRESS_ACTION)) {
                //更新进度
                message = handler.obtainMessage();
                message.what = UPDATE_PROCESS;
                message.obj = intent.getIntExtra(PROGRESS, 0);
                handler.sendMessage(message);

            } else if (intent.getAction().equals(MODEL_CHANGED_ACTION)) {
                //更新播放模式按钮
                message = handler.obtainMessage();
                message.what = UPDATE_PLAY_MODEL;
                message.obj = intent.getIntExtra(PLAY_MODEL_KEY, 1);
                handler.sendMessage(message);
            }

        }
    }



    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(BROADCAST_ACTION);
        filter.addAction(UPDATE_PROGRESS_ACTION);
        filter.addAction(MODEL_CHANGED_ACTION);
        registerReceiver(recevier, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(recevier);

    }


}
