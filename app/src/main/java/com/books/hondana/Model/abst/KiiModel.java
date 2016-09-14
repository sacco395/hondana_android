package com.books.hondana.Model.abst;

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

    public KiiModel() {
    }

    public KiiModel(KiiObject kiiObject) throws JSONException {
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

    public abstract KiiBucket bucket();

    public abstract void setValuesFrom(KiiObject kiiObject) throws JSONException;

    public abstract KiiObject createKiiObject() throws JSONException;

    public void save(boolean override, final KiiSaveCallback callback) {
        try {
            source = createKiiObject();
        } catch (JSONException e) {
            Log.w(TAG, "save: ", e);
            callback.failure(e);
        }
        if (hasValidId()) {
            Log.d(TAG, "save: saveAllFields");
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
        Log.d(TAG, "save: save");
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

    private boolean hasValidId() {
        return !(id == null || id.equals(""));
    }

    public interface KiiSaveCallback {
        void success(int token, KiiObject object);
        void failure(@Nullable Exception e);
    }
}
