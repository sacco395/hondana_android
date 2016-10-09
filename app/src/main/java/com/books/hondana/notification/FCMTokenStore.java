package com.books.hondana.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiPushCallBack;

public class FCMTokenStore {

    private static final String TAG = FCMTokenStore.class.getSimpleName();

    private static final String PREF_NAME = "fcm";

    private static final String KEY_FCM_TOKEN = "fcm_token";

    public static void save(String token, Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        pref.edit().putString(KEY_FCM_TOKEN, token).apply();
    }

    @Nullable
    public static String get(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String token = pref.getString(KEY_FCM_TOKEN, "");
        if (token.isEmpty()) {
            return null;
        }
        return token;
    }

    public static void postTokenToKiiCloud(Context context, final PostTokenCallback callback) {
        String token = get(context);
        if (token == null) {
            return;
        }
        KiiUser.pushInstallation().install(token, new KiiPushCallBack() {
            @Override
            public void onInstallCompleted(int taskId, @Nullable Exception e) {
                super.onInstallCompleted(taskId, e);
                if (e != null) {
                    callback.failure(e);
                    return;
                }
                callback.success();
            }
        });
    }

    public interface PostTokenCallback {
        void success();
        void failure(Exception e);
    }
}
