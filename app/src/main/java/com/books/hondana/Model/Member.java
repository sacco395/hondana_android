package com.books.hondana.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/9/16.
 */
public class Member implements Parcelable {

    private String id;

    private String name;

    private String birthday;

    private String address;

    private String profile;

    private String imageUrl;

    private int point;

    private List<String> favoriteAuthors = new ArrayList<>(3);

    private boolean deleted;

    private long createdAt;

    private long updatedAt;

    public Member() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<String> getFavoriteAuthors() {
        return favoriteAuthors;
    }

    public void setFavoriteAuthors(List<String> favoriteAuthors) {
        this.favoriteAuthors = favoriteAuthors;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
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
        dest.writeStringList(this.favoriteAuthors);
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
        this.favoriteAuthors = in.createStringArrayList();
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
