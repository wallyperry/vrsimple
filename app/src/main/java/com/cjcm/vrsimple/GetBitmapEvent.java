package com.cjcm.vrsimple;

import android.graphics.Bitmap;

/**
 * GetBitmapEvent
 *
 * @author perry
 * @date 2017/10/5
 */

public class GetBitmapEvent {
    private Bitmap bitmap;
    private String code;


    public GetBitmapEvent(Bitmap bitmap, String code) {
        this.bitmap = bitmap;
        this.code = code;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getCode() {
        return code;
    }
}
