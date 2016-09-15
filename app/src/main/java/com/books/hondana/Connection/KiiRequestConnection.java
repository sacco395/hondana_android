package com.books.hondana.Connection;

import android.support.annotation.Nullable;
import android.util.Log;

import com.books.hondana.Model.Book;
import com.books.hondana.Model.Request;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiQueryCallBack;
import com.kii.cloud.storage.query.KiiClause;
import com.kii.cloud.storage.query.KiiQuery;
import com.kii.cloud.storage.query.KiiQueryResult;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/15/16.
 */
public class KiiRequestConnection {

    private static final String TAG = KiiRequestConnection.class.getSimpleName();

    private KiiRequestConnection() {
        throw new RuntimeException("Do not instantiate this Class!");
    }

    public static void fetchUserRequests(String userId, final KiiObjectListCallback<Request> callback) {
        KiiClause clause = KiiClause.equals(Request.CLIENT_ID, userId);
        KiiQuery clientIdQuery = new KiiQuery(clause);
        KiiBucket requestBucket = Kii.bucket(Request.BUCKET_NAME);
        requestBucket.query(new KiiQueryCallBack<KiiObject>() {
            @Override
            public void onQueryCompleted(int token, @Nullable KiiQueryResult<KiiObject> result, @Nullable Exception e) {
                // Error
                if (result == null || result.getResult() == null) {
                    Log.e(TAG, "onQueryCompleted: ", e);
                    callback.failure(e);
                    return;
                }
                // Success
                try {
                    List<Request> requests = convert(result.getResult());
                    callback.success(token, requests);
                } catch (JSONException e1) {
                    Log.e(TAG, "onQueryCompleted: ", e1);
                    callback.failure(e);
                }
            }
        }, clientIdQuery);
    }

    private static List<Request> convert(List<KiiObject> requestObjects) throws JSONException {
        List<Request> requests = new ArrayList<>();
        for (KiiObject object : requestObjects) {
            requests.add(Request.createFrom(object));
        }
        return requests;
    }
}
