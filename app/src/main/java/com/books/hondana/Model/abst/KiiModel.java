package com.books.hondana.model.abst;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiObjectCallBack;

import org.json.JSONException;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/10/16.
 */
public abstract class KiiModel {

    private static final String TAG = KiiModel.class.getSimpleName();

    public static final String ID = "_id";
    public static final String OWNER = "_owner";
    public static final String CREATED_AT = "_created";

    protected KiiObject source;

    protected String id;

    protected long createdAt;

    protected long updatedAt;

    protected KiiModel() {
    }

    protected KiiModel(KiiObject kiiObject) throws JSONException {
        source = kiiObject;
        id = kiiObject.getString(ID);
        createdAt = kiiObject.getCreatedTime();
        updatedAt = kiiObject.getModifedTime();
        try {
            setValuesFrom(kiiObject);
        } catch (JSONException e) {
            Log.e(TAG, "KiiModel: ", e);
        }
    }

    public String getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @return Kii.bucket(バケツ名)
     */
    public abstract KiiBucket bucket();

    /**
     * KiiObject から、フィールド変数に値をセットする
     * @param kiiObject データの元になるオブジェクト
     * @throws JSONException
     */
    public abstract void setValuesFrom(KiiObject kiiObject) throws JSONException;

    protected abstract KiiObject createKiiObject() throws JSONException;

    /**
     * KiiCloud にすでに登録されている Object はアップデート
     * 新規で作成したものは、保存
     * @param override KiiCloud 上の Object が、KiiObject#refresh 以降変化があったとき、
     *                 上書き保存するかしないか。true なら上書き。
     *                 false なら、変化があったときはエラーで KiiSaveCallback#failure がコールされる
     * @param callback 保存処理が終了したときのコールバック
     */
    public void save(boolean override, final KiiSaveCallback callback) {
        try {
            source = createKiiObject();
        } catch (JSONException e) {
            Log.w(TAG, "save: ", e);
            callback.failure(e);
        }
        if (hasValidId()) {
            Log.d(TAG, "save: update!");
            source.saveAllFields(new KiiObjectCallBack() {
                @Override
                public void onSaveCompleted(int token, @NonNull KiiObject object, @Nullable Exception exception) {
                    if (exception == null) {
                        callback.success(token, object);
                        return;
                    }
                    callback.failure(exception);
                }
            }, override);
            return;
        }
        Log.d(TAG, "save: create new model on KiiCloud!");
        source.save(new KiiObjectCallBack() {
            @Override
            public void onSaveCompleted(int token, @NonNull KiiObject object, @Nullable Exception exception) {
                if (exception == null) {
                    callback.success(token, object);
                    return;
                }
                callback.failure(exception);
            }
        });
    }

    /**
     * ID の有無判定
     * KiiObject を保存する際、新規作成かアップデートかを判定するのに使う
     * @return
     */
    private boolean hasValidId() {
        return !(id == null || id.equals(""));
    }

    public interface KiiSaveCallback {
        void success(int token, KiiObject object);
        void failure(@Nullable Exception e);
    }
}
