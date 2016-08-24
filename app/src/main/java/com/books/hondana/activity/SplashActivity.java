package com.books.hondana.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.books.hondana.R;

public class SplashActivity extends AppCompatActivity {

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // スプラッシュ用のビューを取得する
        setContentView(R.layout.activity_splash);

        // 2秒したらMainActivityを呼び出してSplashActivityを終了する
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // MainActivityを呼び出す
                Intent intent = new Intent(getApplicationContext(),
                        BookMainActivity.class);
                startActivity(intent);
                // SplashActivityを終了する
                SplashActivity.this.finish();
            }
        }, 2 * 1000); // 2000ミリ秒後（2秒後）に実行
    }
}

