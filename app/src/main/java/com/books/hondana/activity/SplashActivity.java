package com.books.hondana.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.books.hondana.login.LoginActivity;
import com.books.hondana.start.StartActivity;
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

        setContentView(R.layout.activity_splash);

        SharedPreferences pref = getSharedPreferences(getString(R.string.save_data_name), Context.MODE_PRIVATE);
        final String token = pref.getString(getString(R.string.save_token), "");//保存されていない時は""

        if(token.equals("")){

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(),
                            StartActivity.class);
                    startActivity(intent);

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
                                    Toast.makeText(SplashActivity.this, "再度ログインしてください",
                                            Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(getApplicationContext(),
                                            LoginActivity.class);
                                    startActivity(intent);
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
                        Intent intent = new Intent(getApplicationContext(),
                                BookMainActivity.class);
                        startActivity(intent);
//                        LogUtil.e(TAG, "onLoginCompleted: 自動ログインできません", exception);
//
//                        Toast.makeText(SplashActivity.this, "ホンダナからのお知らせ\n通信状態を確認の上、もう一度お試しください",
//                                Toast.LENGTH_LONG).show();
//
//                        finish();
                    }
                }
            }, token);
        }
    }
}