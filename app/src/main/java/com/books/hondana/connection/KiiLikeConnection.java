package com.books.hondana.connection;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.books.hondana.model.Like;
import com.books.hondana.model.Member;
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
 * KiiCloud 上の Request を取得するためのクラス
 * インスタンス化せずに クラスメソッドを使う
 */
public class KiiLikeConnection {

    private static final String TAG = KiiLikeConnection.class.getSimpleName();

    /**
     * インスタンス化厳禁
     */
    private KiiLikeConnection() {
        throw new RuntimeException("Do not instantiate this Class!");
    }


    public static void fetchStared(String userId, KiiObjectListCallback<Like> callback){
        KiiClause clause = KiiClause.equals(Like.USER_ID, userId);
        KiiQuery stared = new KiiQuery(clause);
        queryLikeBucket(stared, callback);
    }

    /**
     * KiiQuery を元に、Like のバケツを検索、リストを取得
     * @param query 検索条件
     * @param callback
     */
    private static void queryLikeBucket(KiiQuery query, final KiiObjectListCallback<Like> callback) {
        KiiBucket likeBucket = Kii.bucket(Like.BUCKET_NAME);
        likeBucket.query(new KiiQueryCallBack<KiiObject>() {
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
                    List<Like> likes = convert(result.getResult());
                    callback.success(token, likes);
                } catch (JSONException e1) {
                    Log.e(TAG, "onQueryCompleted: ", e1);
                    callback.failure(e);
                }
            }
        }, query);
    }

    private static List<Like> convert(List<KiiObject> likeObjects) throws JSONException {
        List<Like> likes = new ArrayList<>();
        for (KiiObject object : likeObjects) {
            likes.add(Like.createFrom(object));
        }
        return likes;
    }


    public static void fetchLikeByBookId(final String bookId, final String userId, final KiiObjectCallback<Like> callback) {
        final KiiQuery query = new KiiQuery(
                KiiClause.and(
                        KiiClause.equals(Like.BOOK_ID, bookId),
                        KiiClause.equals(Like.USER_ID, userId)
                )
        );
        query.setLimit(1);
        query.sortByDesc("_created");
        final KiiBucket likeBucket = Kii.bucket(Like.BUCKET_NAME);
        likeBucket.query(new KiiQueryCallBack<KiiObject>() {
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
                    Like like = Like.createFrom(kiiObject);
                    callback.success(token, like);

                } catch (JSONException e1) {
                    Log.e(TAG, "onQueryCompleted: ", e1);
                    callback.failure(e);
                }
            }
        }, query);
    }

    public static void fetch(String id, final KiiObjectCallback<Like> callback) {
        KiiObject memberObject = Kii.bucket(Member.BUCKET_NAME).object(id);
        memberObject.refresh(new KiiObjectCallBack() {
            @Override
            public void onRefreshCompleted(int token, @NonNull KiiObject object, @Nullable Exception exception) {
                super.onRefreshCompleted(token, object, exception);
                if (exception != null) {
                    callback.failure(exception);
                    return;
                }
                try {
                    Like like = Like.createFrom(object);
                    callback.success(token, like);
                } catch (JSONException e) {
                    callback.failure(e);
                }
            }
        });
    }
}
