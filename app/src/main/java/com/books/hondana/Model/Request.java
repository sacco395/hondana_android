package com.books.hondana.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.books.hondana.Model.book.Book;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/9/16.
 */
public class Request implements Parcelable {

    private String id;

    private String clientUserId;

    private String serverUserId;

    private Book book;

    public Request() {
    }

    public Request(String id, String clientUserId, String serverUserId, Book book) {
        this.id = id;
        this.clientUserId = clientUserId;
        this.serverUserId = serverUserId;
        this.book = book;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientUserId() {
        return clientUserId;
    }

    public void setClientUserId(String clientUserId) {
        this.clientUserId = clientUserId;
    }

    public String getServerUserId() {
        return serverUserId;
    }

    public void setServerUserId(String serverUserId) {
        this.serverUserId = serverUserId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.clientUserId);
        dest.writeString(this.serverUserId);
        dest.writeParcelable(this.book, flags);
    }

    protected Request(Parcel in) {
        this.id = in.readString();
        this.clientUserId = in.readString();
        this.serverUserId = in.readString();
        this.book = in.readParcelable(Book.class.getClassLoader());
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel source) {
            return new Request(source);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };
}
