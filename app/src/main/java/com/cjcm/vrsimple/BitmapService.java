package com.cjcm.vrsimple;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

public class BitmapService extends IntentService {

    public BitmapService() {
        super("");
    }

    public static void startService(Context context, String imgUrl, String code) {
        Intent intent = new Intent(context, BitmapService.class);
        intent.putExtra("imgUrl", imgUrl);
        intent.putExtra("code", code);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }
        String imageUrl = intent.getStringExtra("imgUrl");
        String code = intent.getStringExtra("code");
        handleImage(imageUrl, code);
    }

    private void handleImage(String url, String code) {
        if (!url.isEmpty()) {
            Bitmap bitmap = ImageUtils.getBitmap(url);
            EventBus.getDefault().post(new GetBitmapEvent(bitmap, code));
        }
    }
}