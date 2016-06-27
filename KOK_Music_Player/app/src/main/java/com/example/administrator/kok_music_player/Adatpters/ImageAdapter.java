package com.example.administrator.kok_music_player.Adatpters;

/**
 * Created by Administrator on 2016/5/7.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.kok_music_player.Utils.imageloader.ImageDownLoader;
import com.example.administrator.kok_music_player.customview.CImageView;
import com.example.administrator.kok_music_player.R;
import com.example.administrator.kok_music_player.Utils.musicutils.MusicInfo;

import java.util.List;


public class ImageAdapter extends BaseAdapter implements OnScrollListener {
    /**
     * 上下文对象的引用
     */
    private Context context;

    /**
     * Image Url的数组
     */
    private String[] imageThumbUrls;
    private List musicInfos;

    /**
     * ListView对象的应用
     */
    private ListView mListView;

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





    public ImageAdapter(Context context, ListView mListView, List musicInfos) {
        this.context = context;
        this.mListView = mListView;
        this.musicInfos = musicInfos;

        mImageDownLoader = new ImageDownLoader(context);
        mListView.setOnScrollListener(this);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //仅当ListView静止时才去下载图片，ListView滑动时取消所有正在下载的任务
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            showImage(mFirstVisibleItem, mVisibleItemCount);
        } else {
            cancelTask();
        }

    }


    /**
     * ListView滚动的时候调用的方法，刚开始显示ListView也会调用此方法
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
        return musicInfos.size();
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

        MusicInfo mp3Info = (MusicInfo) musicInfos.get(position);
        String musicName;
        String songerName;
        String album;
        if (mp3Info.getAlbum() != null || !mp3Info.getAlbum().equals("")) {
            album = "- <" + mp3Info.getAlbum() + ">";
        } else {
            album = "- <未知>";
        }
        if (mp3Info.getArtist() != null || !mp3Info.getArtist().equals("")) {
            songerName = "<" + mp3Info.getArtist() + "> ";
        } else {
            songerName = "<未知> ";
        }

        if (mp3Info.getTitle() == null || !mp3Info.getTitle().equals("")) {
            musicName = mp3Info.getTitle();
        } else {
            musicName = "未知";
        }

//        Bitmap picture = pictureUtil.getArtwork(context, mp3Info.getId(), mp3Info.getAlbumId(), true);


        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.music_info_item, parent, false);
            holder = new ViewHolder();
            holder.songerIcon_view = (CImageView) convertView.findViewById(R.id.songerIcon);
            holder.songerInfo_view = (TextView) convertView.findViewById(R.id.songer_info);
            holder.music_Name_view = (TextView) convertView.findViewById(R.id.music_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.songerIcon_view.setTag(mp3Info.getId());
        holder.songerInfo_view.setText(songerName + album);
        holder.music_Name_view.setText(musicName);


        /*******************************去掉下面这几行试试是什么效果****************************/
        Bitmap bitmap = mImageDownLoader.showCacheBitmap(String.valueOf(mp3Info.getAlbumId()));

        if (bitmap != null) {
            holder.songerIcon_view.setImageBitmap(bitmap);
        } else {
            holder.songerIcon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.logo));
        }
        /**********************************************************************************/


        return convertView;
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
            MusicInfo musicInfo = (MusicInfo) musicInfos.get(i);
            final CImageView mImageView = (CImageView) mListView.findViewWithTag(musicInfo.getId());
            bitmap = mImageDownLoader.downloadImage(musicInfo.getId(),musicInfo.getAlbumId(), new ImageDownLoader.onImageLoaderListener() {

                @Override
                public void onImageLoader(Bitmap bitmap, String url) {
                    if (mImageView != null && bitmap != null) {
                        mImageView.setImageBitmap(bitmap);
                    }

                }
            });


        }
    }
    static class ViewHolder {
        CImageView songerIcon_view;
        TextView music_Name_view;
        TextView songerInfo_view;
//        ImageButton manager_btn ;
    }

    /**
     * 取消下载任务
     */
    public void cancelTask() {
        mImageDownLoader.cancelTask();
    }


}
