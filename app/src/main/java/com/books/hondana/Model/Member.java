package com.books.hondana.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.books.hondana.model.abst.KiiModel;
import com.books.hondana.model.exception.KiiModelException;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;

import org.json.JSONException;

/**
 * 登録されている KiiUser と一対一対応になるオブジェクト
 * ID は KiiUser の ID と同一なので、一対一対応が担保される
 */
public class Member extends KiiModel implements Parcelable {

    public static final String BUCKET_NAME = "members";

    // KiiCloud 上のフィールド名
    public static final String NAME = "name";
    public static final String FULL_NAME = "full_name";
    public static final String PHONE = "phone";
    public static final String BIRTHDAY = "birthday";
    public static final String ADDRESS = "address";
    public static final String PROFILE = "profile";
    public static final String IMAGE_URL = "image_url";
    public static final String POINT = "point";
    public static final String POINTS_BY_BOOKS = "points_by_books";
    public static final String FAVORITE_AUTHORS = "favorite_authors";
    public static final String DELETED = "deleted";
    public static final String TYPE = "type";
    public static final String REQUEST_COUNT = "request_count";
    public static final String LAST_REQUEST_MONTH = "last_request_month";

    private String name;

    private String fullName;

    private String phone;

    private String birthday;

    private String address;

    private String profile;

    private String imageUrl;

    private int point;

    private int pointsByBooks;

    private AuthorList favoriteAuthors;

    private boolean deleted;

    /**
     * 会員の種別
     * 0: 無料会員
     * 1: スタンダード
     * 2: プレミアム
     *
     * 初期値は無料の 0
     */
    private int type;

    private int requestCount;

    private int lastRequestMonth;

    /**
     * すでに KiiCloud 上に保存されているオブジェクトから Member を生成
     * @param kiiObject
     * @return
     * @throws JSONException
     */
    public static Member createFrom(KiiObject kiiObject) throws JSONException {
        return new Member(kiiObject);
    }

    /**
     * KiiUser から Member を新規生成
     * KiiCloud にすでに保存してあるものを取得する場合は、
     * {@link com.books.hondana.connection.KiiMemberConnection} を使用すること
     * @param kiiUser id, name を取得し、生成される Member にセット
     * @return KiiObject を source フィールドに持ち、id, name は KiiUser と同一のものを持つ
     * @throws KiiModelException id が無効な場合
     *        （KiiUser#getID がnull、もしくはすでに同じ id を持つ Member が存在する時）にスロー
     *         ユーザとメンバーの一対一対応が担保されなくなるので、致命的
     */
    public static Member createNew(KiiUser kiiUser) throws KiiModelException {
        String id = kiiUser.getID();
        if (id == null) {
            throw new KiiModelException();
        }
        Member member = new Member();
        if (!KiiObject.isValidObjectID(id)) {
            throw new KiiModelException();
        }
        member.id = id;
        member.source = member.bucket().object(id);
        member.name = kiiUser.getUsername();
        member.phone = kiiUser.getPhone();
        return member;
    }

    private Member() {
        name = "";
        fullName = "";
        phone = "";
        birthday = "";
        address = "";
        profile = "";
        imageUrl = "";
        point = 0;
        pointsByBooks = 0;
        favoriteAuthors = new AuthorList();
        type = 0;
        requestCount = 10;
        lastRequestMonth = 0;
    }

    private Member(KiiObject kiiObject) throws JSONException {
        super(kiiObject);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getPointsByBooks() {
        return pointsByBooks;
    }

    public void setPointsByBooks(int pointsByBooks) {
        this.pointsByBooks = pointsByBooks;
    }

    public AuthorList getFavoriteAuthors() {
        return favoriteAuthors;
    }

    public void setFavoriteAuthors(AuthorList favoriteAuthors) {
        this.favoriteAuthors = favoriteAuthors;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    public int getLastRequestMonth() {
        return lastRequestMonth;
    }

    public void setLastRequestMonth(int lastRequestMonth) {
        this.lastRequestMonth = lastRequestMonth;
    }


    public boolean hasValidImageUrl() {
        return !(imageUrl == null || imageUrl.equals(""));
    }

    @Override
    public KiiBucket bucket() {
        return Kii.bucket(BUCKET_NAME);
    }

    @Override
    public void setValuesFrom(KiiObject kiiObject) throws JSONException {
        name = kiiObject.getString(NAME);
        fullName = kiiObject.getString(FULL_NAME);
        phone = kiiObject.getString(PHONE);
        birthday = kiiObject.getString(BIRTHDAY);
        address = kiiObject.getString(ADDRESS);
        profile = kiiObject.getString(PROFILE);
        imageUrl = kiiObject.getString(IMAGE_URL);
        point = kiiObject.getInt(POINT);
        pointsByBooks = kiiObject.getInt(POINTS_BY_BOOKS);
        favoriteAuthors = new AuthorList(kiiObject.getJSONObject(FAVORITE_AUTHORS));
        deleted = kiiObject.getBoolean(DELETED);
        type = kiiObject.getInt(TYPE);
        requestCount = kiiObject.getInt(REQUEST_COUNT);
        lastRequestMonth = kiiObject.getInt(LAST_REQUEST_MONTH);
    }

    @Override
    protected KiiObject createKiiObject() throws JSONException {
        if (source == null) {
            source = bucket().object();
        }
        source.set(NAME, name);
        source.set(FULL_NAME, fullName);
        source.set(PHONE, phone);
        source.set(BIRTHDAY, birthday);
        source.set(ADDRESS, address);
        source.set(PROFILE, profile);
        source.set(IMAGE_URL, imageUrl);
        source.set(POINT, point);
        source.set(POINTS_BY_BOOKS, pointsByBooks);
        source.set(FAVORITE_AUTHORS, favoriteAuthors.toJSON());
        source.set(DELETED, deleted);
        source.set(TYPE, type);
        source.set(REQUEST_COUNT, requestCount);
        source.set(LAST_REQUEST_MONTH, lastRequestMonth);
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
        dest.writeString(this.name);
        dest.writeString(this.fullName);
        dest.writeString(this.phone);
        dest.writeString(this.birthday);
        dest.writeString(this.address);
        dest.writeString(this.profile);
        dest.writeString(this.imageUrl);
        dest.writeInt(this.point);
        dest.writeInt(this.pointsByBooks);
        dest.writeParcelable(this.favoriteAuthors, flags);
        dest.writeByte(this.deleted ? (byte) 1 : (byte) 0);
        dest.writeInt(this.type);
        dest.writeInt(this.requestCount);
        dest.writeInt(this.lastRequestMonth);
    }

    protected Member(Parcel in) {
        this.source = in.readParcelable(KiiObject.class.getClassLoader());
        this.id = in.readString();
        this.createdAt = in.readLong();
        this.updatedAt = in.readLong();
        this.name = in.readString();
        this.fullName = in.readString();
        this.phone = in.readString();
        this.birthday = in.readString();
        this.address = in.readString();
        this.profile = in.readString();
        this.imageUrl = in.readString();
        this.point = in.readInt();
        this.pointsByBooks = in.readInt();
        this.favoriteAuthors = in.readParcelable(AuthorList.class.getClassLoader());
        this.deleted = in.readByte() != 0;
        this.type = in.readInt();
        this.requestCount = in.readInt();
        this.lastRequestMonth = in.readInt();
    }

    public static final Creator<Member> CREATOR = new Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel source) {
            return new Member(source);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };
}
