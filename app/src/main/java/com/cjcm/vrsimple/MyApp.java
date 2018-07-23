package com.cjcm.vrsimple;

import android.app.Application;

/**
 * Email: pl.w@outlook.com
 *
 * @author perry
 * @date 2017/9/8
 */

public class MyApp extends Application {
    private static MyApp mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mContext == null) {
            mContext = this;
        }
    }


    public static MyApp getContext() {
        return mContext;
    }
}
