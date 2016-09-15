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

    private GenreList genres;

    /**
     * 新規に Book オブジェクトを生成する
     * @return
     */
    public static Book createNew() {
        return new Book();
    }

    /**
     * KiiCloud 上に保存されているものを、KiiObject から生成
     * @param kiiObject
     * @return
     * @throws JSONException 求めるフィールドがなかった場合に例外をスロー
     */
    public static Book createFrom(KiiObject kiiObject) throws JSONException {
        return new Book(kiiObject);
    }

    private Book() {
        ownerId = "";
        info = new BookInfo();
        condition = new BookCondition();
        genres = new GenreList();
    }

    private Book(KiiObject kiiObject) throws JSONException {
        super(kiiObject);
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

    public GenreList getGenres() {
        return genres;
    }

    public void setGenres(GenreList genres) {
        this.genres = genres;
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
        genres = new GenreList();
        genres.getValueFrom(createKiiObject());
    }

    @Override
    protected KiiObject createKiiObject() throws JSONException {
        if (source == null) {
            source = bucket().object();
        }
        source.set(OWNER_ID, ownerId);
        source.set(INFO, info.toJSON());
        source.set(CONDITION, condition.toJSON());
        genres.putValueInto(source);
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
        dest.writeString(this.ownerId);
        dest.writeParcelable(this.info, flags);
        dest.writeParcelable(this.condition, flags);
        dest.writeParcelable(this.genres, flags);
    }

    protected Book(Parcel in) {
        this.source = in.readParcelable(KiiObject.class.getClassLoader());
        this.id = in.readString();
        this.createdAt = in.readLong();
        this.updatedAt = in.readLong();
        this.ownerId = in.readString();
        this.info = in.readParcelable(BookInfo.class.getClassLoader());
        this.condition = in.readParcelable(BookCondition.class.getClassLoader());
        this.genres = in.readParcelable(GenreList.class.getClassLoader());
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
