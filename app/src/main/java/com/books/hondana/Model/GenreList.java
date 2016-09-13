package com.books.hondana.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.books.hondana.Model.abst.JSONConvertible;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/10/16.
 */
public class GenreList extends JSONConvertible implements Parcelable {

    // KiiCloud 上のフィールド名
    public static final String GENRE_1 = "genre_1";
    public static final String GENRE_2 = "genre_2";
    public static final String GENRE_3 = "genre_3";
    public static final String GENRE_4 = "genre_4";
    public static final String GENRE_5 = "genre_5";

    private List<String> values;

    public GenreList() {
        values = new ArrayList<>(5);
        values.add("");
        values.add("");
        values.add("");
        values.add("");
        values.add("");
    }

    public GenreList(JSONObject json) throws JSONException {
        super(json);
    }

    public GenreList(List<String> values) {
        this.values = values;
    }

    public List<String> getValues() {
        return values;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(GENRE_1, values.get(1));
        json.put(GENRE_2, values.get(2));
        json.put(GENRE_3, values.get(3));
        json.put(GENRE_4, values.get(4));
        json.put(GENRE_5, values.get(5));
        return json;
    }

    @Override
    public void setValues(JSONObject json) throws JSONException {
        values = new ArrayList<>(5);
        values.add(json.getString(GENRE_1));
        values.add(json.getString(GENRE_2));
        values.add(json.getString(GENRE_3));
        values.add(json.getString(GENRE_4));
        values.add(json.getString(GENRE_5));
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
