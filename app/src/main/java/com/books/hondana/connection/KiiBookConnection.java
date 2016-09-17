package com.books.hondana.connection;

import android.util.Log;

import com.books.hondana.model.Book;
import com.books.hondana.model.Genre;
import com.books.hondana.model.abst.KiiModel;
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

    /**
     * @param genre 取得してくるジャンル
     * @param from ページネーションのための、作成日時の UNIX 時間 (ミリ秒)
     *             この時間以前に作成された KiiBook を取ってくる
     *             0 を指定したら、最新のものから
     * @param limit 最大取得件数。0 を指定したら Kii の仕様上 Max の 200 件
     * @param callback 処理終了時のコールバック
     */
    public static void fetch(Genre genre, long from, int limit, final KiiObjectListCallback<Book> callback) {
        if (from == 0) {
            from = System.currentTimeMillis();
        }
        KiiClause clauseWithTime = KiiClause.and(
                genre.clause(), // Genre 絞り込み
                KiiClause.lessThan(KiiModel.CREATED_AT, from) // 日時絞り込み
        );
        Log.d(TAG, "fetch: " + clauseWithTime.toString());

        KiiQuery query = new KiiQuery(clauseWithTime);

        // Default 新しい順
        query.sortByDesc("_created");
        if (limit != 0) {
            query.setLimit(limit);
        }

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
}
