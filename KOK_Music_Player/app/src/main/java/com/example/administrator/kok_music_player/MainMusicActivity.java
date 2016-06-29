package com.example.administrator.kok_music_player;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.kok_music_player.Adatpters.AdsViewAdapter;
import com.example.administrator.kok_music_player.Utils.GsonUtil;
import com.example.administrator.kok_music_player.Utils.MessageUtil;
import com.example.administrator.kok_music_player.Utils.fieldsutils.MusicSetImageFields;
import com.example.administrator.kok_music_player.Utils.fieldsutils.MusicSetInfoFields;
import com.example.administrator.kok_music_player.Utils.imageloader2.ImageDownLoader;
import com.example.administrator.kok_music_player.Webservices.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/13.
 */
public class MainMusicActivity extends AppCompatActivity implements View.OnClickListener {
    private Button local_btn, chart_btn, category_btn, musicset_btn;
    private ImageView pointer;
    private ListView musicsetList;
    private ViewPager adsPager;
    private int currentItem = 0;
    private boolean isTouch = false;
    private int pressCount = 0;
    private String[] imageurls;
    private List<View> views = new ArrayList<>();
    private ImageLoader imageLoader;
    private ArrayList<Map<String, String>> recommendinfos = new ArrayList<>();
    private TextView musicset_title1, musicset_title2, musicset_title3, musicset_title4, musicset_title5;
    private TextView musicsetinfo1, musicsetinfo2, musicsetinfo3, musicsetinfo4, musicsetinfo5;
    private ImageView musicset_pic1, musicset_pic2, musicset_pic3, musicset_pic4, musicset_pic5;
    private ImageDownLoader downLoader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_music);

        musicset_btn = (Button) this.findViewById(R.id.button2);
        chart_btn = (Button) this.findViewById(R.id.button1);
        category_btn = (Button) this.findViewById(R.id.button3);
        local_btn = (Button) this.findViewById(R.id.button4);
        pointer = (ImageView) this.findViewById(R.id.pointer);
        adsPager = (ViewPager) this.findViewById(R.id.ads_viewpager);
        local_btn.setOnClickListener(this);
        chart_btn.setOnClickListener(this);
        category_btn.setOnClickListener(this);
        musicset_btn.setOnClickListener(this);
        String url1 = getString(R.string.server_url) + getString(R.string.album_ads1);
        String url2 = getString(R.string.server_url) + getString(R.string.album_ads2);
        String url3 = getString(R.string.server_url) + getString(R.string.album_ads3);
        String url4 = getString(R.string.server_url) + getString(R.string.album_ads4);
        String url5 = getString(R.string.server_url) + getString(R.string.album_ads5);
        imageurls = new String[]{url1, url2, url3, url4, url5};
        imageLoader = ImageLoader.getInstance(this);
        imageLoader.loadBitmaps(imageurls, handler);
        downLoader = new ImageDownLoader(this);
        initRecommends();

        MyConnection connection = new MyConnection();
        connection.execute();

        LinearLayout ll = (LinearLayout) this.findViewById(R.id.all_listen);
        ll.setOnClickListener(this);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MessageUtil.FETCH_DATA_SUCCESS:
                    Log.i("test", "bitmap");
                    Bitmap bitmap = (Bitmap) msg.obj;
                    ImageView ads = new ImageView(getApplicationContext());
                    if (bitmap != null) {
                        ads.setScaleType(ImageView.ScaleType.FIT_XY);
                        ads.setImageBitmap(bitmap);
                    }
                    views.add(ads);
                    if (views.size() == 5) {
                        adsPager.setAdapter(new AdsViewAdapter(getApplicationContext(), views));
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

                        adsPager.setPageTransformer(true, new DepthPageTransformer());
                        resycleview();
                    }
                    break;
                case MessageUtil.FETCH_DATA_FAILD:
                    Log.i("test", "faild");
                    break;
                case MessageUtil.FETCH_RECOMMENT_FAILD:
                    setRecommend();
                    break;
            }
        }
    };

    private void initRecommends() {
        musicset_pic1 = (ImageView) this.findViewById(R.id.music_photo1);
        musicset_pic2 = (ImageView) this.findViewById(R.id.music_photo2);
        musicset_pic3 = (ImageView) this.findViewById(R.id.music_photo3);
        musicset_pic4 = (ImageView) this.findViewById(R.id.music_photo4);
        musicset_pic5 = (ImageView) this.findViewById(R.id.music_photo5);

        musicset_title1 = (TextView) this.findViewById(R.id.musicset_title1);
        musicset_title2 = (TextView) this.findViewById(R.id.musicset_title2);
        musicset_title3 = (TextView) this.findViewById(R.id.musicset_title3);
        musicset_title4 = (TextView) this.findViewById(R.id.musicset_title4);
        musicset_title5 = (TextView) this.findViewById(R.id.musicset_title5);

        musicsetinfo1 = (TextView) this.findViewById(R.id.summry1);
        musicsetinfo2 = (TextView) this.findViewById(R.id.summry2);
        musicsetinfo3 = (TextView) this.findViewById(R.id.summry3);
        musicsetinfo4 = (TextView) this.findViewById(R.id.summry4);
        musicsetinfo5 = (TextView) this.findViewById(R.id.summry5);


    }

    private void setRecommend() {
        musicset_pic1.setImageBitmap(downLoader.downloadImage(getString(R.string.server_url) +
                recommendinfos.get(0).get(MusicSetImageFields.MUSICSET_IMAGE_URL), null));
        musicset_pic2.setImageBitmap(downLoader.downloadImage(getString(R.string.server_url) +
                recommendinfos.get(1).get(MusicSetImageFields.MUSICSET_IMAGE_URL), null));
        musicset_pic3.setImageBitmap(downLoader.downloadImage(getString(R.string.server_url) +
                recommendinfos.get(2).get(MusicSetImageFields.MUSICSET_IMAGE_URL), null));
        musicset_pic4.setImageBitmap(downLoader.downloadImage(getString(R.string.server_url) +
                recommendinfos.get(3).get(MusicSetImageFields.MUSICSET_IMAGE_URL), null));
        musicset_pic5.setImageBitmap(downLoader.downloadImage(getString(R.string.server_url) +
                recommendinfos.get(4).get(MusicSetImageFields.MUSICSET_IMAGE_URL), null));

        musicsetinfo1.setText("| 歌单介绍:" + recommendinfos.get(0).get(MusicSetInfoFields.MUSICSET_INFO));
        musicsetinfo2.setText("| 歌单介绍:" + recommendinfos.get(1).get(MusicSetInfoFields.MUSICSET_INFO));
        musicsetinfo3.setText("| 歌单介绍:" + recommendinfos.get(2).get(MusicSetInfoFields.MUSICSET_INFO));
        musicsetinfo4.setText("| 歌单介绍:" + recommendinfos.get(3).get(MusicSetInfoFields.MUSICSET_INFO));
        musicsetinfo5.setText("| 歌单介绍:" + recommendinfos.get(4).get(MusicSetInfoFields.MUSICSET_INFO));

        musicset_title1.setText("| " + recommendinfos.get(0).get(MusicSetInfoFields.MUSICSET_TITLE));
        musicset_title2.setText("| " + recommendinfos.get(1).get(MusicSetInfoFields.MUSICSET_TITLE));
        musicset_title3.setText("| " + recommendinfos.get(2).get(MusicSetInfoFields.MUSICSET_TITLE));
        musicset_title4.setText("| " + recommendinfos.get(3).get(MusicSetInfoFields.MUSICSET_TITLE));
        musicset_title5.setText("| " + recommendinfos.get(4).get(MusicSetInfoFields.MUSICSET_TITLE));


    }

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

    @Override
    public void onBackPressed() {

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


    private class MyConnection extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                URL r = new URL(getString(R.string.server_url) + "/musicsetinfoservlet");
                HttpURLConnection connection = (HttpURLConnection) r.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(3000);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();
                String param = "type=recommend";
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.write(param.getBytes());
                outputStream.flush();
                outputStream.close();
//            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");


                byte[] data = new byte[1024];
                InputStream inputStream = connection.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                String string = (String) objectInputStream.readObject();
                recommendinfos = GsonUtil.toObject(string, ArrayList.class);

                if (recommendinfos.size() > 0) {
                    Message message = handler.obtainMessage();
                    message.what = MessageUtil.FETCH_RECOMMENT_FAILD;
                    handler.sendMessage(message);
                } else {
                    Message message = handler.obtainMessage();
                    message.what = MessageUtil.FETCH_DATA_FAILD;
                    handler.sendMessage(message);
                }
                connection.disconnect();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                Intent intent1 = new Intent(this, ChartActivity.class);
                startActivity(intent1);
                break;
            case R.id.button2:
                Intent intent2 = new Intent(this, MusicSetActivity.class);
                startActivity(intent2);
                break;
            case R.id.button3:
                Intent intent3 = new Intent(this, CategoryActivity.class);
                startActivity(intent3);
                break;
            case R.id.button4:
                Intent intent4 = new Intent(this, MainActivity.class);
                startActivity(intent4);
                break;
            case R.id.all_listen:
                PackageManager pn = getPackageManager();
                List<ResolveInfo>act = pn.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),0);
                if(act.size()==0){
                    Log.i("test","没有语音包");
                }else{
                    Log.i("test","有语音包");
                    Intent intent5 = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent5.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent5.putExtra(RecognizerIntent.EXTRA_PROMPT, "语音输入");
                    startActivityForResult(intent5, 1);
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                musicsetinfo1.setText(matches.get(0));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            if (position < -1) {
                view.setAlpha(0);
            } else if (position <= 0) {
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);
            } else if (position <= 1) {
                view.setAlpha(1 - position);
                view.setTranslationX(pageWidth * -position);
                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            } else {
                view.setAlpha(0);
            }
        }
    }
}
