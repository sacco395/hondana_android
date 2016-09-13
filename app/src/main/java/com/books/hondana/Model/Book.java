package com.books.hondana.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.books.hondana.Model.abst.KiiModel;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;

import org.json.JSONException;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/9/16.
 */
public class Book extends KiiModel implements Parcelable {

    private static final String TAG = Book.class.getSimpleName();

    public static final String BUCKET_NAME = "appbooks";

    public static final String OWNER_ID = "owner_id";
    public static final String INFO = "info";
    public static final String CONDITION = "condition";

    private String ownerId;

    private BookInfo info;

    private BookCondition condition;

    public Book() {
        ownerId = "";
        info = new BookInfo();
        condition = new BookCondition();
    }

    public Book(KiiObject kiiObject) throws JSONException {
        super(kiiObject);
        ownerId = source.getString(OWNER_ID);
        info = new BookInfo(source.getJSONObject(INFO));
        condition = new BookCondition(source.getJSONObject(CONDITION));
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public BookInfo getInfo() {
        return info;
    }

    public void setInfo(BookInfo info) {
        this.info = info;
    }

    public BookCondition getCondition() {
        return condition;
    }

    public void setCondition(BookCondition condition) {
        this.condition = condition;
    }

    @Override
    public KiiBucket bucket() {
        return Kii.bucket(BUCKET_NAME);
    }

    @Override
    public void setValuesFrom(KiiObject object) throws JSONException {
        ownerId = object.getString(OWNER_ID);
        info = new BookInfo(object.getJSONObject(INFO));
        condition = new BookCondition(object.getJSONObject(CONDITION));
    }

    @Override
    public KiiObject createKiiObject() throws JSONException {
        if (source == null) {
            source = bucket().object();
        }
        source.set(OWNER, ownerId);
        source.set(INFO, info.toJSON());
        source.set(CONDITION, condition.toJSON());
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
        dest.writeString(this.ownerId);
        dest.writeParcelable(this.info, flags);
        dest.writeParcelable(this.condition, flags);
    }

    protected Book(Parcel in) {
        this.source = in.readParcelable(KiiObject.class.getClassLoader());
        this.id = in.readString();
        this.ownerId = in.readString();
        this.info = in.readParcelable(BookInfo.class.getClassLoader());
        this.condition = in.readParcelable(BookCondition.class.getClassLoader());
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
