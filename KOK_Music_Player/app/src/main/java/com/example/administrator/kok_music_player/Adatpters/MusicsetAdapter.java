package com.example.administrator.kok_music_player.Adatpters;

/**
 * Created by Administrator on 2016/5/7.
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.administrator.kok_music_player.MusicListActivity;
import com.example.administrator.kok_music_player.R;
import com.example.administrator.kok_music_player.Utils.fieldsutils.AlbumImageUrlFields;
import com.example.administrator.kok_music_player.Utils.fieldsutils.AlbumInfoFields;
import com.example.administrator.kok_music_player.Utils.fieldsutils.CommonFidlds;
import com.example.administrator.kok_music_player.Utils.fieldsutils.MusicSetImageFields;
import com.example.administrator.kok_music_player.Utils.fieldsutils.MusicSetInfoFields;
import com.example.administrator.kok_music_player.Utils.imageloader2.ImageDownLoader;
import com.example.administrator.kok_music_player.Utils.imageloader2.ImageDownLoader.onImageLoaderListener;

import java.util.ArrayList;
import java.util.Map;

public class MusicsetAdapter extends BaseAdapter implements OnScrollListener {
    /**
     * 上下文对象的引用
     */
    private Context context;

    /**
     * Image Url的数组
     */
    private String[] imageThumbUrls;

    /**
     * GridView对象的应用
     */
    private GridView mGridView;

    /**
     * Image 下载器
     */
    private ImageDownLoader mImageDownLoader;

    /**
     * 记录是否刚打开程序，用于解决进入程序不滚动屏幕，不会下载图片的问题。
     * 参考http://blog.csdn.net/guolin_blog/article/details/9526203#comments
     */
    private boolean isFirstEnter = true;

    /**
     * 一屏中第一个item的位置
     */
    private int mFirstVisibleItem;

    /**
     * 一屏中所有item的个数
     */
    private int mVisibleItemCount;

    private ArrayList<Map<String,String>> infos;





    public MusicsetAdapter(Context context, GridView mGridView, ArrayList<Map<String,String>> infos) {
        this.context = context;
        this.mGridView = mGridView;
        this.infos = infos;

        mImageDownLoader = new ImageDownLoader(context);
        mGridView.setOnScrollListener(this);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //仅当GridView静止时才去下载图片，GridView滑动时取消所有正在下载的任务
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            showImage(mFirstVisibleItem, mVisibleItemCount);
        } else {
            cancelTask();
        }

    }


    /**
     * GridView滚动的时候调用的方法，刚开始显示GridView也会调用此方法
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        mFirstVisibleItem = firstVisibleItem;
        mVisibleItemCount = visibleItemCount;
        // 因此在这里为首次进入程序开启下载任务。
        if (isFirstEnter && visibleItemCount > 0) {
            showImage(mFirstVisibleItem, mVisibleItemCount);
            isFirstEnter = false;
        }
    }


    @Override
    public int getCount() {
        return infos.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView mImageView;
        ViewHolder viewHolder ;
        final String mImageUrl = context.getString(R.string.server_url)+infos.get(position).get(MusicSetImageFields.MUSICSET_IMAGE_URL);
        String titleinfo = infos.get(position).get(MusicSetInfoFields.MUSICSET_TITLE);
        String collector = infos.get(position).get(MusicSetInfoFields.MUSICSET_COLLECTOR) ;
        final String musicsetid = infos.get(position).get(MusicSetInfoFields.MUSICSET_ID);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.musicset_item,null);
            viewHolder = new ViewHolder();
            viewHolder.linearLayout = (LinearLayout)convertView.findViewById(R.id.musicset1);
            viewHolder.album_image = (ImageView) convertView.findViewById(R.id.music_photo);
            viewHolder.album_collector = (TextView)convertView.findViewById(R.id.collector);
            viewHolder.album_title = (TextView)convertView.findViewById(R.id.title);
            convertView.setTag(viewHolder);
        } else {
           viewHolder = (ViewHolder)convertView.getTag();

        }


        viewHolder.album_title.setText(titleinfo);
        viewHolder.album_collector.setText(collector);

        //先获取屏幕的分辨率
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        //将分辨率转换成像素 ，并根据GridView的每个Item 的宽度 算出共有多少Item （宽度为80dp）
        float scale = context.getResources().getDisplayMetrics().density;
        //最后算出每个ImageView 的高和宽 的分辨率size
        int size = (width - 15) / 3;


        //设置正方形的ImageView的宽和高的分辨率 size
        viewHolder.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(size, size+100));
        viewHolder.album_image.setLayoutParams(new LinearLayout.LayoutParams(size,size));
        viewHolder.album_image.setScaleType(ImageView.ScaleType.FIT_XY);


        //给ImageView设置Tag,这里已经是司空见惯了
        viewHolder.album_image.setTag(mImageUrl);


        /*******************************去掉下面这几行试试是什么效果****************************/
        Log.i("test", "imagurl" + mImageUrl);
        Bitmap bitmap = mImageDownLoader.showCacheBitmap(mImageUrl.replaceAll("[^\\w]", ""));
        if (bitmap != null) {
            viewHolder.album_image.setImageBitmap(bitmap);
        } else {
            viewHolder.album_image.setImageDrawable(context.getResources().getDrawable(R.drawable.logo));
        }
        /**********************************************************************************/


        return convertView;
    }

    static class ViewHolder{
        ImageView album_image;
        TextView album_title;
        TextView album_collector;
        LinearLayout linearLayout;
    }
    /**
     * 显示当前屏幕的图片，先会去查找LruCache，LruCache没有就去sd卡或者手机目录查找，在没有就开启线程去下载
     *
     * @param firstVisibleItem
     * @param visibleItemCount
     */
    private void showImage(int firstVisibleItem, int visibleItemCount) {
        Bitmap bitmap = null;
        for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
            String mImageUrl = context.getString(R.string.server_url)+infos.get(i).get(MusicSetImageFields.MUSICSET_IMAGE_URL);
            final ImageView mImageView = (ImageView) mGridView.findViewWithTag(mImageUrl);
            bitmap = mImageDownLoader.downloadImage(mImageUrl, new onImageLoaderListener() {

                @Override
                public void onImageLoader(Bitmap bitmap, String url) {
                    if (mImageView != null && bitmap != null) {
                        mImageView.setImageBitmap(bitmap);
                    }

                }
            });

        }

    }

    /**
     * 取消下载任务
     */
    public void cancelTask() {
        mImageDownLoader.cancelTask();
    }


}
