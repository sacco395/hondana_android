package com.books.hondana.Model.book;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/9/16.
 */
public class Smell implements Parcelable {

    private String cigar;

    private String pet;

    private String mold;

    public Smell() {
    }

    public Smell(String cigar, String pet, String mold) {
        this.cigar = cigar;
        this.pet = pet;
        this.mold = mold;
    }

    public String getCigar() {
        return cigar;
    }

    public void setCigar(String cigar) {
        this.cigar = cigar;
    }

    public String getPet() {
        return pet;
    }

    public void setPet(String pet) {
        this.pet = pet;
    }

    public String getMold() {
        return mold;
    }

    public void setMold(String mold) {
        this.mold = mold;
    }

    public String getCigarSmellText() {
        if (cigar == null) {
            return "";
        }
        switch (cigar) {
            case "ペットを飼っている": return "ペットを飼っている";
            default: return "";
        }
    }

    public String getPetSmellText() {
        if (pet == null) {
            return "";
        }
        switch (pet) {
            case "ペットを飼っている": return "ペットを飼っている";
            default: return "";
        }
    }

    public String getMoldSmellText() {
        if (mold == null) {
            return "";
        }
        switch (mold) {
            case "カビ臭": return "カビ臭";
            default: return "";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cigar);
        dest.writeString(this.pet);
        dest.writeString(this.mold);
    }

    protected Smell(Parcel in) {
        this.cigar = in.readString();
        this.pet = in.readString();
        this.mold = in.readString();
    }

    public static final Parcelable.Creator<Smell> CREATOR = new Parcelable.Creator<Smell>() {
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