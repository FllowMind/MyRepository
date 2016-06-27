package com.example.administrator.kok_music_player;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.kok_music_player.Fragments.MusicsetListFragment;
import com.example.administrator.kok_music_player.Utils.MessageUtil;
import com.example.administrator.kok_music_player.Utils.fieldsutils.CommonFidlds;
import com.example.administrator.kok_music_player.Webservices.WebServices;

import java.util.ArrayList;
import java.util.Map;

public class MusicListActivity extends AppCompatActivity {

    private MusicsetListFragment musicsetListFragment;
    private ArrayList<Map<String,String>> infos;
    private String fidld ,value,type,type2;
    private WebServices webServices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        webServices = new WebServices(this);
        Intent intent = getIntent();
        if (intent != null) {
           String []datas =  intent.getStringArrayExtra(CommonFidlds.DATA);
            fidld = datas[0];
            value = datas[1];
            type = datas[2];
            type2 = datas[3];
            webServices.getInfos(fidld, value, type,type2, handler);


        }else{

        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MessageUtil.FETCH_DATA_SUCCESS:
                    infos = (ArrayList<Map<String, String>>) msg.obj;
                    if (infos != null) {
                        musicList(infos);
                    }

                    break;
                case MessageUtil.FETCH_DATA_FAILD:
                    break;
            }
        }
    };
    private void musicList(ArrayList<Map<String,String>> infos) {
        musicsetListFragment = MusicsetListFragment.newInstance(infos, null);
//        musicsetListFragment.initview(infos);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_frame, musicsetListFragment);
        transaction.commit();
    }
}
