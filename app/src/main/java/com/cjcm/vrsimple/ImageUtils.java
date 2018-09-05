package com.cjcm.vrsimple;

import android.graphics.Bitmap;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Email: pl.w@outlook.com
 *
 * @author perry
 * @date 2017/9/16
 */

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ImageUtils {

    public static Bitmap getBitmap(String url) {
        try {
            return Glide.with(MyApp.getContext())
                    .load(url)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把bitmap转化为file
     */
    public static File bitMap2File(Bitmap bitmap) {
        String path = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            //保存到sd根目录下
            path = Environment.getExternalStorageDirectory() + File.separator;
        }
        File f = new File(path, System.currentTimeMillis() + ".jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            bitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

}
