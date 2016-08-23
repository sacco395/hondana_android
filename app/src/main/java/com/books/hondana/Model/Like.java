package com.books.hondana.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kii.cloud.storage.KiiObject;

/**
 * Created by asumi on 2016/08/21.
 */
public class kiiLike extends KiiDataObj implements Parcelable {

    public static final String LIKE_ID = "like_id";
    public static final String BOOK_ID = "book_id";
    public static final String MEMBER_ID = "member_id";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";

    // Constructor
    public KiiLike() {
        super();
        // application scope の kiiBucket の Objectとして設定
        kiiDataInitialize(KiiCloudBucket.LINKS);
    }

    // KiiCloud
    public KiiLike(KiiObject kiiObject){
        super(kiiObject);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected KiiLike(Parcel in) {
        super(in);
    }

    public static final Creator<KiiLike> CREATOR = new Creator<KiiLike>() {
        @Override
        public KiiLike createFromParcel(Parcel source) {
            return new KiiLike(source);
        }

        @Override
        public KiiLike[] newArray(int size) {
            return new KiiLike[size];
        }
    };



}
