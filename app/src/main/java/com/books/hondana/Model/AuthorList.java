package com.books.hondana.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.books.hondana.Model.abst.JSONConvertible;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作家のリスト
 * KiiCloud 上には、以下のように保存される
 * {
 *     ...
 *     "authors": {
 *         "author1": value1,
 *         "author2": value2,
 *         "author3": value3
 *     },
 *     ...
 * }
 */
public class AuthorList extends JSONConvertible implements Parcelable {

    public static final String AUTHOR_1 = "author1";
    public static final String AUTHOR_2 = "author2";
    public static final String AUTHOR_3 = "author3";

    private List<String> values;

    // デフォルトの値をセット
    public AuthorList() {
        values = new ArrayList<>();
        values.add("");
        values.add("");
        values.add("");
    }

    public AuthorList(JSONObject json) throws JSONException {
        super(json);
    }

    public AuthorList(List<String> values) {
        this.values = values;
    }

    public List<String> getValues() {
        return values;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(AUTHOR_1, values.get(0));
        json.put(AUTHOR_2, values.get(1));
        json.put(AUTHOR_3, values.get(2));
        return json;
    }

    @Override
    public void setValues(JSONObject json) throws JSONException {
        values = new ArrayList<>(3);
        values.add(json.getString(AUTHOR_1));
        values.add(json.getString(AUTHOR_2));
        values.add(json.getString(AUTHOR_3));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.values);
    }

    protected AuthorList(Parcel in) {
        this.values = in.createStringArrayList();
    }

    public static final Creator<AuthorList> CREATOR = new Creator<AuthorList>() {
        @Override
        public AuthorList createFromParcel(Parcel source) {
            return new AuthorList(source);
        }

        @Override
        public AuthorList[] newArray(int size) {
            return new AuthorList[size];
        }
    };
}
