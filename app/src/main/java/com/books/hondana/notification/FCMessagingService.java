package com.books.hondana.notification;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMessagingService extends FirebaseMessagingService {

    private static final String TAG = FCMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived: " + remoteMessage);

        // TODO: 10/9/16 NotificationCompat.Builder で実際の通知を作成
    }
}
