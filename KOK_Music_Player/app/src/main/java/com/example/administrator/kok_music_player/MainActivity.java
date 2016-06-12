package com.example.administrator.kok_music_player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.kok_music_player.customview.CImageView;
import com.example.administrator.kok_music_player.services.musicService.MusicServiceManager;
import com.example.administrator.kok_music_player.services.musicsearchservice.MusicSearchServiceManager;
import com.example.administrator.kok_music_player.Utils.imageloader.ImageAdapter;
import com.example.administrator.kok_music_player.Utils.imageloader.ImageDownLoader;
import com.example.administrator.kok_music_player.Utils.musicutils.MediaUtil;
import com.example.administrator.kok_music_player.Utils.musicutils.MusicInfo;
import com.example.administrator.kok_music_player.Utils.pictureutil.PictureUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private ImageButton play_pause_btn, next_btn;
    private TextView music_name_TV;
    private CImageView musicpanel;
    private ListView music_listview;
    private ViewPager viewPager;
    private PagerTabStrip pagerTabStrip;
    private MediaPlayer mediaPlayer;
    private File[] musicFiles;
    private MusicPlayerInterface playerInterface;
    private int currentMusic = -1;
    private boolean isplaying = false;
    private final static String BROADCAST_ACTION = "MUSIC_ACTION";
    private final static String MUSICID = "music_id";
    private final static String PLAY_STATE = "play_state";
    private final static String PLAY_PROGRESS = "play_progress";
    private final static String SONG_CHANGED = "song_changed";
    private final static int UPDATE_PLAY_STATE = 1;
    private final static int UPDATE_PLAY_PROCESS = 2;
    private final static int UPDATE_SONG = 3;
    private int Music_Id = 0;
    private int pressCount = 0;
    private int currentPositon = 0;
    private List map3InfoLists;
    private PictureUtil pictureUtil;

    MyMusicBroadcastRecevier recevier;
    private MusicServiceManager musicServiceManager;//音乐播放服务管理器
    private MusicSearchServiceManager musicSSManager;//音乐搜索服务管理器
    private ImageDownLoader mImageDownLoader;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Window window = getWindow();
//取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

//需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//设置状态栏颜色
        window.setStatusBarColor(Color.BLACK);


        ViewGroup mContentView = (ViewGroup) this.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
            ViewCompat.setFitsSystemWindows(mChildView, true);
        }
        if (!this.isTaskRoot()) {
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory((Intent.CATEGORY_LAUNCHER)) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        setContentView(R.layout.activity_main);
//        MyStatuBar.initSystemBar(this,Color.BLACK);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("本地音乐");
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        musicFiles = new File("/storage/emulated/0/我的歌曲/").listFiles();

        musicpanel = (CImageView) this.findViewById(R.id.music_panel);
        musicpanel.setOnClickListener(this);
        play_pause_btn = (ImageButton) this.findViewById(R.id.play_pause_btn);
        play_pause_btn.setOnClickListener(this);
        next_btn = (ImageButton) this.findViewById(R.id.next_music);
        next_btn.setOnClickListener(this);
        music_name_TV = (TextView) this.findViewById(R.id.show_music_name);
        viewPager = (ViewPager) this.findViewById(R.id.music_viewpager);
        pagerTabStrip = (PagerTabStrip) this.findViewById(R.id.tabstrip);

        mImageDownLoader = new ImageDownLoader(this);
        map3InfoLists = MediaUtil.getMp3Infos(this);

        musicSSManager = new MusicSearchServiceManager(this);//初始化音乐搜索服务管理器
        musicServiceManager = new MusicServiceManager(this);//初始化音乐播放服务管理器
        recevier = new MyMusicBroadcastRecevier();//初始化广播接收者
        pictureUtil = new PictureUtil(this);

        initview();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_PLAY_PROCESS:
                    break;
                case UPDATE_SONG:
                    int musicId = (int) msg.obj;
                    MusicInfo musicInfo = (MusicInfo) map3InfoLists.get(musicId);
                    music_name_TV.setText(musicInfo.getTitle());
                    Bitmap bitmap = mImageDownLoader.showCacheBitmap(String.valueOf(musicInfo.getAlbumId()));
                    if (bitmap == null) {
                        bitmap  = mImageDownLoader.downloadImage(musicInfo.getId(),musicInfo.getAlbumId(), new ImageDownLoader.onImageLoaderListener() {

                            @Override
                            public void onImageLoader(Bitmap bitmap, String url) {
                                if (musicpanel != null && bitmap != null) {
                                    musicpanel.setImageBitmap(bitmap);
                                }
                            }
                        });
                    }else{
                        musicpanel.setImageBitmap(bitmap);
                    }


                    break;
                case UPDATE_PLAY_STATE:
                    boolean play_state = (boolean) msg.obj;
                    updatePlayBtn(play_state);
                    break;
            }
        }
    };



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.music_panel:
                Intent intent = new Intent(this, PlayMusicActivity.class);
                intent.putExtra("currentmusic", currentMusic);
                startActivity(intent);
                break;

            case R.id.play_pause_btn:

                if (musicServiceManager.getPlaySate() == true) {
                    musicServiceManager.pauseMusic();//暂停
                } else {
                    musicServiceManager.playMusic();//播放
                }

                break;
            case R.id.next_music:
                musicServiceManager.nextMusic();//下一曲
                break;
        }
    }

    private void initview(){

        final List<String> titlelist = new ArrayList<>();
        titlelist.add("全部");
        titlelist.add("歌手");
        titlelist.add("专辑");

        final List<View> views = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(this);
        View view_all = inflater.inflate(R.layout.music_list_all, null);
        View view_songer = inflater.inflate(R.layout.music_list_album, null);
        View view_album = inflater.inflate(R.layout.music_list_artist, null);

        views.add(view_all);
        views.add(view_songer);
        views.add(view_album);

        music_listview = (ListView) view_all.findViewById(R.id.music_list_all);
        music_listview.setAdapter(new ImageAdapter(this,music_listview,map3InfoLists));
        music_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (musicServiceManager.getPlaySate() == true && musicServiceManager.getMusicId() == position) {
                    updatePlayBtn(false);
                    musicServiceManager.pauseMusic();//暂停音乐
                } else if (musicServiceManager.getPlaySate() == false && musicServiceManager.getMusicId() == position) {
                    updatePlayBtn(true);
                    musicServiceManager.continueToPlay();//继续播放
                } else {

                    updatePlayBtn(true);
                    musicServiceManager.chooseMusicToPlay(position);//选择播放新音乐
                }

            }
        });


        PagerAdapter pageradapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                View view = views.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views.get(position));
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titlelist.get(position);
            }
        };
        pagerTabStrip.setTextColor(Color.WHITE);
        pagerTabStrip.setDrawingCacheBackgroundColor(Color.WHITE);
        pagerTabStrip.setTabIndicatorColor(Color.WHITE);
        viewPager.setAdapter(pageradapter);

    }

    private void updatePlayBtn(boolean isplaying) {

        if (isplaying == true) {
            play_pause_btn.setImageDrawable(getResources().getDrawable(R.drawable.pause_button));
        } else {
            play_pause_btn.setImageDrawable(getResources().getDrawable(R.drawable.play_button));

        }
    }


    public class MyMusicBroadcastRecevier extends BroadcastReceiver {
        private int musicid;
        private boolean issongchanged;
        private boolean play_state;

        @Override
        public void onReceive(Context context, Intent intent) {

            issongchanged = intent.getBooleanExtra(SONG_CHANGED, false);
            musicid = intent.getIntExtra(MUSICID, 0);
            play_state = intent.getBooleanExtra(PLAY_STATE, false);

            Message message1 = handler.obtainMessage();
            if (issongchanged == true) {
                message1.what = UPDATE_SONG;
                message1.obj = musicid;
                handler.sendMessage(message1);
            }
            Message message2 = handler.obtainMessage();
            message2.what = UPDATE_PLAY_STATE;
            message2.obj = play_state;
            handler.sendMessage(message2);


        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        pressCount = 0;
        IntentFilter filter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(recevier, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(recevier);//注销广播接收
        musicServiceManager.unbindService();//注销音乐服务
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            pressCount++;
            if (pressCount == 2) {
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addCategory(Intent.CATEGORY_HOME);
                startActivity(i);
            } else {
                Toast.makeText(this, "再按一次退出到桌面！", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            pressCount = 0;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_scanmusic) {
            Intent scanIntent = new Intent(this, ScanMusicActivity.class);
            startActivity(scanIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_exit) {
            musicServiceManager.pauseMusic();
            musicServiceManager.unbindService();
            System.exit(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}






