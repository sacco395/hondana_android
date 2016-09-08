package com.books.hondana.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kii.cloud.storage.KiiObject;

/**
 * Created by sacco on 2016/08/21.
 */
public class Member extends KiiDataObj implements Parcelable {

    public static final String USER_ID = "user_id";
    public static final String NAME = "name";
    public static final String BIRTHDAY = "birthday";
    public static final String ADDRESS = "address";
    public static final String PROFILE = "profile";
    public static final String IMAGE_URL = "image_Url";
    public static final String POINT = "point";
    public static final String FAVORITE_AUTHOR1 = "favorite_author1";
    public static final String FAVORITE_AUTHOR2 = "favorite_author2";
    public static final String FAVORITE_AUTHOR3 = "favorite_author3";
    public static final String DELETE_FLG = "delete_flg";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATE_AT = "updated_at";

    // Constructor
    public Member() {
        super();
        // application scope の kiiBucket の Objectとして設定
        kiiDataInitialize(KiiCloudBucket.MEMBERS);
    }

    // KiiCloud
    public Member(KiiObject kiiObject){
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

    protected Member(Parcel in) {
        super (in);
    }

    public static final Creator<Member> CREATOR = new Creator<Member> () {
        @Override
        public Member createFromParcel(Parcel source) {
            return new Member (source);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };
}