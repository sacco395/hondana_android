package com.books.hondana.Connection;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;

import com.books.hondana.Model.Member;
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

import java.util.List;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/9/16.
 */
public class KiiMemberConnection {

    public KiiMemberConnection() {
    }

    public void fetch(String id, final Callback callback) {
        KiiQuery idQuery = new KiiQuery(
                KiiClause.equals(KiiMember.USER_ID, id)
        );
        idQuery.setLimit(1);

        KiiBucket kiiBucket = Kii.bucket(KiiCloudBucket.MEMBERS.getName());
        kiiBucket.query(
                new KiiQueryCallBack<KiiObject>() {
                    public void onQueryCompleted(int token, KiiQueryResult<KiiObject> result, Exception e) {
                        // Error
                        if (result == null || result.getResult() == null) {
                            callback.failure(e);
                            return;
                        }
                        // Success
                        callback.success(new KiiMember(result.getResult().get(0)));
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
            public void success(KiiMember kiiMember) {
                Member member = kiiMember.convert();
                int point = member.getPoint();
                point += diff;
                member.setPoint(point);
                KiiMember updated = KiiMember.create(member);
                updated.save(new KiiObjectCallBack() {
                    @Override
                    public void onSaveCompleted(int token, @NonNull KiiObject object, @Nullable Exception exception) {
                        if (exception == null) {
                            callback.success(new KiiMember(object));
                            return;
                        }
                        callback.failure(exception);
                    }
                });
            }

            @Override
            public void failure(@Nullable Exception e) {
                callback.failure(e);
            }
        });
    }

    public interface Callback {
        void success(KiiMember member);
        void failure(@Nullable Exception e);
    }
}
