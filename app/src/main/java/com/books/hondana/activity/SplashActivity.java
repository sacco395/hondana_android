package com.books.hondana.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import com.books.hondana.util.LogUtil;

import com.books.hondana.R;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiUserCallBack;

public class SplashActivity extends Activity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // スプラッシュ用のビューを取得する
        setContentView(R.layout.activity_splash);

        //SharePreferences prefは自動ログインのため保存されているaccess tokenを読み出す。tokenがあればログインできる
        //紫色は定数
        SharedPreferences pref = getSharedPreferences(getString(R.string.save_data_name), Context.MODE_PRIVATE);
        final String token = pref.getString(getString(R.string.save_token), "");//保存されていない時は""

        //tokenがないとき。
        if(token.equals("")){
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
            KiiUser.loginWithToken(new KiiUserCallBack() {
                @Override
                public void onLoginCompleted(int token, @Nullable KiiUser user, @Nullable Exception exception) {
                    super.onLoginCompleted(token, user, exception);
                    if (user != null) {
                        user.refresh(new KiiUserCallBack() {
                            @Override
                            public void onRefreshCompleted(int token, Exception exception) {
                                if (exception != null) {
                                    // Error handling
                                    LogUtil.e(TAG, "onRefreshCompleted: ", exception);
                                    return;
                                }
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 2秒したらBookMainActivityを呼び出してSplashActivityを終了する
                                        // BookMainActivityを呼び出す
                                        Intent intent = new Intent(getApplicationContext(),
                                                BookMainActivity.class);
                                        startActivity(intent);
                                        // SplashActivityを終了する
                                        SplashActivity.this.finish();
                                    }
                                }, 2 * 1000); // 2000ミリ秒後（2秒後）に実行
                            }
                        });
                    } else {
                        LogUtil.e(TAG, "onLoginCompleted: ", exception);
                    }
                }
            }, token);
        }
    }
}