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
public class Smell extends JSONConvertible implements Parcelable {

    // KiiCloud 上のフィールド名
    public static final String CIGAR = "cigar";
    public static final String PET = "pet";
    public static final String MOLD = "mold";

    private boolean cigar;

    private boolean pet;

    private boolean mold;

    public Smell() {
    }

    public Smell(JSONObject json) throws JSONException {
        super(json);
    }

    public boolean hasCigar() {
        return cigar;
    }

    public void setCigar(boolean cigar) {
        this.cigar = cigar;
    }

    public boolean hasPet() {
        return pet;
    }

    public void setPet(boolean pet) {
        this.pet = pet;
    }

    public boolean hasMold() {
        return mold;
    }

    public void setMold(boolean mold) {
        this.mold = mold;
    }

    public String getCigarSmellText() {
        if (cigar) {
            // TODO: 9/10/16 文言修正
            return "タバコ臭あり";
        } else {
            return "";//タバコ臭なし
        }
    }

    public String getPetSmellText() {
        if (cigar) {
            // TODO: 9/10/16 文言修正
            return "ペット臭あり";
        } else {
            return "";//ペット臭なし
        }
    }

    public String getMoldSmellText() {
        if (mold) {
            // TODO: 9/10/16 文言修正
            return "カビ臭あり";
        } else {
            return "";//カビ臭なし
        }
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(CIGAR, cigar);
        json.put(PET, pet);
        json.put(MOLD, mold);
        return json;
    }

    @Override
    public void setValues(JSONObject json) throws JSONException {
        cigar = json.getBoolean(CIGAR);
        pet = json.getBoolean(PET);
        mold = json.getBoolean(MOLD);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.cigar ? (byte) 1 : (byte) 0);
        dest.writeByte(this.pet ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mold ? (byte) 1 : (byte) 0);
    }

    protected Smell(Parcel in) {
        this.cigar = in.readByte() != 0;
        this.pet = in.readByte() != 0;
        this.mold = in.readByte() != 0;
    }

    public static final Creator<Smell> CREATOR = new Creator<Smell>() {
        @Override
        public Smell createFromParcel(Parcel source) {
            return new Smell(source);
        }

        @Override
        public Smell[] newArray(int size) {
            return new Smell[size];
        }
    };
}