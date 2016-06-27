package com.example.administrator.kok_music_player;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.kok_music_player.Adatpters.AdsViewAdapter;
import com.example.administrator.kok_music_player.Fragments.AlbumChartFragment;
import com.example.administrator.kok_music_player.Fragments.ArtistChartFragment;
import com.example.administrator.kok_music_player.Fragments.MusicChatFragment;
import com.example.administrator.kok_music_player.Utils.GsonUtil;
import com.example.administrator.kok_music_player.Utils.MessageUtil;
import com.example.administrator.kok_music_player.Webservices.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChartActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView musiclist;
    private ProgressDialog progressDialog;
    private MusicChatFragment musicChatFragment;
    private ArtistChartFragment artistChartFragment;
    private AlbumChartFragment albumChartFragment;
    private Button button1, button2, button3;
    private String[] imageurls;
    private List<View> views = new ArrayList<>();
    private ViewPager adsPager;
    private int currentItem = 0;
    private boolean isTouch = false;
    private int pressCount = 0;
    private ImageView pointer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        musicChatFragment = new MusicChatFragment();
        artistChartFragment = new ArtistChartFragment();
        albumChartFragment = new AlbumChartFragment();
        button1 = (Button) this.findViewById(R.id.button1);
        button2 = (Button) this.findViewById(R.id.button2);
        button3 = (Button) this.findViewById(R.id.button3);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        pointer = (ImageView) this.findViewById(R.id.pointer);
        adsPager = (ViewPager) this.findViewById(R.id.ads_viewpager);
        String url1 = getString(R.string.server_url) + getString(R.string.album_ads1);
        String url2 = getString(R.string.server_url) + getString(R.string.album_ads2);
        String url3 = getString(R.string.server_url) + getString(R.string.album_ads3);
        String url4 = getString(R.string.server_url) + getString(R.string.album_ads4);
        String url5 = getString(R.string.server_url) + getString(R.string.album_ads5);
        imageurls = new String[]{url1, url2, url3, url4, url5};
        ImageLoader imageLoader = ImageLoader.getInstance(this);
        imageLoader.loadBitmaps(imageurls, handler);

        musicList();
    }


    private void musicList() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.chart_content, musicChatFragment);
        transaction.commit();
    }

    private void artistList() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.chart_content, artistChartFragment);
        transaction.commit();
    }

    private void albumList() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.chart_content, albumChartFragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                musicList();
                break;
            case R.id.button2:
                artistList();
                break;
            case R.id.button3:
                albumList();
                break;
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MessageUtil.FETCH_DATA_SUCCESS:
                    Log.i("test","bitmap");
                    Bitmap bitmap = (Bitmap)msg.obj;
                    ImageView ads = new ImageView(getApplicationContext());
                    if (bitmap != null) {
                        ads.setScaleType(ImageView.ScaleType.FIT_XY);
                        ads.setImageBitmap(bitmap);
                    }
                    views.add(ads);
                    if(views.size()==5){
                        adsPager.setAdapter(new AdsViewAdapter(getApplicationContext(),views));
                        adsPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                currentItem = position;
                                if (position == 0) {
                                    pointer.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pointer1));
                                } else if (position == 1) {
                                    pointer.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pointer2));
                                } else if (position == 2) {
                                    pointer.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pointer3));
                                } else if (position == 3) {
                                    pointer.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pointer4));
                                } else if (position == 4) {
                                    pointer.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pointer5));
                                }
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });

//                        adsPager.setPageTransformer(true,new DepthPageTransformer());
                        resycleview();
                    }
                    break;
                case MessageUtil.FETCH_DATA_FAILD:
                    Log.i("test","faild");
                    break;
            }
        }
    };
        private void resycleview() {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    if (currentItem < 4) {
                        currentItem++;
                        adsPager.setCurrentItem(currentItem, true);
                    } else {
                        currentItem = 0;
                        adsPager.setCurrentItem(currentItem, false);
                    }

                    handler.postDelayed(this, 5000);
                }
            };

            handler.postDelayed(runnable, 2000);
        }


    }
