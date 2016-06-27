package com.example.administrator.kok_music_player.Utils.pictureutil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2016/6/18.
 */
public class BitmapUtil {

    private BitmapUtil (){

    }


    /**
     * 压缩图片大小和质量
     * @param bitmap
     * @param newwidth
     * @param newheight
     * @return
     */
    public static Bitmap compressScale(Bitmap bitmap, int newwidth, int newheight){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scalewidth = newwidth/width;
        float scaleheight = newheight/height;
        Matrix matrix = new Matrix();
        matrix.postScale(scalewidth, scaleheight);
        bitmap = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
        return compressImage(bitmap);
    }

    //压缩图片
    public static Bitmap getimage(String imagepath) {

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imagepath, newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //将图片压缩成高度和宽度分别为imageview的高度和宽度的两倍，以获得较清晰的图片
        float hh = 320;//这里设置高度为180f
        float ww = 320;//这里设置宽度为180f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(imagepath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }


    private static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}
