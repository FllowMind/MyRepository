package com.example.administrator.kok_music_player.Adatpters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.text.BoringLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.kok_music_player.R;
import com.example.administrator.kok_music_player.Utils.MessageUtil;
import com.example.administrator.kok_music_player.Utils.ThreadPool;
import com.example.administrator.kok_music_player.Utils.pictureutil.BitmapUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Administrator on 2016/6/13.
 */
public class AdsViewAdapter extends PagerAdapter {

    private List<View> views ;
    private ImageView pointer;
    private String imageurl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/kokplayer/";
    private Context context;
    private String[] imageurls;

    public AdsViewAdapter(Context context,List<View> views) {
        this.context = context;
       this.views = views;
    }


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
        container.addView(views.get(position));
        return views.get(position);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }



}
