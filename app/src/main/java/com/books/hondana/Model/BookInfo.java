package com.books.hondana.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.books.hondana.Model.abst.JSONConvertible;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 書誌的なデータ。別の所有者による同じ本が共通して持つ情報を担う
 */
public class BookInfo extends JSONConvertible implements Parcelable {

    // KiiCloud 上のフィールド名
    public static final String TITLE = "title";
    public static final String PUBLISHER = "publisher";
    public static final String AUTHOR = "author";
    public static final String ISBN = "isbn";
    public static final String LANGUAGE = "language";
    public static final String ISSUE_DATE = "issue_date";
    public static final String IMAGE_URL = "image_url";
    public static final String SIZE = "size";

    private String title;

    private String publisher;

    private String author;

    private String isbn;

    private String language;

    private String issueDate;

    private String imageUrl;

    private Size size;

    public BookInfo() {
        title = "";
        author = "";
        isbn = "";
        language = "";
        issueDate = "";
        imageUrl = "";
        size = new Size();
    }

    public BookInfo(JSONObject json) throws JSONException {
        super(json);
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

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    @Override
    public void setValues(JSONObject json) throws JSONException {
        title = json.getString(TITLE);
        publisher = json.getString(PUBLISHER);
        author = json.getString(AUTHOR);
        isbn = json.getString(ISBN);
        language = json.getString(LANGUAGE);
        issueDate = json.getString(ISSUE_DATE);
        imageUrl = json.getString(IMAGE_URL);
        size = new Size(json.getJSONObject(SIZE));
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(TITLE, title);
        json.put(PUBLISHER, publisher);
        json.put(AUTHOR, author);
        json.put(ISBN, isbn);
        json.put(LANGUAGE, language);
        json.put(ISSUE_DATE, issueDate);
        json.put(IMAGE_URL, imageUrl);
        json.put(SIZE, size.toJSON());
        return json;
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
        dest.writeParcelable(this.size, flags);
    }

    protected BookInfo(Parcel in) {
        this.title = in.readString();
        this.publisher = in.readString();
        this.author = in.readString();
        this.isbn = in.readString();
        this.language = in.readString();
        this.issueDate = in.readString();
        this.imageUrl = in.readString();
        this.size = in.readParcelable(Size.class.getClassLoader());
    }

    public static final Creator<BookInfo> CREATOR = new Creator<BookInfo>() {
        @Override
        public BookInfo createFromParcel(Parcel source) {
            return new BookInfo(source);
        }

        @Override
        public BookInfo[] newArray(int size) {
            return new BookInfo[size];
        }
    };
}
