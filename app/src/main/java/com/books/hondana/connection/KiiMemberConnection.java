package com.books.hondana.connection;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.books.hondana.model.Member;
import com.books.hondana.model.abst.KiiModel;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiObjectCallBack;

import org.json.JSONException;

/**
 * KiiCloud 上の Member を取得するためのクラス
 * インスタンス化せずに クラスメソッドを使う
 */
public class KiiMemberConnection {

    private static final String TAG = KiiMemberConnection.class.getSimpleName();

    /**
     * インスタンス化厳禁
     */
    private KiiMemberConnection() {
        throw new RuntimeException("Do not instantiate this Class!");
    }

    /**
     * ID で KiiCloud 上の Member を検索
     * @param id KiiUser#getID から取得できる ID
     * @param callback
     */
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
                    Member member = Member.createFrom(object);
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
     * @param callback
     */
    public static void updatePoint(String userId, final int diff, final KiiObjectCallback<Member> callback) {
        fetch(userId, new KiiObjectCallback<Member>() {
            @Override
            public void success(int token, Member member) {
                int current = member.getPoint();
                member.setPoint(current + diff);
                member.save(false, new KiiModel.KiiSaveCallback() {
                    @Override
                    public void success(int token, KiiObject object) {
                        try {
                            Member result = Member.createFrom(object);
                            callback.success(token, result);
                        } catch (JSONException e) {
                            callback.failure(e);
                        }
                    }

                    @Override
                    public void failure(@Nullable Exception e) {
                        callback.failure(e);
                    }
                });
            }

            @Override
            public void failure(@Nullable Exception e) {
                callback.failure(new IllegalStateException("KiiCloud 上の Member の値が書き換わっている。ポイントの整合性が取れなくなる可能性があるので、ポイントを書き換えずに通信終了"));
            }
        });
    }
}
