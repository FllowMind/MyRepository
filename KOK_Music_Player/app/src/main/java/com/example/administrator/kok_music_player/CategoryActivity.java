package com.example.administrator.kok_music_player;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class CategoryActivity extends AppCompatActivity {

    private GridView hotview, languageview, styleview, yearview, artistview, moodview, sceneview;

    private String[] hottype = {"经典", "伤感", "英语", "激情", "纯音乐",
            "安静", "中文DJ", "欧美DJ", "怀旧粤语", "草原风"};

    private String[] languages = {"国语", "英语", "日语", "韩语", "法语", "粤语", "闽南语"};

    private String[] sytles = {"流行", "摇滚", "R&B", "嘻哈", "乡村", "拉丁", "爵士",
            "古风", "电子", "古典", "民歌", "中国风", "新世纪", "华语民谣", "BossaNova"};

    private String[] moods = {"安静", "轻松", "激情", "伤感", "励志", "疗伤", "开心", "减压",
            "甜蜜", "想哭", "寂寞", "虐心", "发呆", "想念", "感动", "宣泄", "治疗", "活力", "惬意",
            "温暖", "挑逗", "迷离"};
    private String[] scenes = {"车载", "夜店", "酒吧", "店铺", "校园", "旅途", "KTV", "健身房",
            "咖啡馆", "度假", "失恋", "约会", "派对", "婚礼", "洗澡", "散步", "做家务", "搭公交"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        hotview = (GridView) this.findViewById(R.id.hot);
        languageview = (GridView) this.findViewById(R.id.language);
        styleview = (GridView) this.findViewById(R.id.style);
        moodview = (GridView) this.findViewById(R.id.mood);
        sceneview = (GridView) this.findViewById(R.id.scene);
        init();
    }

    public void init() {
        hotview.setAdapter(new MyAdapter(hottype));
        languageview.setAdapter(new MyAdapter(languages));
        styleview.setAdapter(new MyAdapter(sytles));
        moodview.setAdapter(new MyAdapter(moods));
        sceneview.setAdapter(new MyAdapter(scenes));
    }

    class MyAdapter extends BaseAdapter {

        private String[] infos;

        public MyAdapter(String[] infos) {
            this.infos = infos;
        }

        @Override
        public int getCount() {
            return infos.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView textView;
            if (convertView == null) {
                convertView = new TextView(getApplicationContext());
            }
            textView = (TextView) convertView;
            int num = 1 + (int) (Math.random() * 4);
            Log.i("test", "num" + num);
            if (num == 1) {
                textView.setBackground(getDrawable(R.drawable.textview_backgroud1));
            } else if (num == 2) {
                textView.setBackground(getDrawable(R.drawable.textview_backgroud2));
            } else if (num == 3) {
                textView.setBackground(getDrawable(R.drawable.textview_backgroud3));
            } else {
                textView.setBackground(getDrawable(R.drawable.textview_backgroud4));
            }
//            textView.setBackground(getDrawable(R.drawable.textview_backgroud2));
            textView.setText(infos[position]);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.MAGENTA);
            textView.setPadding(4, 4, 4, 4);
            return convertView;
        }
    }
}
