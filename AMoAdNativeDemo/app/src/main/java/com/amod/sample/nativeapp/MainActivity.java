package com.amod.sample.nativeapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.json.JSONObject;

public class MainActivity extends Activity implements View.OnClickListener {

    //TODO [SDK] 管理画面から取得したsidを入力してください
    private static final String SID1 = "62056d310111552c000000000000000000000000000000000000000000000000";
    private static final String TAG1 = "Ad01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.showBtn).setOnClickListener(this);
        findViewById(R.id.hideBtn).setOnClickListener(this);
        findViewById(R.id.startRoataionBtn).setOnClickListener(this);
        findViewById(R.id.stopRotationBtn).setOnClickListener(this);
        findViewById(R.id.reloadBtn).setOnClickListener(this);

        ViewGroup container = (ViewGroup) findViewById(R.id.container);

        //ネットワーク通信の制限時間を設定する
        com.amoad.Native.setNetworkTimeoutMillis(5000);//５秒

        // TODO 1.ロ広告をロードする
        com.amoad.Native.load(this, SID1, TAG1, new com.amoad.ResultListener() {
            @Override
            public void onResult(String sid, String tag, com.amoad.Result result, JSONObject info) {
                Log.d("TAG", String.format("onResult() sid=%s, tag=%s, result=%s, info=%s", sid, tag, result, info));
                switch (result) {
                    case Success:
                        Log.d("TAG", "広告取得成功");
                        break;
                    case Empty:
                        Log.d("TAG", "空広告");
                        break;
                    case Failure:
                        Log.d("TAG", "広告取得エラー");
                        break;
                }
            }
        });

        int widthDpi = 140;
        int heightDpi = 120;
        int widthPixel = dpiToPixel(widthDpi);
        int heightPixel = dpiToPixel(heightDpi);
        
        // TODO 2.広告ビューを画面に追加する
        container.addView(com.amoad.Native.getView(SID1, TAG1), new ViewGroup.LayoutParams(widthPixel, heightPixel));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TODO 3.広告Viewを解除する
        com.amoad.Native.disposeView(SID1, TAG1);
    }

    private void show() {
        com.amoad.Native.show(SID1, TAG1);
    }

    private void hide() {
        com.amoad.Native.hide(SID1, TAG1);
    }

    private void startRotation() {
        com.amoad.Native.startRotation(SID1, TAG1, 10);
    }

    private void stopRotation() {
        com.amoad.Native.stopRotation(SID1, TAG1);
    }

    private void reload() {
        com.amoad.Native.reload(SID1, TAG1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showBtn:
                show();
                break;
            case R.id.hideBtn:
                hide();
                break;
            case R.id.startRoataionBtn:
                startRotation();
                break;
            case R.id.stopRotationBtn:
                stopRotation();
                break;
            case R.id.reloadBtn:
                reload();
                break;
        }
    }

    private int dpiToPixel(int dpiValue) {
        return (int) (dpiValue * getDensity() + 0.5F);
    }

    private final float getDensity() {
        final WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }
}
