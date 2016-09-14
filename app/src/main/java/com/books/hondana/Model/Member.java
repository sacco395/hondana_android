package com.books.hondana.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.books.hondana.Model.abst.KiiModel;
import com.books.hondana.Model.exception.KiiModelException;
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
    public static final String BIRTHDAY = "birthday";
    public static final String ADDRESS = "address";
    public static final String PROFILE = "profile";
    public static final String IMAGE_URL = "image_url";
    public static final String POINT = "point";
    public static final String FAVORITE_AUTHORS = "favorite_authors";
    public static final String DELETED = "deleted";

    private String name;

    private String birthday;

    private String address;

    private String profile;

    private String imageUrl;

    private int point;

    private AuthorList favoriteAuthors;

    private boolean deleted;

    private long createdAt;

    private long updatedAt;

    /**
     * KiiUser から Member を新規生成
     * @param kiiUser id, name を取得し、生成される Member にセット
     * @return KiiObject を source フィールドに持ち、id, name は KiiUser と同一のものを持つ
     * @throws KiiModelException id が無効な場合
     *        （KiiUser#getID がnull、もしくはすでに同じ id を持つ Member が存在する時）にスロー
     *         ユーザとメンバーの一対一対応が担保されなくなるので、致命的
     */
    public static Member createFrom(KiiUser kiiUser) throws KiiModelException {
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
        return member;
    }

    public Member() {
        name = "";
        birthday = "";
        address = "";
        profile = "";
        imageUrl = "";
        name = "";
        favoriteAuthors = new AuthorList();
    }

    public Member(KiiObject kiiObject) throws JSONException {
        super(kiiObject);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        birthday = kiiObject.getString(BIRTHDAY);
        address = kiiObject.getString(ADDRESS);
        profile = kiiObject.getString(PROFILE);
        imageUrl = kiiObject.getString(IMAGE_URL);
        point = kiiObject.getInt(POINT);
        favoriteAuthors = new AuthorList(kiiObject.getJSONObject(FAVORITE_AUTHORS));
        deleted = kiiObject.getBoolean(DELETED);
    }

    @Override
    public KiiObject createKiiObject() throws JSONException {
        if (source == null) {
            source = bucket().object();
        }
        source.set(NAME, name);
        source.set(BIRTHDAY, birthday);
        source.set(ADDRESS, address);
        source.set(PROFILE, profile);
        source.set(IMAGE_URL, imageUrl);
        source.set(POINT, point);
        source.set(FAVORITE_AUTHORS, favoriteAuthors.toJSON());
        source.set(DELETED, deleted);
        return source;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.birthday);
        dest.writeString(this.address);
        dest.writeString(this.profile);
        dest.writeString(this.imageUrl);
        dest.writeInt(this.point);
        dest.writeParcelable(this.favoriteAuthors, flags);
        dest.writeByte(this.deleted ? (byte) 1 : (byte) 0);
        dest.writeLong(this.createdAt);
        dest.writeLong(this.updatedAt);
    }

    protected Member(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.birthday = in.readString();
        this.address = in.readString();
        this.profile = in.readString();
        this.imageUrl = in.readString();
        this.point = in.readInt();
        this.favoriteAuthors = in.readParcelable(AuthorList.class.getClassLoader());
        this.deleted = in.readByte() != 0;
        this.createdAt = in.readLong();
        this.updatedAt = in.readLong();
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
