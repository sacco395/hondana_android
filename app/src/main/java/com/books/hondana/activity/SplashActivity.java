package com.books.hondana.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.books.hondana.R;
import com.kii.cloud.storage.KiiCallback;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiUserCallBack;

public class SplashActivity extends Activity {

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // スプラッシュ用のビューを取得する
        setContentView(R.layout.activity_splash);

        //SharePreferences prefは自動ログインのため保存されているaccess tokenを読み出す。tokenがあればログインできる
        //紫色は定数
        SharedPreferences pref = getSharedPreferences(getString(R.string.save_data_name), Context.MODE_PRIVATE);
        String token = pref.getString(getString(R.string.save_token), "");//保存されていない時は""

        //tokenがないとき。
        if(token == "") {
            //画面を作る
            // 2秒したらMainActivityを呼び出してSplashActivityを終了する
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // StartActivityを呼び出す
                    Intent intent = new Intent(getApplicationContext(),
                            StartActivity.class);
                    startActivity(intent);
                    // SplashActivityを終了する
                    SplashActivity.this.finish();
                }
            }, 2 * 1000); // 2000ミリ秒後（2秒後）に実行
        }

        else {
            //自動ログインをする。
            KiiUser.loginWithStoredCredentials(new KiiCallback<KiiUser>() {
                @Override
                public void onComplete(KiiUser user, Exception exception) {
                    if (exception != null) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // 2秒したらBookMainActivityを呼び出してSplashActivityを終了する
                                // BoolMainActivityを呼び出す
                                Intent intent = new Intent(getApplicationContext(),
                                        BookMainActivity.class);
                                startActivity(intent);
                                // SplashActivityを終了する
                                SplashActivity.this.finish();
                            }
                        }, 2 * 1000); // 2000ミリ秒後（2秒後）に実行
                        // Error handling
                        return;
                    }
                    user.refresh(new KiiUserCallBack() {
                        @Override
                        public void onRefreshCompleted(int token, Exception exception) {
                            if (exception != null) {
                                // Error handling
                                return;
                            }
                        }
                    });
                }
            });
        }
    }
}