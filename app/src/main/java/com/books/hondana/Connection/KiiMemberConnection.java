package com.books.hondana.Connection;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.books.hondana.Model.Member;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiObjectCallBack;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/9/16.
 */
public class KiiMemberConnection {

    private static final String TAG = KiiMemberConnection.class.getSimpleName();

    private KiiMemberConnection() {
        throw new RuntimeException("Do not instantiate this Class!");
    }

    public static void fetch(String id, final KiiObjectCallback<Member> callback) {
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
                    Member member = new Member(object);
                    callback.success(token, member);
                } catch (JSONException e) {
                    callback.failure(e);
                }
            }
        });
    }

    /**
     * @param userId 対象ユーザの ID
     * @param diff ポイントの差分。減るときは負の数を指定
     * @param callback コールバック
     */
    public static void updatePoint(String userId, final int diff, final KiiObjectCallback<Member> callback) {
        fetch(userId, new KiiObjectCallback<Member>() {
            @Override
            public void success(int token, Member member) {
                int current = member.getPoint();
                member.setPoint(current + diff);
                KiiObject updated;
                try  {
                    updated = member.createKiiObject();
                } catch (JSONException e) {
                    callback.failure(e);
                    return;
                }
                updated.saveAllFields(new KiiObjectCallBack() {
                    @Override
                    public void onSaveCompleted(int token, @NonNull KiiObject object, @Nullable Exception exception) {
                        if (exception != null) {
                            callback.failure(exception);
                            return;
                        }
                        try {
                            Member result = new Member(object);
                            callback.success(token, result);
                        } catch (JSONException e) {
                            callback.failure(e);
                        }
                    }
                }, false);
            }

            @Override
            public void failure(@Nullable Exception e) {

            }
        });
    }

    private static List<Member> convert(List<KiiObject> memberObjects) throws JSONException {
        List<Member> members = new ArrayList<>();
        for (KiiObject memberObject : memberObjects) {
            members.add(new Member(memberObject));
        }
        return members;
    }
}
