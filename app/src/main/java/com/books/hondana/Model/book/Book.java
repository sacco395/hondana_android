package com.books.hondana.Model.book;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/9/16.
 */
public class Book implements Parcelable {

    private String id;

    private String ownerId;

    private Info info;

    private Size size;

    private Smell smell;

    private Condition condition;

    private List<String> genres = new ArrayList<>(5);

    private String description;

    private long createdAt;

    private long updatedAt;

    public Book() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Smell getSmell() {
        return smell;
    }

    public void setSmell(Smell smell) {
        this.smell = smell;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        dest.writeString(this.ownerId);
        dest.writeParcelable(this.info, flags);
        dest.writeParcelable(this.size, flags);
        dest.writeParcelable(this.smell, flags);
        dest.writeParcelable(this.condition, flags);
        dest.writeStringList(this.genres);
        dest.writeString(this.description);
        dest.writeLong(this.createdAt);
        dest.writeLong(this.updatedAt);
    }

    protected Book(Parcel in) {
        this.id = in.readString();
        this.ownerId = in.readString();
        this.info = in.readParcelable(Info.class.getClassLoader());
        this.size = in.readParcelable(Size.class.getClassLoader());
        this.smell = in.readParcelable(Smell.class.getClassLoader());
        this.condition = in.readParcelable(Condition.class.getClassLoader());
        this.genres = in.createStringArrayList();
        this.description = in.readString();
        this.createdAt = in.readLong();
        this.updatedAt = in.readLong();
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
