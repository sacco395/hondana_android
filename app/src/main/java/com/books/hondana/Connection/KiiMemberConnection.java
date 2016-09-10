package com.books.hondana.Connection;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.books.hondana.Model.kii.KiiCloudBucket;
import com.books.hondana.Model.kii.KiiMember;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiObjectCallBack;
import com.kii.cloud.storage.callback.KiiQueryCallBack;
import com.kii.cloud.storage.query.KiiClause;
import com.kii.cloud.storage.query.KiiQuery;
import com.kii.cloud.storage.query.KiiQueryResult;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/9/16.
 */
public class KiiMemberConnection {

    private static final String TAG = KiiMemberConnection.class.getSimpleName();

    public KiiMemberConnection() {
    }

    public void fetch(String id, final Callback callback) {
        KiiQuery idQuery = new KiiQuery(
                KiiClause.equals(KiiMember.OWNER, id)
        );

        KiiBucket kiiBucket = Kii.bucket(KiiCloudBucket.MEMBERS.getName());
        kiiBucket.query(
                new KiiQueryCallBack<KiiObject>() {
                    public void onQueryCompleted(int token, KiiQueryResult<KiiObject> result, Exception e) {
                        // Error
                        if (result == null || result.getResult() == null || result.getResult().isEmpty()) {
                            callback.failure(e);
                            return;
                        }
                        if (result.getResult().size() > 1) {
                            throw  new RuntimeException("KiiMember は、ユーザに対して一つのはずなのに、複数存在している。ポイントの整合性などが保てなくなる致命的なエラー。三上に連絡ください。");
                        }
                        // Success
                        callback.success(result.getResult().get(0));
                    }
                }, idQuery);
    }

    /**
     * @param userId 対象ユーザの ID
     * @param diff ポイントの差分。減るときは負の数を指定
     * @param callback コールバック
     */
    public void updatePoint(String userId, final int diff, final Callback callback) {
        fetch(userId, new Callback() {
            @Override
            public void success(KiiObject kiiObject) {
                String pointStr;
                if (kiiObject.has(KiiMember.POINT)) {
                    pointStr = kiiObject.getString(KiiMember.POINT);
                } else {
                    pointStr = "0";
                }
                int point = Integer.valueOf(pointStr);
                kiiObject.set(KiiMember.POINT, point + diff);
                kiiObject.saveAllFields(new KiiObjectCallBack() {
                    @Override
                    public void onSaveCompleted(int token, @NonNull KiiObject object, @Nullable Exception exception) {
                        if (exception == null) {
                            callback.success(object);
                            return;
                        }
                        callback.failure(exception);
                    }
                }, false);
            }

            @Override
            public void failure(@Nullable Exception e) {
                callback.failure(e);
            }
        });
    }

    public interface Callback {
        void success(KiiObject kiiObject);
        void failure(@Nullable Exception e);
    }
}
