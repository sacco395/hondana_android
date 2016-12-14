package com.books.hondana.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.books.hondana.model.abst.KiiModel;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;

import org.json.JSONException;

/**
 * Created by sacco on 2016/12/09.
 */

public class Tracking extends KiiModel implements Parcelable {

    public static final String BUCKET_NAME = "trackings";

    // KiiCloud 上のフィールド名
    public static final String BOOK_ID = "book_id";
    public static final String BOOK_TITLE = "book_title";
    public static final String BOOK_AUTHOR = "book_author";
    public static final String BOOK_IMAGE_URL = "book_image_url";
    public static final String OWNER_ID = "owner_id";
    public static final String OWNER_NAME = "owner_name";
    public static final String OWNER_IMAGE_URL = "owner_image_url";
    public static final String SITUATION = "situation";

    private String bookId;

    private String bookTitle;

    private String bookAuthor;

    private String bookImageUrl;

    private String ownerId;

    private String ownerName;

    private String ownerImageUrl;

    private String situation;

    /**
     * Tracking を、新規に作成する。すでにサーバに保存されているオブジェクトの取得は、
     * {@link com.books.hondana.connection.KiiTrackingConnection} を使用すること
     *
     * @param ownerId KiiUser#getID
     * @param book 登録する本
     * @return
     */
    public static Tracking createNew(String ownerId, Book book) {
        Tracking tracking = new Tracking();
        tracking.setBookId(book.getId());
        BookInfo info = book.getInfo();
        tracking.setBookTitle(info.getTitle());
        tracking.setBookAuthor(info.getAuthor());
        tracking.setBookImageUrl(info.getImageUrl());
        tracking.setOwnerId(ownerId);
        return tracking;
    }


    /**
     * すでに KiiCloud 上に保存されているオブジェクトから Tracking を生成
     * @param kiiObject
     * @return
     * @throws JSONException
     */
    public static Tracking createFrom(KiiObject kiiObject) throws JSONException {
        return new Tracking (kiiObject);
    }

    private Tracking() {
        bookId = "";
        bookTitle = "";
        bookAuthor = "";
        bookImageUrl = "";
        ownerId = "";
        ownerName = "";
        ownerImageUrl = "";
        situation = "";
    }

    private Tracking(KiiObject kiiObject) throws JSONException {
        super(kiiObject);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookImageUrl() {
        return bookImageUrl;
    }

    public void setBookImageUrl(String bookImageUrl) {
        this.bookImageUrl = bookImageUrl;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerImageUrl(String ownerImageUrl) {
        this.ownerImageUrl = ownerImageUrl;
    }

    public String getOwnerImageUrl() {
        return ownerImageUrl;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getSituation() {
        return situation;
    }

    @Override
    public KiiBucket bucket() {
        return Kii.bucket(BUCKET_NAME);
    }

    @Override
    public void setValuesFrom(KiiObject kiiObject) throws JSONException {
        bookId = kiiObject.getString(BOOK_ID);
        bookTitle = kiiObject.getString(BOOK_TITLE);
        bookAuthor = kiiObject.getString(BOOK_AUTHOR);
        bookImageUrl = kiiObject.getString(BOOK_IMAGE_URL);
        ownerId = kiiObject.getString(OWNER_ID);
        ownerName = kiiObject.getString(OWNER_NAME);
        ownerImageUrl = kiiObject.getString(OWNER_IMAGE_URL);
        situation = kiiObject.getString(SITUATION);
    }

    @Override
    protected KiiObject createKiiObject() throws JSONException {
        if (source == null) {
            source = bucket().object();
        }
        source.set(BOOK_ID, bookId);
        source.set(BOOK_TITLE, bookTitle);
        source.set(BOOK_AUTHOR, bookAuthor);
        source.set(BOOK_IMAGE_URL, bookImageUrl);
        source.set(OWNER_ID, ownerId);
        source.set(OWNER_NAME, ownerName);
        source.set(OWNER_IMAGE_URL, ownerImageUrl);
        source.set(SITUATION, situation);
        return source;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.source, flags);
        dest.writeString(this.id);
        dest.writeLong(this.createdAt);
        dest.writeLong(this.updatedAt);
        dest.writeString(this.bookId);
        dest.writeString(this.bookTitle);
        dest.writeString(this.bookAuthor);
        dest.writeString(this.bookImageUrl);
        dest.writeString(this.ownerId);
        dest.writeString(this.ownerName);
        dest.writeString(this.ownerImageUrl);
        dest.writeString(this.situation);
    }

    protected Tracking(Parcel in) {
        this.source = in.readParcelable(KiiObject.class.getClassLoader());
        this.id = in.readString();
        this.createdAt = in.readLong();
        this.updatedAt = in.readLong();
        this.bookId = in.readString();
        this.bookTitle = in.readString();
        this.bookAuthor = in.readString();
        this.bookImageUrl = in.readString();
        this.ownerId = in.readString();
        this.ownerName = in.readString();
        this.ownerImageUrl = in.readString();
        this.situation = in.readString();
    }

    public static final Creator<Tracking> CREATOR = new Creator<Tracking>() {
        @Override
        public Tracking createFromParcel(Parcel source) {
            return new Tracking (source);
        }

        @Override
        public Tracking[] newArray(int size) {
            return new Tracking[size];
        }
    };
}
