package com.example.administrator.kok_music_player.Utils.pictureutil;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.LruCache;

import com.example.administrator.kok_music_player.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/5/25.
 */
public class PictureUtil {
    private LruCache<String, Bitmap> lruCaches;
    private Context context;
    private final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
    private final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
    private  Bitmap mCachedBit = null;


    public PictureUtil(Context context) {
        this.context = context;
        //获取系统分配给每个应用程序的最大内存，每个应用系统分配32M
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int mCacheSize = maxMemory / 8;
        //给LruCache分配1/8 4M

        lruCaches = new LruCache<String, Bitmap>(mCacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    public Bitmap getArtwork(Context context, long song_id, long album_id,
                                    boolean allowdefault) {
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
        if (getBitmapFromCaches(String.valueOf(albumid)) == null) {
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
                saveBitmapToCaches(String.valueOf(albumid),compressBitmap(bm,(float) 0.2));
            }


        }

        return getBitmapFromCaches(String.valueOf(albumid));
    }

    public Bitmap compressBitmap(Bitmap bitmap, float size) {
        if (bitmap != null) {


            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);

            float zoom = (float)Math.sqrt(size * 1024 / (float)out.toByteArray().length);

            Matrix matrix = new Matrix();
            matrix.setScale(zoom, zoom);

            Bitmap resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            out.reset();
            resultBitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
            while(out.toByteArray().length > size * 1024){
                System.out.println(out.toByteArray().length);
                matrix.setScale(0.9f, 0.9f);
                resultBitmap = Bitmap.createBitmap(resultBitmap, 0, 0, resultBitmap.getWidth(), resultBitmap.getHeight(), matrix, true);
                out.reset();
                resultBitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
            }
            return resultBitmap;   }

        return null;

    }

    private static Bitmap compressImage(Bitmap image) {

        if (image != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;
            while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset();//重置baos即清空baos
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
                options -= 10;//每次都减少10
            }
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
            Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
            return bitmap;
        }

        return null;
    }

    private static Bitmap getDefaultArtwork(Context context) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeStream(
                context.getResources().openRawResource(R.drawable.logo), null, opts);
    }

    public Bitmap getCacheBitmap(String key1,Bitmap bitmap1) {
          final  String key = key1;
          final Bitmap bitmap = bitmap1;
        if (getBitmapFromCaches(key)== null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap nBitmap = compressBitmap(bitmap,(float) 0.1);
                    saveBitmapToCaches(key,nBitmap);
                }
            }).start();

        }
        return getBitmapFromCaches(key);
    }



    public void saveBitmapToCaches(String key, Bitmap bitmap) {
        lruCaches.put(key, bitmap);
    }

    public Bitmap getBitmapFromCaches(String key) {
        return lruCaches.get(key);
    }
}
