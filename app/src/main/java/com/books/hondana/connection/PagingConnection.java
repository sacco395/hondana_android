package com.books.hondana.connection;

import android.support.annotation.Nullable;
import android.util.Log;

import com.books.hondana.model.Book;
import com.books.hondana.model.abst.KiiModel;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiQueryCallBack;
import com.kii.cloud.storage.query.KiiQuery;
import com.kii.cloud.storage.query.KiiQueryResult;

import org.json.JSONException;

import java.util.List;

public abstract class PagingConnection<T extends KiiModel> {

    private static final String TAG = PagingConnection.class.getSimpleName();

    private KiiQueryResult<KiiObject> lastQueryResult;

    /**
     * @param limit 最大取得件数。0 以下を指定したら Kii の仕様上 Max の 200 件
     * @param refresh 再度最新のものから取得しなおすためのフラグ
     *                false なら、前回の続きからクエリ、true なら最初から
     * @param callback 処理終了時のコールバック
     */
    public void fetch(int limit, boolean refresh, final KiiObjectListCallback<T> callback) {
        if (lastQueryResult == null || refresh) {
            KiiQuery query = createQuery();

            // Default 新しい順
            query.sortByDesc("_created");
            if (limit > 0) {
                query.setLimit(limit);
            }

            KiiBucket kiiBucket = getBucket();
            kiiBucket.query(
                    createQueryCallbackWith(callback),
                    query);
            return;
        }
        if (lastQueryResult.hasNext()) {
            lastQueryResult.getNextQueryResult(createQueryCallbackWith(callback));
            return;
        }
        Log.d(TAG, "fetch: No more data");
    }

    private KiiQueryCallBack<KiiObject> createQueryCallbackWith(final KiiObjectListCallback<T> callback) {
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
                    List<T> models = convert(result.getResult());
                    lastQueryResult = result;
                    callback.success(token, models);
                } catch (JSONException e1) {
                    Log.e(TAG, "onQueryCompleted: ", e1);
                    callback.failure(e1);
                }
            }
        };
    }

    protected abstract KiiQuery createQuery();

    protected abstract KiiBucket getBucket();

    protected abstract List<T> convert(List<KiiObject> objects) throws JSONException;
}
