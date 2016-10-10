package com.books.hondana.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.books.hondana.model.abst.KiiModel;
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
    public static final String OWNER_NAME = "owner_name";
    public static final String INFO = "info";
    public static final String CONDITION = "condition";
    public static final String STATE = "state";

    private String ownerId;

    private String ownerName;

    private BookInfo info;

    private BookCondition condition;

    private GenreList genres;

    /**
     * 本の状態
     * 0: 出品中
     * 1: 取引中
     * 2: 取引完了
     */
    private int state;

    /**
     * 新規に Book オブジェクトを生成する
     * KiiCloud にすでに保存されている場合は、
     * {@link com.books.hondana.connection.KiiBookConnection} を使用する事
     * @return
     */
    public static Book createNew() {
        return new Book();
    }

    /**
     * KiiCloud 上に保存されているものを、KiiObject から生成
     * @param kiiObject
     * @return
     * @throws JSONExceptzion 求めるフィールドがなかった場合に例外をスロー
     */
    public static Book createFrom(KiiObject kiiObject) throws JSONException {
        return new Book(kiiObject);
    }

    private Book() {
        ownerId = "";
        ownerName = "";
        info = new BookInfo();
        condition = new BookCondition();
        genres = new GenreList();
        state = 0;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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

    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }

    public String getStateText() {
        switch (state) {
            case 2:
                return "交換成立済み";//取引完了
            case 1:
                return "リクエスト受付済";//取引中
            default:
                return "";
        }
    }



    @Override
    public KiiBucket bucket() {
        return Kii.bucket(BUCKET_NAME);
    }

    @Override
    public void setValuesFrom(KiiObject object) throws JSONException {
        ownerId = object.getString(OWNER_ID);
        ownerName = object.getString(OWNER_NAME);
        info = new BookInfo(object.getJSONObject(INFO));
        condition = new BookCondition(object.getJSONObject(CONDITION));
        state = object.getInt(STATE);
        genres = new GenreList();
        genres.getValueFrom(createKiiObject());
    }

    @Override
    protected KiiObject createKiiObject() throws JSONException {
        if (source == null) {
            source = bucket().object();
        }
        source.set(OWNER_ID, ownerId);
        source.set(OWNER_NAME, ownerName);
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
        dest.writeString(this.ownerName);
        dest.writeParcelable(this.info, flags);
        dest.writeParcelable(this.condition, flags);
        dest.writeParcelable(this.genres, flags);
        dest.writeInt(this.state);
    }

    protected Book(Parcel in) {
        this.source = in.readParcelable(KiiObject.class.getClassLoader());
        this.id = in.readString();
        this.createdAt = in.readLong();
        this.updatedAt = in.readLong();
        this.ownerId = in.readString();
        this.ownerName = in.readString();
        this.info = in.readParcelable(BookInfo.class.getClassLoader());
        this.condition = in.readParcelable(BookCondition.class.getClassLoader());
        this.genres = in.readParcelable(GenreList.class.getClassLoader());
        this.state = in.readInt();
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
