package com.example.administrator.kok_music_player.Utils.imageloader;

/**
 * Created by Administrator on 2016/5/7.
 */
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.example.administrator.kok_music_player.R;


public class ImageDownLoader {
    /**
     * 缓存Image的类，当存储Image的大小大于LruCache设定的值，系统自动释放内存
     */
    private LruCache<String, Bitmap> mMemoryCache;
    /**
     * 操作文件相关类对象的引用
     */
    private FileUtils fileUtils;
    /**
     * 下载Image的线程池
     */
    private ExecutorService mImageThreadPool = null;

    private Context context;
    private final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
    private final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
    private  Bitmap mCachedBit = null;





    public ImageDownLoader(Context context){

        //获取系统分配给每个应用程序的最大内存，每个应用系统分配32M
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int mCacheSize = maxMemory / 8;
        //给LruCache分配1/8 4M
        mMemoryCache = new LruCache<String, Bitmap>(mCacheSize){

            //必须重写此方法，来测量Bitmap的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }

        };

        this.context = context;
        fileUtils = new FileUtils(context);
    }


    /**
     * 获取线程池的方法，因为涉及到并发的问题，我们加上同步锁
     * @return
     */
    public ExecutorService getThreadPool(){
        if(mImageThreadPool == null){
            synchronized(ExecutorService.class){
                if(mImageThreadPool == null){
                    //为了下载图片更加的流畅，我们用了2个线程来下载图片
                    mImageThreadPool = Executors.newFixedThreadPool(2);
                }
            }
        }

        return mImageThreadPool;

    }

    /**
     * 添加Bitmap到内存缓存
     * @param key
     * @param bitmap
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null && bitmap != null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 从内存缓存中获取一个Bitmap
     * @param key
     * @return
     */
    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 先从内存缓存中获取Bitmap,如果没有就从SD卡或者手机缓存中获取，SD卡或者手机缓存
     * 没有就去下载
     * @param url
     * @param listener
     * @return
     */
    public Bitmap downloadImage(final String url, final onImageLoaderListener listener){
        //替换Url中非字母和非数字的字符，这里比较重要，因为我们用Url作为文件名，比如我们的Url
        //是Http://xiaanming/abc.jpg;用这个作为图片名称，系统会认为xiaanming为一个目录，
        //我们没有创建此目录保存文件就会报错
        final String subUrl = url.replaceAll("[^\\w]", "");
        Bitmap bitmap = showCacheBitmap(subUrl);
        if(bitmap != null){
            return bitmap;
        }else{

            final Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    listener.onImageLoader((Bitmap)msg.obj, url);
                }
            };

            getThreadPool().execute(new Runnable() {

                @Override
                public void run() {
                    Bitmap bitmap = getBitmapFormUrl(url);
                    Message msg = handler.obtainMessage();
                    msg.obj = bitmap;
                    handler.sendMessage(msg);

                    try {
                        //保存在SD卡或者手机目录
                        fileUtils.savaBitmap(subUrl, bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i("test", subUrl);

                    //将Bitmap 加入内存缓存
                    addBitmapToMemoryCache(subUrl, bitmap);
                }
            });
        }

        return null;
    }

    public Bitmap downloadImage(final long songid,final long albumid, final onImageLoaderListener listener){
        //替换Url中非字母和非数字的字符，这里比较重要，因为我们用Url作为文件名，比如我们的Url
        //是Http://xiaanming/abc.jpg;用这个作为图片名称，系统会认为xiaanming为一个目录，
        //我们没有创建此目录保存文件就会报错
        final String subUrl = String.valueOf(albumid);
        Bitmap bitmap = showCacheBitmap(subUrl);
        if(bitmap != null){
            return bitmap;
        }else{

            final Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    listener.onImageLoader((Bitmap)msg.obj, subUrl);
                }
            };

            getThreadPool().execute(new Runnable() {

                @Override
                public void run() {
                    Bitmap bitmap = getBitmapFromNative(songid,albumid);
                    Message msg = handler.obtainMessage();
                    msg.obj = bitmap;
                    handler.sendMessage(msg);

                    try {
                        //保存在SD卡或者手机目录
                        fileUtils.savaBitmap(subUrl, bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i("test", subUrl);

                    //将Bitmap 加入内存缓存
                    addBitmapToMemoryCache(subUrl, bitmap);
                }
            });
        }

        return null;
    }

    /**
     * 获取Bitmap, 内存中没有就去手机或者sd卡中获取，这一步在getView中会调用，比较关键的一步
     * @param url
     * @return
     */
    public Bitmap showCacheBitmap(String url){

        if(getBitmapFromMemCache(url) != null){
            Log.i("test", "get");
            return getBitmapFromMemCache(url);
        }else if(fileUtils.isFileExists(url) && fileUtils.getFileSize(url) != 0){
            //从SD卡获取手机里面获取Bitmap

            Bitmap bitmap = fileUtils.getimage(url);

            //将Bitmap 加入内存缓存
            addBitmapToMemoryCache(url, bitmap);
            return getBitmapFromMemCache(url);
        }

        return null;
    }







    /**
     * 从Url中获取Bitmap
     * @param url
     * @return
     */
    private Bitmap getBitmapFormUrl(String url) {
        Bitmap bitmap = null;
        HttpURLConnection con = null;
        try {
            URL mImageUrl = new URL(url);
            con = (HttpURLConnection) mImageUrl.openConnection();
            con.setConnectTimeout(10 * 1000);
            con.setReadTimeout(10 * 1000);
            con.setDoInput(true);
            con.setDoOutput(true);
            bitmap = BitmapFactory.decodeStream(con.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return bitmap;
    }

    private Bitmap getBitmapFromNative(long songid,long albumid){

        return getArtwork(songid, albumid);
    }

    /**
     * 取消正在下载的任务
     */
    public synchronized void cancelTask() {
        if(mImageThreadPool != null){
            mImageThreadPool.shutdownNow();
            mImageThreadPool = null;
        }
    }


    /**
     * 异步下载图片的回调接口
     * @author len
     *
     */
    public interface onImageLoaderListener{
        void onImageLoader(Bitmap bitmap, String url);
    }


    public Bitmap getArtwork(long song_id, long album_id) {
        boolean allowdefault = true;
        if (album_id < 0) {
            // This is something that is not in the database, so get the album art directly
            // from the file.
            if (song_id >= 0) {
                getArtworkFromFile(context, song_id, -1);
                Bitmap bm =  getArtworkFromFile(context, song_id, -1);;
                if (bm != null) {
                    return bm;
                }
            }
            if (allowdefault) {
                return getDefaultArtwork(context);
            }
            return null;
        }
        ContentResolver res = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
        if (uri != null) {
            InputStream in = null;
            try {
                in = res.openInputStream(uri);
                return BitmapFactory.decodeStream(in, null, sBitmapOptions);
            } catch (FileNotFoundException ex) {
                // The album art thumbnail does not actually exist. Maybe the user deleted it, or
                // maybe it never existed to begin with.
                Bitmap bm = getArtworkFromFile(context, song_id, album_id);
                if (bm != null) {
                    if (bm.getConfig() == null) {
                        bm = bm.copy(Bitmap.Config.RGB_565, false);
                        if (bm == null && allowdefault) {
                            return getDefaultArtwork(context);
                        }
                    }
                } else if (allowdefault) {
                    bm = getDefaultArtwork(context);
                }
                return bm;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException ex) {
                }
            }
        }

        return null;
    }

    private Bitmap getArtworkFromFile(Context context, long songid, long albumid) {
            Bitmap bm = null;
            byte [] art = null;
            String path = null;
            if (albumid < 0 && songid < 0) {
                throw new IllegalArgumentException("Must specify an album or a song id");
            }
            try {
                if (albumid < 0) {
                    Uri uri = Uri.parse("content://media/external/audio/media/" + songid + "/albumart");
                    ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                    if (pfd != null) {
                        FileDescriptor fd = pfd.getFileDescriptor();
                        bm = BitmapFactory.decodeFileDescriptor(fd);
                    }
                } else {
                    Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
                    ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                    if (pfd != null) {
                        FileDescriptor fd = pfd.getFileDescriptor();
                        bm = BitmapFactory.decodeFileDescriptor(fd);
                    }
                }
            } catch (FileNotFoundException ex) {

            }
            if (bm != null) {
                mCachedBit = bm;
            }


        return bm;
    }

    private static Bitmap getDefaultArtwork(Context context) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeStream(
                context.getResources().openRawResource(R.drawable.logo), null, opts);
    }

}