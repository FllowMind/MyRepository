package com.example.administrator.kok_music_player.Webservices;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;

import com.example.administrator.kok_music_player.Utils.MessageUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/18.
 */
public class ImageLoader {
    private Context context;

    private static ImageLoader imageLoader;
    private LruCache<String,Bitmap> lruCache;
    private ImageLoader(Context context) {
        this.context = context;
        int maxMemory = (int) (Runtime.getRuntime().maxMemory()) / 1024;
        int cacheSize = maxMemory / 8;
        lruCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    public static ImageLoader getInstance(Context context){
        if(imageLoader==null){
            imageLoader = new ImageLoader(context);
        }
        return imageLoader;
    }

    public void loadBitmaps(String[] imageurls, Handler handler) {

        for(int i=0;i<imageurls.length;i++) {
            if(lruCache.get(imageurls[i])!=null){
                sendMessage(handler,lruCache.get(imageurls[i]));
            }else {
                MyConnection connection = new MyConnection(imageurls[i],handler);
                connection.execute();
            }

        }
    }

    class MyConnection extends AsyncTask {

        String path ;
        private Handler handler;
        public MyConnection(String path,Handler handler) {
            this.handler = handler;
            this.path = path;
        }



        @Override
        protected Object doInBackground(Object[] params) {
            URL r = null;
            Bitmap bitmap = null;
            try {
                r = new URL(path);
                HttpURLConnection connection = (HttpURLConnection) r.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                sendMessage(handler,bitmap);
                lruCache.put(path,bitmap);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    public void sendMessage(Handler handler,Bitmap bitmap){
        Message message = handler.obtainMessage();
        if(bitmap!=null){
            message.what = MessageUtil.FETCH_DATA_SUCCESS;
            message.obj = bitmap;
        }else{
            message.what = MessageUtil.FETCH_DATA_FAILD;
        }
        handler.sendMessage(message);
    }


}
