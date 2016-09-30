package com.books.hondana.connection;

import android.support.annotation.Nullable;
import android.util.Log;

import com.books.hondana.model.Book;
import com.books.hondana.model.Genre;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiQueryCallBack;
import com.kii.cloud.storage.query.KiiQuery;
import com.kii.cloud.storage.query.KiiQueryResult;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 10/1/16.
 */

public class KiiGenreConnection {

    private static final String TAG = KiiGenreConnection.class.getSimpleName();

    private Genre genre;

    private KiiQueryResult<KiiObject> lastQueryResult;

    public KiiGenreConnection(Genre genre) {
        this.genre = genre;
    }

    /**
     * @param limit 最大取得件数。0 以下を指定したら Kii の仕様上 Max の 200 件
     * @param refresh 再度最新のものから取得しなおすためのフラグ
     *                false なら、前回の続きからクエリ、true なら最初から
     * @param callback 処理終了時のコールバック
     */
    public void fetch(int limit, boolean refresh, final KiiObjectListCallback<Book> callback) {
        if (lastQueryResult == null || refresh) {
            KiiQuery query = new KiiQuery(genre.clause());

            // Default 新しい順
            query.sortByDesc("_created");
            if (limit > 0) {
                query.setLimit(limit);
            }

            KiiBucket kiiBucket = Kii.bucket(Book.BUCKET_NAME);
            kiiBucket.query(
                    createQueryCallbackWith(callback),
                    query);
            return;
        }
        if (lastQueryResult.hasNext()) {
            lastQueryResult.getNextQueryResult(createQueryCallbackWith(callback));
            return;
        }
        Log.d(TAG, "fetch: No more books in " + genre.title());
    }

    private KiiQueryCallBack<KiiObject> createQueryCallbackWith(final KiiObjectListCallback<Book> callback) {
        return new KiiQueryCallBack<KiiObject>() {
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
                    List<Book> books = convert(result.getResult());
                    lastQueryResult = result;
                    callback.success(token, books);
                } catch (JSONException e1) {
                    Log.e(TAG, "onQueryCompleted: ", e1);
                    callback.failure(e1);
                }
            }
        };
    }

    private static List<Book> convert(List<KiiObject> bookObjects) throws JSONException {
        List<Book> books = new ArrayList<>();
        for (KiiObject object : bookObjects) {
            books.add(Book.createFrom(object));
        }
        return books;
    }
}
