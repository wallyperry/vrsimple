package com.cjcm.vrsimple;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ren.perry.perry.LoadingDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String defaultUrl = "http://192.168.5.12/pano_image/pano_reception.jpg";
    private VrPanoramaView panoView;
    private EditText etUrl;
    private Button btnGei;

    private ImageLoaderTask loaderTask;
    private LoadingDialog loadingDialog;

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(GetBitmapEvent event) {
        switch (event.getCode()) {
            case "online":
                loaderTask = new ImageLoaderTask(event.getBitmap());
                loaderTask.execute();
                break;
            case "showLoading":
                loadingDialog.show();
                break;
            case "cancelLoading":
                loadingDialog.dismiss();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        panoView = findViewById(R.id.panoView);
        etUrl = findViewById(R.id.etUrl);
        btnGei = findViewById(R.id.btnGet);

        loadingDialog = new LoadingDialog(this, "加载中，请稍后...");
        loadingDialog.setNotCancel();

        panoView.setEventListener(new VrEventListener());
        etUrl.setText(defaultUrl);
        btnGei.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGet:
                EventBus.getDefault().post(new GetBitmapEvent(null, "showLoading"));
                String url = etUrl.getText().toString().trim();
                BitmapService.startService(this, url.length() < 1 ? defaultUrl : url, "online");
                break;
        }
    }

    @Override
    protected void onPause() {
        panoView.pauseRendering();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        panoView.resumeRendering();
    }

    @Override
    protected void onDestroy() {
        panoView.shutdown();

        if (loaderTask != null) {
            loaderTask.cancel(true);
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @SuppressLint("StaticFieldLeak")
    class ImageLoaderTask extends AsyncTask<Void, Void, Void> {
        private Bitmap bitmap;

        ImageLoaderTask(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            VrPanoramaView.Options options = new VrPanoramaView.Options();
            options.inputType = VrPanoramaView.Options.TYPE_MONO;
            panoView.loadImageFromBitmap(bitmap, options);
            return null;
        }
    }

    class VrEventListener extends VrPanoramaEventListener {
        @Override
        public void onLoadSuccess() {
            Log.e("MainActivity", "onLoadSuccess:加载成功啦");
            EventBus.getDefault().post(new GetBitmapEvent(null, "cancelLoading"));
        }

        @Override
        public void onLoadError(String errorMessage) {
            Log.e("MainActivity", "onLoadError:加载失败：" + errorMessage);
            Toast.makeText(MainActivity.this, "加载失败：" + errorMessage, Toast.LENGTH_LONG).show();
            EventBus.getDefault().post(new GetBitmapEvent(null, "cancelLoading"));
        }
    }

}