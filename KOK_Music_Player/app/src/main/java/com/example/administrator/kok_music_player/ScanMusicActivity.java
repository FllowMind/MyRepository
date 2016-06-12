package com.example.administrator.kok_music_player;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.kok_music_player.Fragments.FileListFragment;
import com.example.administrator.kok_music_player.Fragments.ScanFragment;
import com.example.administrator.kok_music_player.Fragments.ScanSettingFragment;
import com.example.administrator.kok_music_player.customview.MyStatuBar;
import com.example.administrator.kok_music_player.customview.titlepopubview.ActionItem;
import com.example.administrator.kok_music_player.customview.titlepopubview.MyMenu;
import com.example.administrator.kok_music_player.services.musicsearchservice.MusicSearchServiceManager;

/**
 * Created by Administrator on 2016/5/30.
 */
public class ScanMusicActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView title_tv, path_tv, num_tv;
    private ImageButton back_button, menu_button;
    private Button scan_button;
    private Recevier recevier;

    private static final String ACTION_UPDATEPATH = "UPDATEPATH";
    private static final String PATH = "PATH";
    private static final String ACTION_UPDATENUM = "UPDATENUM";
    private static final String NUM = "NUM";
    private static final String ACTION_SCAN_OVER = "SCANOVER";
    private boolean isScanning = false;
    private boolean isFristFrag = true;//判断是否是第一个fragment

    private MusicSearchServiceManager mssManager;
    //定义标题栏弹窗按钮
    private MyMenu myMenu;

    private ScanFragment scanFragment;
    private ScanSettingFragment settingFragment;
    private FileListFragment fileListFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyStatuBar.initSystemBar(this, R.color.systembar_color_red);
        setContentView(R.layout.activity_scanmusic);

//      默认Fragment
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        scanFragment = new ScanFragment();
        transaction.replace(R.id.content_frame, scanFragment);
        transaction.commit();

        title_tv = (TextView) this.findViewById(R.id.titlebar_title);
        back_button = (ImageButton) this.findViewById(R.id.titlebar_back_btn);
        menu_button = (ImageButton) this.findViewById(R.id.titlebar_funtion_btn);


        title_tv.setText(R.string.scan_music);
        back_button.setOnClickListener(this);
        menu_button.setOnClickListener(this);

        initMenu();
    }

    /**
     * 初始化数据
     */
    private void initMenu(){
        //实例化标题栏弹窗
        myMenu = new MyMenu(this, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        //给标题栏弹窗添加子类
        myMenu.addAction(new ActionItem(this, "扫描设置", R.drawable.scanmusic));
        myMenu.addAction(new ActionItem(this, "自定义扫描", R.drawable.scanmusic));
        myMenu.setItemOnClickListener(new MyMenu.MyOnItemOnClickListener() {
            @Override
            public void onItemClick(ActionItem item, int position) {
                isFristFrag = false;
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                if (position == 0) {
                    title_tv.setText("扫描设置");
                    menu_button.setVisibility(View.GONE);
                    if (settingFragment == null) {
                        settingFragment = new ScanSettingFragment();
                    }
                    transaction.add(R.id.content_frame, settingFragment);
                    transaction.addToBackStack(null);
                } else if (position == 1) {
                    title_tv.setText("选择要扫描的文件夹");
                    menu_button.setVisibility(View.GONE);
                    if (fileListFragment == null) {
                        fileListFragment = new FileListFragment();
                    }
                    transaction.add(R.id.content_frame, fileListFragment);
                    transaction.addToBackStack(null);
                }
                transaction.hide(scanFragment);
                transaction.commit();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (isFristFrag==false) {

                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.content_frame,scanFragment);
                transaction.commit(); title_tv.setText("扫描音乐");
                menu_button.setVisibility(View.VISIBLE);
                isFristFrag = true;


        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_back_btn:
                onBackPressed();
                break;
            case R.id.titlebar_funtion_btn:
                myMenu.show(v);
                break;

        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPDATENUM);
        filter.addAction(ACTION_UPDATEPATH);
        filter.addAction(ACTION_SCAN_OVER);
        registerReceiver(recevier, filter);
    }

    class Recevier extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_UPDATEPATH)) {
                String path = intent.getStringExtra(PATH);
                path_tv.setText(path);
            } else if (intent.getAction().equals(ACTION_UPDATENUM)) {
                int num = intent.getIntExtra(NUM, 0);
                num_tv.setText("已扫描到" + num + "首歌曲");
            } else if (intent.getAction().equals(ACTION_SCAN_OVER)) {
                isScanning = false;
                path_tv.setText("");
                num_tv.setText("");
                scan_button.setText("重新扫描");
            }
        }
    }

    @Override
    protected void onDestroy() {
//        mssManager.unbind();// 关闭文件搜索服务
        super.onDestroy();
    }
}
