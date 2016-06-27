package com.example.administrator.kok_music_player.Webservices;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.administrator.kok_music_player.R;
import com.example.administrator.kok_music_player.Utils.GsonUtil;
import com.example.administrator.kok_music_player.Utils.MessageUtil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/18.
 */
public class WebServices {

    private Context context;
    private static String path;
    private ArrayList<Map<String, String>> infos;
    private Handler handler;

    public WebServices(Context context) {
        this.context = context;
        path = context.getString(R.string.server_url);
    }


    public void getInfos(String fields, String value, String type, String type2,Handler handler) {
        this.handler = handler;
        MyConnection connection = new MyConnection(fields, value, type,type2);
        connection.execute();


    }



    private class MyConnection extends AsyncTask {
        String field, value, type,type2;
        String url;

        public MyConnection(String field, String value, String type,String type2) {
            this.field = field;
            this.value = value;
            this.type = type;
            this.type2 = type2;
            if (type2.equals("musicset")) {
                url =path+ "/musicinfoservlet";
            }else if(type2.equals("artist")){
                url =path+ "/musicinfoservlet";
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                URL r = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) r.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(3000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                connection.connect();
                String param = field + "=" + value + "";
                Log.i("test", "param" + param);
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.write(param.getBytes());
                outputStream.flush();
                outputStream.close();


                byte[] data = new byte[1024];
                InputStream inputStream = connection.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
//                Map<String, String> userdata = ( Map<String, String>)objectInputStream.readObject();
                String string = (String) objectInputStream.readObject();
                infos = GsonUtil.toObject(string, ArrayList.class);
                Message message = handler.obtainMessage();
                if (infos.size() > 0) {
                    message.what = MessageUtil.FETCH_DATA_SUCCESS;
                    message.obj = infos;
                    handler.sendMessage(message);
                }else{
                    message.what = MessageUtil.FETCH_RECOMMENT_FAILD;
                }

               connection.disconnect();
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
