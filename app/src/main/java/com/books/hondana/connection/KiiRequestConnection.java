package com.books.hondana.connection;

import android.support.annotation.Nullable;
import android.util.Log;

import com.books.hondana.model.Request;
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
 * KiiCloud 上の Request を取得するためのクラス
 * インスタンス化せずに クラスメソッドを使う
 */
public class KiiRequestConnection {

    private static final String TAG = KiiRequestConnection.class.getSimpleName();

    /**
     * インスタンス化厳禁
     */
    private KiiRequestConnection() {
        throw new RuntimeException("Do not instantiate this Class!");
    }

    /**
     * ユーザがお願いしているリクエストのリストを取得
     * @param userId KiiUser#getUserID
     * @param callback
     */
    public static void fetchRequestsByUser(String userId, KiiObjectListCallback<Request> callback) {
        KiiClause clause = KiiClause.equals(Request.CLIENT_ID, userId);
        KiiQuery clientIdQuery = new KiiQuery(clause);
        queryRequestBucket(clientIdQuery, callback);
    }

    /**
     * ユーザがお願いされているリクエストのリストを取得
     * @param userId KiiUser#getUserID
     * @param callback
     */
    public static void fetchRequestsByOthers(String userId, KiiObjectListCallback<Request> callback) {
        KiiClause clause = KiiClause.equals(Request.SERVER_ID, userId);
        KiiQuery serverIdQuery = new KiiQuery(clause);
        queryRequestBucket(serverIdQuery, callback);
    }

    /**
     * ユーザが関わっているリクエストのリストを取得
     * @param userId KiiUser#getUserID
     * @param callback
     */
    public static void fetchRequestsRelatedToUser(String userId, KiiObjectListCallback<Request> callback) {
        KiiClause clause = KiiClause.or(
                KiiClause.equals(Request.CLIENT_ID, userId),
                KiiClause.equals(Request.SERVER_ID, userId)
        );
        KiiQuery userIdQuery = new KiiQuery(clause);
        queryRequestBucket(userIdQuery, callback);
    }

    /**
     * KiiQuery を元に、Request のバケツを検索、リストを取得
     * @param query 検索条件
     * @param callback
     */
    private static void queryRequestBucket(KiiQuery query, final KiiObjectListCallback<Request> callback) {
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
        }, query);
    }

    private static List<Request> convert(List<KiiObject> requestObjects) throws JSONException {
        List<Request> requests = new ArrayList<>();
        for (KiiObject object : requestObjects) {
            requests.add(Request.createFrom(object));
        }
        return requests;
    }
}
