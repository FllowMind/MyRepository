package com.example.administrator.kok_music_player.customview.titlepopubview;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.kok_music_player.R;
import com.example.administrator.kok_music_player.Utils.deviceutil.ScreenUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/2.
 */
public class MyMenu extends PopupWindow {

    private static int List_Padding = 10;
    private int screenHeight, screenWidth;
    private Rect mRect = new Rect();
    private int[] mLocation = new int[2];
    private boolean isNeedUpdate;
    private int popupgravity = Gravity.NO_GRAVITY;
    private MyOnItemOnClickListener onItemClickListener;
    private ListView listView;
    private ArrayList<ActionItem> actionItems = new ArrayList<>();
    private Context context;

    public MyMenu(Context context) {
        //设置布局的参数
        this(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public MyMenu(Context context, int width, int height) {
        this.context = context;
        setFocusable(true);
        setOutsideTouchable(true);
        setTouchable(true);

        screenHeight = ScreenUtil.getScreenHeigth(context);
        screenWidth = ScreenUtil.getScreenWidth(context);

        setWidth(screenWidth / 3);
        setHeight(height);
        setBackgroundDrawable(new BitmapDrawable());
        setContentView(LayoutInflater.from(context).inflate(R.layout.title_popup, null));

        initUT();

    }

    private void initUT() {
        listView = (ListView) getContentView().findViewById(R.id.item_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(actionItems.get(position), position);
                }
            }
        });
    }

    public void show(View view) {
        view.getLocationInWindow(mLocation);
        Log.i("show", "" + view.getWidth() + "1" + view.getHeight());
        mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(), mLocation[1] + view.getHeight());
        if (isNeedUpdate) {
            populateActions();
        }

        showAtLocation(view, popupgravity, mRect.left, mRect.bottom);
    }

    private void populateActions() {
        isNeedUpdate = false;
        //设置列表的适配器
        listView.setAdapter(new BaseAdapter() {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Holder holder=null;
                ActionItem item = actionItems.get(position);
                if (convertView == null) {
                    holder = new Holder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.menu_item, null);
                    holder.text = (TextView) convertView.findViewById(R.id.menu_text);
                    holder.icon = (ImageView) convertView.findViewById(R.id.menu_icon);
                    convertView.setTag(holder);
                }else {
                    holder = (Holder) convertView.getTag();
                }
                holder.text.setText(item.mTitle);
                holder.icon.setImageDrawable(item.mDrawable);
                return convertView;
            }

            class Holder {
                public ImageView icon;
                public TextView text;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public Object getItem(int position) {
                return actionItems.get(position);
            }

            @Override
            public int getCount() {
                return actionItems.size();
            }
        });


    }

    /**
     * 添加子类项
     */
    public void addAction(ActionItem action) {
        if (action != null) {
            actionItems.add(action);
            isNeedUpdate = true;
        }


    }

    /**
     * 清除子类项
     */
    public void cleanAction() {
        if (actionItems.isEmpty()) {
            actionItems.clear();
            isNeedUpdate = true;
        }
    }

    /**
     * 根据位置得到子类项
     */
    public ActionItem getAction(int position) {
        if (position < 0 || position > actionItems.size())
            return null;
        return actionItems.get(position);
    }

    /**
     * 设置监听事件
     */
    public void setItemOnClickListener(MyOnItemOnClickListener onItemOnClickListener) {
        this.onItemClickListener = onItemOnClickListener;
    }

    /**
     * @author yangyu
     *         功能描述：弹窗子类项按钮监听事件
     */
    public static interface MyOnItemOnClickListener {
        public void onItemClick(ActionItem item, int position);
    }

}
