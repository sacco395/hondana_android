package com.books.hondana.Model.book;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/9/16.
 */
public class Info implements Parcelable {

    private String title;

    private String publisher;

    private String author;

    private String isbn;

    private String language;

    private String issueDate;

    private String imageUrl;

    public Info() {
    }

    public Info(String title,
                String publisher,
                String author,
                String isbn,
                String language,
                String issueDate,
                String imageUrl) {
        this.title = title;
        this.publisher = publisher;
        this.author = author;
        this.isbn = isbn;
        this.language = language;
        this.issueDate = issueDate;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.publisher);
        dest.writeString(this.author);
        dest.writeString(this.isbn);
        dest.writeString(this.language);
        dest.writeString(this.issueDate);
        dest.writeString(this.imageUrl);
    }

    protected Info(Parcel in) {
        this.title = in.readString();
        this.publisher = in.readString();
        this.author = in.readString();
        this.isbn = in.readString();
        this.language = in.readString();
        this.issueDate = in.readString();
        this.imageUrl = in.readString();
    }

    public static final Parcelable.Creator<Info> CREATOR = new Parcelable.Creator<Info>() {
        @Override
        public Info createFromParcel(Parcel source) {
            return new Info(source);
        }

        @Override
        public Info[] newArray(int size) {
            return new Info[size];
        }
    };
}
