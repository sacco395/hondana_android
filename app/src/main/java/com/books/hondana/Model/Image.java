package com.books.hondana.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kii.cloud.storage.KiiObject;

/**
 * Created by sacco on 2016/08/31.
 */
public class Image extends KiiDataObj implements Parcelable {


    public static final String APPBOOKS_ID = "appbooks_id";
    public static final String IMAGE_URL = "imageUrl";

    // Constructor
    public Image() {
        super();
        // application scope の kiiBucket の Objectとして設定
        kiiDataInitialize(KiiCloudBucket.IMAGES);
    }

    // KiiCloud
    public Image(KiiObject kiiObject){
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

    protected Image(Parcel in) {
        super (in);
    }

    public static final Creator<Image> CREATOR = new Creator<Image> () {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image (source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
