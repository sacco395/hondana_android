package com.books.hondana.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kii.cloud.storage.KiiObject;

import java.util.ArrayList;
import java.util.List;

/**
 * KiiCloud には以下のように保存される
 * {
 *     ...
 *     "genre": {
 *         "genre_1": value1,
 *         "genre_2": value2,
 *         "genre_3": value3,
 *         "genre_4": value4,
 *         "genre_5": value5,
 *     }
 *     ...
 * }
 */
public class GenreList implements Parcelable {

    // KiiCloud 上のフィールド名
    public static final String GENRE_1 = "genre_1";
    public static final String GENRE_2 = "genre_2";
    public static final String GENRE_3 = "genre_3";
    public static final String GENRE_4 = "genre_4";
    public static final String GENRE_5 = "genre_5";

    private List<String> values;

    public GenreList() {
        values = new ArrayList<>(5);
        values.add("001");
        values.add("");
        values.add("");
        values.add("");
        values.add("");
    }

    public GenreList(List<String> values) {
        this.values = values;
    }

    public List<String> getValues() {
        return values;
    }

    public void getValueFrom(KiiObject object) {
        values = new ArrayList<>();
        values.add(object.getString(GENRE_1));
        values.add(object.getString(GENRE_2));
        values.add(object.getString(GENRE_3));
        values.add(object.getString(GENRE_4));
        values.add(object.getString(GENRE_5));
    }

    public void putValueInto(KiiObject object) {
        object.set(GENRE_1, values.get(0));
        object.set(GENRE_2, values.get(1));
        object.set(GENRE_3, values.get(2));
        object.set(GENRE_4, values.get(3));
        object.set(GENRE_5, values.get(4));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.values);
    }

    protected GenreList(Parcel in) {
        this.values = in.createStringArrayList();
    }

    public static final Parcelable.Creator<GenreList> CREATOR = new Parcelable.Creator<GenreList>() {
        @Override
        public GenreList createFromParcel(Parcel source) {
            return new GenreList(source);
        }

        @Override
        public GenreList[] newArray(int size) {
            return new GenreList[size];
        }
    };
}
