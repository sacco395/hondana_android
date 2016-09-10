package com.books.hondana.Model.book;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.books.hondana.Model.KiiModel;
import com.books.hondana.Model.KiiModelException;
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

    public static final String OWNER = "owner";
    public static final String INFO = "info";
    public static final String CONDITION = "condition";

    private String id;

    private String owner;

    private BookInfo info;

    private BookCondition condition;

    public Book() {
    }

    public Book(KiiObject kiiObject) throws JSONException {
        super(kiiObject);
        owner = source.getString(OWNER);
        info = new BookInfo(source.getJSONObject(INFO));
        condition = new BookCondition(source.getJSONObject(CONDITION));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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
        return Kii.bucket("appbooks");
    }

    @Override
    public KiiObject createNewKiiObject() throws KiiModelException {
        KiiObject object = bucket().object();
        return setValues(object);
    }

    @Override
    public KiiObject createNewKiiObject(String id) throws KiiModelException {
        KiiObject object = bucket().object(id);
        return setValues(object);
    }

    private KiiObject setValues(KiiObject object) throws KiiModelException {
        object.set(OWNER, owner);
        try {
            object.set(INFO, info.toJSON());
            object.set(CONDITION, condition.toJSON());
        } catch (JSONException e) {
            throw new KiiModelException(e);
        }
        return object;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.source, flags);
        dest.writeString(this.id);
        dest.writeString(this.owner);
        dest.writeParcelable(this.info, flags);
        dest.writeParcelable(this.condition, flags);
    }

    protected Book(Parcel in) {
        this.source = in.readParcelable(KiiObject.class.getClassLoader());
        this.id = in.readString();
        this.owner = in.readString();
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
