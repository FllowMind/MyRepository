package com.example.administrator.kok_music_player;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.administrator.kok_music_player.Utils.GsonUtil;
import com.example.administrator.kok_music_player.Utils.MessageUtil;
import com.example.administrator.kok_music_player.Adatpters.MusicsetAdapter;
import com.example.administrator.kok_music_player.Utils.fieldsutils.CommonFidlds;
import com.example.administrator.kok_music_player.Utils.fieldsutils.MusicSetInfoFields;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/14.
 */
public class MusicSetActivity extends AppCompatActivity {
    private GridView gridView;
    private ArrayList<Map<String,String>> albumInfos;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicset);
        gridView = (GridView)this.findViewById(R.id.musicset);
        MyConnection connection = new MyConnection();
        connection.execute();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MessageUtil.FETCH_DATA_SUCCESS:
                    gridView.setAdapter(new MusicsetAdapter(getApplicationContext(),gridView,albumInfos));
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getApplicationContext(), MusicListActivity.class);
                            String[] datas = {MusicSetInfoFields.MUSICSET_ID, albumInfos.get(position).get(MusicSetInfoFields.MUSICSET_ID),"musicsetofmusic", "musicset"};
                            intent.putExtra(CommonFidlds.DATA, datas);
                            Log.i("test", "start");
                            startActivity(intent);
                        }
                    });
                    break;
                case MessageUtil.FETCH_DATA_FAILD:
                    break;
            }
        }
    };


    private class MyConnection extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                Log.i("test", "connnection1");
                URL r = new URL(getString(R.string.server_url) + "/musicsetinfoservlet");
                HttpURLConnection connection = (HttpURLConnection) r.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(3000);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();
                String param = "type=chart";
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.write(param.getBytes());
                outputStream.flush();
                outputStream.close();
//            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");


                byte[] data = new byte[1024];
                InputStream inputStream = connection.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
//                Map<String, String> userdata = ( Map<String, String>)objectInputStream.readObject();
                String string = (String) objectInputStream.readObject();
                albumInfos = GsonUtil.toObject(string, ArrayList.class);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                int   i=-1;
//                while((i=inputStream.read())!=-1){
//                    baos.write(i);
//                }
                if (albumInfos.size() > 0) {
                    Message message = handler.obtainMessage();
                    message.what = MessageUtil.FETCH_DATA_SUCCESS;
                    handler.sendMessage(message);
                } else {
                    Message message = handler.obtainMessage();
                    message.what = MessageUtil.FETCH_DATA_FAILD;
                    handler.sendMessage(message);
                }

//                Log.i("test", albumInfos.get(0).get("music_title"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }


}
