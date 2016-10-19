package com.books.hondana.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.books.hondana.model.abst.JSONConvertible;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/9/16.
 */
public class Size extends JSONConvertible implements Parcelable {

    // KiiCloud 上のフィールド名
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String THICKNESS = "thickness";
    private static final String WEIGHT = "weight";
    private static final String BIGGER_THAN_CLICKPOST = "bigger_than_clickpost";

    private double width;

    private double height;

    private double thickness;

    private double weight;

    private boolean biggerThanClickpost;

    public Size() {
        biggerThanClickpost = false;
    }

    public Size(JSONObject json) throws JSONException {
        super(json);
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getThickness() {
        return thickness;
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isBiggerThanClickpost() {
        return biggerThanClickpost;
    }

    public void setBiggerThanClickpost(boolean biggerThanClickpost) {
        this.biggerThanClickpost = biggerThanClickpost;
    }

    public String getBiggerThanClickpostText() {
        if (biggerThanClickpost) {
            return "この本は規格外のため、発送方法は発送者に委ねられて着払いになります";
        } else {
            return "";
        }
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(WIDTH, width);
        json.put(HEIGHT, height);
        json.put(THICKNESS, thickness);
        json.put(WEIGHT, weight);
        json.put(BIGGER_THAN_CLICKPOST, biggerThanClickpost);
        return json;
    }

    @Override
    public void setValues(JSONObject json) throws JSONException {
        width = json.getDouble(WIDTH);
        height = json.getDouble(HEIGHT);
        thickness = json.getDouble(THICKNESS);
        weight = json.getDouble(WEIGHT);
        biggerThanClickpost = json.getBoolean(BIGGER_THAN_CLICKPOST);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.width);
        dest.writeDouble(this.height);
        dest.writeDouble(this.thickness);
        dest.writeDouble(this.weight);
        dest.writeByte(this.biggerThanClickpost ? (byte) 1 : (byte) 0);
    }

    protected Size(Parcel in) {
        this.width = in.readDouble();
        this.height = in.readDouble();
        this.thickness = in.readDouble();
        this.weight = in.readDouble();
        this.biggerThanClickpost = in.readByte() != 0;
    }

    public static final Creator<Size> CREATOR = new Creator<Size>() {
        @Override
        public Size createFromParcel(Parcel source) {
            return new Size(source);
        }

        @Override
        public Size[] newArray(int size) {
            return new Size[size];
        }
    };
}