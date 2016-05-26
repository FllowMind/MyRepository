package com.example.administrator.kok_music_player.Utils.imageloader;

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

import java.util.HashMap;
import java.util.Map;

import edu.jyu.apts.android.R;
import edu.jyu.apts.android.utils.imageloader.ImageDownLoader.onImageLoaderListener;

public class ImageAdapter extends BaseAdapter implements OnScrollListener {
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





    public ImageAdapter(Context context, GridView mGridView, String[] imageThumbUrls) {
        this.context = context;
        this.mGridView = mGridView;
        this.imageThumbUrls = imageThumbUrls;

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
        return imageThumbUrls.length;
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
        final String mImageUrl = imageThumbUrls[position];
        if (convertView == null) {
            mImageView = new ImageView(context);
            Log.i("test", "postion" + position);

        } else {
            mImageView = (ImageView) convertView;

        }

        //点击图片查看大图片
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_VIEW);
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri mUri = Uri.parse(mImageUrl);
                it.setDataAndType(mUri, "image/*");
                context.startActivity(it);
            }
        });

      //  实现点击图片变暗效果
//        mImageView.setClickable(true);
        mImageView.setOnTouchListener(new View.OnTouchListener() {
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
        int count = (int) ((width - 15) / scale + 0.5f) / 80;
        //最后算出每个ImageView 的高和宽 的分辨率size
        int size = (width - 15) / count;


        //设置正方形的ImageView的宽和高的分辨率 size
        mImageView.setLayoutParams(new GridView.LayoutParams(size, size));
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);


        //给ImageView设置Tag,这里已经是司空见惯了
        mImageView.setTag(mImageUrl);


        /*******************************去掉下面这几行试试是什么效果****************************/
        Bitmap bitmap = mImageDownLoader.showCacheBitmap(mImageUrl.replaceAll("[^\\w]", ""));
        if (bitmap != null) {
            mImageView.setImageBitmap(bitmap);
        } else {
            mImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.loading));
        }
        /**********************************************************************************/


        return mImageView;
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
            String mImageUrl = imageThumbUrls[i];
            final ImageView mImageView = (ImageView) mGridView.findViewWithTag(mImageUrl);
            bitmap = mImageDownLoader.downloadImage(mImageUrl, new onImageLoaderListener() {

                @Override
                public void onImageLoader(Bitmap bitmap, String url) {
                    if (mImageView != null && bitmap != null) {
                        mImageView.setImageBitmap(bitmap);
                    }

                }
            });

            //if(bitmap != null){
            //  mImageView.setImageBitmap(bitmap);
            //}else{
            //  mImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_empty));
            //}
        }
    }

    /**
     * 取消下载任务
     */
    public void cancelTask() {
        mImageDownLoader.cancelTask();
    }


}
