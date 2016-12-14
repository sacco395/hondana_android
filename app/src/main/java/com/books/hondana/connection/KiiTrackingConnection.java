package com.books.hondana.connection;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.books.hondana.model.Tracking;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiObjectCallBack;
import com.kii.cloud.storage.callback.KiiQueryCallBack;
import com.kii.cloud.storage.query.KiiClause;
import com.kii.cloud.storage.query.KiiQuery;
import com.kii.cloud.storage.query.KiiQueryResult;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * KiiCloud 上の Tracking を取得するためのクラス
 * インスタンス化せずに クラスメソッドを使う
 */
public class KiiTrackingConnection {

    private static final String TAG = KiiTrackingConnection.class.getSimpleName();

    /**
     * インスタンス化厳禁
     */
    private KiiTrackingConnection() {
        throw new RuntimeException("Do not instantiate this Class!");
    }

    /**
     * KiiQuery を元に、Tracking のバケツを検索、リストを取得
     * @param query 検索条件
     * @param callback
     */
    private static void queryTrackingBucket(KiiQuery query, final KiiObjectListCallback<Tracking> callback) {
        KiiBucket trackingBucket = Kii.bucket(Tracking.BUCKET_NAME);
        trackingBucket.query(new KiiQueryCallBack<KiiObject>() {
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
                    List<Tracking> trackings = convert(result.getResult());
                    callback.success(token, trackings);
                } catch (JSONException e1) {
                    Log.e(TAG, "onQueryCompleted: ", e1);
                    callback.failure(e);
                }
            }
        }, query);
    }

    private static List<Tracking> convert(List<KiiObject> trackingObjects) throws JSONException {
        List<Tracking> trackings = new ArrayList<>();
        for (KiiObject object : trackingObjects) {
            trackings.add(Tracking.createFrom(object));
        }
        return trackings;
    }


    public static void fetchTrackingByBookId(final String bookId, final String userId, final KiiObjectCallback<Tracking> callback) {
        final KiiQuery query = new KiiQuery(
                KiiClause.and(
                        KiiClause.equals(Tracking.BOOK_ID, bookId),
                        KiiClause.equals(Tracking.OWNER_ID, userId)
                )
        );
        query.setLimit(1);
        query.sortByDesc("_created");
        final KiiBucket trackingBucket = Kii.bucket(Tracking.BUCKET_NAME);
        trackingBucket.query(new KiiQueryCallBack<KiiObject>() {
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
                    if (result.getResult().isEmpty()) {
                        callback.success(token, null);
                        Log.d(TAG,"お気に入りじゃなかったです");
                        return;
                    }
                    KiiObject kiiObject = result.getResult().get(0);
                    Tracking tracking = Tracking.createFrom(kiiObject);
                    callback.success(token, tracking);

                } catch (JSONException e1) {
                    Log.e(TAG, "onQueryCompleted: ", e1);
                    callback.failure(e);
                }
            }
        }, query);
    }

    public static void fetch(String id, final KiiObjectCallback<Tracking> callback) {
        KiiObject trackingObject = Kii.bucket(Tracking.BUCKET_NAME).object(id);
        trackingObject.refresh(new KiiObjectCallBack() {
            @Override
            public void onRefreshCompleted(int token, @NonNull KiiObject object, @Nullable Exception exception) {
                super.onRefreshCompleted(token, object, exception);
                if (exception != null) {
                    callback.failure(exception);
                    return;
                }
                try {
                    Tracking tracking = Tracking.createFrom(object);
                    callback.success(token, tracking);
                } catch (JSONException e) {
                    callback.failure(e);
                }
            }
        });
    }
}
