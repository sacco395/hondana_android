package com.books.hondana.connection;

import android.support.annotation.Nullable;
import android.util.Log;

import com.books.hondana.model.Book;
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
 * KiiCloud 上の Book を取得するためのクラス
 * インスタンス化せずに クラスメソッドを使う
 */
public class KiiBookConnection {

    private static final String TAG = KiiBookConnection.class.getSimpleName();

    private KiiBookConnection() {
        throw new RuntimeException("Do not instantiate this Class!");
    }
	

    public static void fetchPostedBooks(String userId, KiiObjectListCallback<Book> callback) {
        KiiClause clause = KiiClause.equals(Book.OWNER_ID, userId);
        KiiQuery ownerIdQuery = new KiiQuery(clause);
        ownerIdQuery.sortByDesc("_created");
        queryBookBucket(ownerIdQuery, callback);
    }

    public static void fetchExhibitedBooks(String userId, KiiObjectListCallback<Book> callback) {
        KiiClause clause = KiiClause.and(
                KiiClause.equals(Book.OWNER_ID, userId),
                KiiClause.equals(Book.STATE, 0)
        );
        KiiQuery RequestingByOthersBookQuery = new KiiQuery(clause);
        RequestingByOthersBookQuery.sortByDesc("_created");
        queryBookBucket(RequestingByOthersBookQuery, callback);

    }


    /**
     * 出品した本の取引状態
     */
    public static void fetchReceivedRequestBooks(String userId, KiiObjectListCallback<Book> callback) {
        KiiClause clause = KiiClause.and(
                KiiClause.equals(Book.OWNER_ID, userId),
                KiiClause.equals(Book.STATE, 1)
        );
        KiiQuery RequestingByOthersBookQuery = new KiiQuery(clause);
        RequestingByOthersBookQuery.sortByDesc("_created");
        queryBookBucket(RequestingByOthersBookQuery, callback);

    }

    public static void fetchHadSendBooks(String userId, KiiObjectListCallback<Book> callback) {
        KiiClause clause = KiiClause.and(
                KiiClause.equals(Book.OWNER_ID, userId),
                KiiClause.equals(Book.STATE, 2)
        );
        KiiQuery RequestingByOthersBookQuery = new KiiQuery(clause);
        RequestingByOthersBookQuery.sortByDesc("_created");
        queryBookBucket(RequestingByOthersBookQuery, callback);
    }

    private static void queryBookBucket(KiiQuery query, final KiiObjectListCallback<Book> callback) {
        KiiBucket kiiBucket = Kii.bucket(Book.BUCKET_NAME);
        kiiBucket.query(
                new KiiQueryCallBack<KiiObject>() {
                    public void onQueryCompleted(int token, KiiQueryResult<KiiObject> result, Exception e) {
                        // Error
                        if (result == null || result.getResult() == null) {
                            Log.e(TAG, "onQueryCompleted: ", e);
                            callback.failure(e);
                            return;
                        }
                        // Success
                        try {
                            List<Book> books = convert(result.getResult());
                            callback.success(token, books);
                        } catch (JSONException e1) {
                            Log.e(TAG, "onQueryCompleted: ", e1);
                            callback.failure(e);
                        }
                    }
                },
                query);
    }
    private static List<Book> convert(List<KiiObject> bookObjects) throws JSONException {
        List<Book> books = new ArrayList<>();
        for (KiiObject object : bookObjects) {
            books.add(Book.createFrom(object));
        }
        return books;
    }


    public static void fetchByRequestBookId(final String bookId, final String serverUserId, final KiiObjectCallback<Request> callback) {
        final KiiQuery query = new KiiQuery(
                KiiClause.and(
                        KiiClause.equals(Request.SERVER_ID, serverUserId),
                        KiiClause.equals(Request.BOOK_ID, bookId)
                )
        );
        query.setLimit(1);
        query.sortByDesc("_created");
        final KiiBucket requestBucket = Kii.bucket(Request.BUCKET_NAME);
        requestBucket.query(new KiiQueryCallBack<KiiObject>() {
            @Override
            public void onQueryCompleted(int token, @Nullable KiiQueryResult<KiiObject> result, @Nullable Exception e) {
                // エラーハンドリングと適したコールバック呼び出し
                if (e != null) {
                    callback.failure(e);
                    return;
                }
                // result の null チェック（おそらくエラーが null なかぎり、このようなケースはないと思われるが、念のため）
                if (result == null || result.getResult() == null) {
                    Log.e(TAG, "onQueryCompleted: Could not get any result with " +
                            "serverId=" + serverUserId + " AND bookId=" + bookId);
                    callback.failure(null);
                    return;
                }
                try {
                    // KiiQueryResult が持っている KiiObject の List を getResult
                    // で取得し、その List の一番先頭にあるものを取る
                    // クエリで Limit を指定しているので、List と言っても、一つしかないはず
                    KiiObject kiiObject = result.getResult().get(0);
                    // KiiObject から Request を生成
                    Request request = Request.createFrom(kiiObject);
                    callback.success(token, request);
                } catch (JSONException e1) {
                    callback.failure(e1);
                }
            }
        }, query);
    }
}
