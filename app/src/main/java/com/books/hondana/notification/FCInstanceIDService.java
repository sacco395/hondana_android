package com.books.hondana.notification;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.kii.cloud.storage.KiiCallback;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiPushCallBack;

public class FCInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = FCInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        FCMTokenStore.save(refreshedToken, this);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(final String token) {
        KiiUser.loginWithStoredCredentials(new KiiCallback<KiiUser>() {
            @Override
            public void onComplete(KiiUser kiiUser, Exception e) {
                if (e != null) {
                    Log.v(TAG, "Failed to login to Cloud or No user logged in.");
                    // Don't worry. Push will be installed when user sign up/in to Kii Cloud on MainActivity.
                    return;
                }
                KiiUser.pushInstallation().install(token, new KiiPushCallBack() {
                    @Override
                    public void onInstallCompleted(int taskId, @Nullable Exception e) {
                        if (e != null) {
                            Log.v(TAG, "Failed to Install push");
                            return;
                        }
                        Log.v(TAG, "Push Installation succeeded.");
                    }
                });
            }
        });
    }
}
