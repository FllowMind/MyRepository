package com.example.administrator.kok_music_player.Adatpters;

/**
 * Created by Administrator on 2016/5/7.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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

import com.example.administrator.kok_music_player.R;
import com.example.administrator.kok_music_player.Utils.fieldsutils.ArtistImageUrlFields;
import com.example.administrator.kok_music_player.Utils.fieldsutils.ArtistInfoFields;
import com.example.administrator.kok_music_player.Utils.imageloader2.ImageDownLoader;
import com.example.administrator.kok_music_player.Utils.imageloader2.ImageDownLoader.onImageLoaderListener;

import java.util.ArrayList;
import java.util.Map;

public class ArtistAdapter extends BaseAdapter implements OnScrollListener {
    /**
     * 上下文对象的引用
     */
    private Context context;


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





    public ArtistAdapter(Context context, GridView mGridView, ArrayList<Map<String,String>> infos) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView mImageView;
        ViewHolder viewHolder ;
        final String mImageUrl = context.getString(R.string.server_url)+infos.get(position).get(ArtistImageUrlFields.ARTIST_IMAGE_URL);
        String artistname = infos.get(position).get(ArtistInfoFields.ARTIST_NAME);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.artist_item,null);
            viewHolder = new ViewHolder();
            viewHolder.linearLayout = (LinearLayout)convertView.findViewById(R.id.artist);
            viewHolder. artist_image = (ImageView) convertView.findViewById(R.id.artist_photo);
            viewHolder.artist_name = (TextView)convertView.findViewById(R.id.artist_name);
            convertView.setTag(viewHolder);
        } else {
           viewHolder = (ViewHolder)convertView.getTag();

        }

        Log.i("test", "name"+artistname);
        viewHolder.artist_name.setText(artistname);

      //  实现点击图片变暗效果
        viewHolder. artist_image.setClickable(true);
        viewHolder. artist_image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView imageView = (ImageView) v;
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:

                        Drawable drawable=imageView.getDrawable();
                        //在按下事件中设置滤镜
                        //当src图片为Null，获取背景图片
                        if (drawable==null) {
                            drawable=imageView.getBackground();
                        }
                        if(drawable!=null){
                            //设置滤镜
                            drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        Log.i("test","up");
                        //由于捕获了Touch事件，需要手动触发Click事件

                        Drawable drawable1=imageView.getDrawable();
                        //在按下事件中设置滤镜
                        //当src图片为Null，获取背景图片
                        if (drawable1==null) {
                            drawable1=imageView.getBackground();
                        }if(drawable1!=null){
                        Log.i("test","up");
                        //清除滤镜
                        drawable1.clearColorFilter();

                    }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        Log.i("test","cancel");
                        //在CANCEL和UP事件中清除滤镜
                        //由于捕获了Touch事件，需要手动触发Click事件
                        Drawable drawable2=imageView.getDrawable();
                        //在按下事件中设置滤镜
                        //当src图片为Null，获取背景图片
                        if (drawable2==null) {
                            drawable2=imageView.getBackground();
                        }if(drawable2!=null){
                        Log.i("test","cancel");
                        //清除滤镜
                        drawable2.clearColorFilter();

                    }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        //先获取屏幕的分辨率
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        //将分辨率转换成像素 ，并根据GridView的每个Item 的宽度 算出共有多少Item （宽度为80dp）
        float scale = context.getResources().getDisplayMetrics().density;
        //最后算出每个ImageView 的高和宽 的分辨率size
        int size = (width - 15) / 3;


        //设置正方形的ImageView的宽和高的分辨率 size
        viewHolder.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(size, size+100));
        viewHolder. artist_image.setLayoutParams(new LinearLayout.LayoutParams(size,size));
        viewHolder. artist_image.setScaleType(ImageView.ScaleType.FIT_XY);


        //给ImageView设置Tag,这里已经是司空见惯了
        viewHolder. artist_image.setTag(mImageUrl);


        /*******************************去掉下面这几行试试是什么效果****************************/
        Bitmap bitmap = mImageDownLoader.showCacheBitmap(mImageUrl.replaceAll("[^\\w]", ""));
        if (bitmap != null) {
            viewHolder. artist_image.setImageBitmap(bitmap);
        } else {
//            viewHolder.album_image.setImageDrawable(context.getResources().getDrawable(R.drawable.loading));
        }
        /**********************************************************************************/


        return convertView;
    }

    static class ViewHolder{
        ImageView artist_image;
        TextView artist_name;
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
            String mImageUrl = context.getString(R.string.server_url)+infos.get(i).get(ArtistImageUrlFields.ARTIST_IMAGE_URL);
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
