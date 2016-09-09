package com.books.hondana.Model.book;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/9/16.
 */
public class Size implements Parcelable {

    private double height;

    private double width;

    private double depth;

    private double weight;

    public Size() {}

    public Size(double height, double width, double depth, double weight) {
        this.height = height;
        this.width = width;
        this.depth = depth;
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.height);
        dest.writeDouble(this.width);
        dest.writeDouble(this.depth);
        dest.writeDouble(this.weight);
    }

    protected Size(Parcel in) {
        this.height = in.readDouble();
        this.width = in.readDouble();
        this.depth = in.readDouble();
        this.weight = in.readDouble();
    }

    public static final Parcelable.Creator<Size> CREATOR = new Parcelable.Creator<Size>() {
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
