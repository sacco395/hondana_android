package com.books.hondana.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kii.cloud.storage.KiiObject;

/**
 * Created by sacco on 2016/08/21.
 */
public class UserBook extends KiiDataObj implements Parcelable {


    public static final String USERBOOK_ID = "userbook_id";
    public static final String USER_ID = "user_id";
    public static final String BOOKS_ID = "books_id";
    public static final String HEIGHT = "height";
    public static final String DEPTH = "depth";
    public static final String WEIGHT = "weight";
    public static final String CONDITON = "condition";
    public static final String SUNBURNED = "sunburned";
    public static final String LINED = "lined";
    public static final String BROKEN = "broken";
    public static final String BAND = "band";
    public static final String CIGAR_SMELL = "cigar_smell";
    public static final String PET_SMELL = "pet_smell";
    public static final String MOLD_SMELL = "mold_smell";
    public static final String REMARK = "remark";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";

    // Constructor
    public UserBook() {
        super();
        // application scope の kiiBucket の Objectとして設定
        kiiDataInitialize(KiiCloudBucket.USERBOOKS);
    }

    // KiiCloud
    public UserBook(KiiObject kiiObject){
        super(kiiObject);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel (dest, flags);
    }

    protected UserBook(Parcel in) {
        super (in);
    }

    public static final Creator<UserBook> CREATOR = new Creator<UserBook> () {
        @Override
        public UserBook createFromParcel(Parcel source) {
            return new UserBook (source);
        }

        @Override
        public UserBook[] newArray(int size) {
            return new UserBook[size];
        }
    };
}
