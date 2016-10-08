package com.books.hondana.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.books.hondana.model.abst.KiiModel;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;

import org.json.JSONException;
/**
 * お気に入りに関する情報を保持
 */
public class Like extends KiiModel implements Parcelable {
    public static final String BUCKET_NAME = "likes";
    public static final String BOOK_ID = "book_id";
    public static final String LIKE_ID = "like_id";
    public static final String USER_ID = "userId";

    /**
     * リクエストをしたユーザID
     */
    private String userId;
    private String bookId;
    private boolean likeId;
    private String id;

    /**
     * Request を、新規に作成する。すでにサーバに保存されているオブジェクトの取得は、
     * {@link com.books.hondana.connection.KiiRequestConnection} を使用すること
     *
     * @param userId KiiUser#getID
     * @param book     リクエストの対象の本
     * @return
     */
    public static Like createNew(String userId, Book book) {
        Like like = new Like();
        like.setUserId(userId);
        like.setBookId(book.getId());
        return like;
    }

    /**
     * KiiCloud に保存されているオブジェクトから、Request を生成
     *
     * @param kiiObject
     * @return
     * @throws JSONException KiiObject 内に、求めるフィールドがなかったら例外をスロー
     */
    public static Like createFrom(KiiObject kiiObject) throws JSONException {
        return new Like(kiiObject);
    }

    private Like() {
        userId = "";
        likeId = true;
        bookId = "";
    }

    private Like(KiiObject kiiObject) throws JSONException {
        super(kiiObject);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isLikeId() {
        return likeId;
    }

    public void setLikeId(boolean likeId) {
        this.likeId = likeId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    @Override
    public KiiBucket bucket() {
        return Kii.bucket(BUCKET_NAME);
    }

    @Override
    public void setValuesFrom(KiiObject object) throws JSONException {
        userId = object.getString(USER_ID);
        likeId = object.getBoolean(LIKE_ID);
        bookId = object.getString(BOOK_ID);
    }

    @Override
    public KiiObject createKiiObject() throws JSONException {
        if (source == null) {
            source = bucket().object();
        }
        source.set(USER_ID, userId);
        source.set(LIKE_ID, likeId);
        source.set(BOOK_ID, bookId);
        return source;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userId);
        dest.writeByte(this.likeId ? (byte) 1 : (byte) 0);
        dest.writeString(this.bookId);
    }

    protected Like(Parcel in) {
        this.id = in.readString();
        this.userId = in.readString();
        this.likeId = in.readByte() != 0;
        this.bookId = in.readString();
    }

    public static final Creator<Like> CREATOR = new Creator<Like>() {
        @Override
        public Like createFromParcel(Parcel source) {
            return new Like(source);
        }

        @Override
        public Like[] newArray(int size) {
            return new Like[size];
        }
    };

}
