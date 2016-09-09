package com.books.hondana.Model.book;

import android.os.Parcel;
import android.os.Parcelable;

import com.books.hondana.R;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/9/16.
 */
public class Condition implements Parcelable {

    // 下線などの書き込み
    private String lined;

    // 折れ
    private String folded;

    // 日焼け
    private String sunburned;

    // 傷
    private String scratched;

    // メモなどの書き込み
    private String noted;

    private String band;

    // 総合的な評価。きれい、汚い、普通など
    private String evaluation;

    public Condition() {
    }

    public Condition(String lined, String folded, String sunburned, String scratched, String noted, String band, String evaluation) {
        this.lined = lined;
        this.folded = folded;
        this.sunburned = sunburned;
        this.scratched = scratched;
        this.noted = noted;
        this.band = band;
        this.evaluation = evaluation;
    }

    public String getLined() {
        return lined;
    }

    public void setLined(String lined) {
        this.lined = lined;
    }

    public String getFolded() {
        return folded;
    }

    public void setFolded(String folded) {
        this.folded = folded;
    }

    public String getSunburned() {
        return sunburned;
    }

    public void setSunburned(String sunburned) {
        this.sunburned = sunburned;
    }

    public String getScratched() {
        return scratched;
    }

    public void setScratched(String scratched) {
        this.scratched = scratched;
    }

    public String getNoted() {
        return noted;
    }

    public void setNoted(String noted) {
        this.noted = noted;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public static Creator<Condition> getCREATOR() {
        return CREATOR;
    }

    public int getIconDrawableResId() {
        if (evaluation == null) {
            return 0;
        }
        switch (evaluation) {
            case "良い":
                return R.drawable.book_icon_excellent;
            case "普通":
                return R.drawable.book_icon_good;
            case "汚れあり":
                return R.drawable.book_icon_bad;
            default:
                return 0;
        }
    }

    public String getSunburnedText() {
        if (sunburned == null) {
            return "";
        }
        switch (sunburned) {
            case "日焼け・変色": return "日焼け・変色";
            default: return "";
        }
    }

    public String getScratchedText() {
        if (scratched == null) {
            return "";
        }
        switch (scratched) {
            case "スレ・傷など": return "スレ・傷など";
            default: return "";
        }
    }

    public String getBandText() {
        if (band == null) {
            return "";
        }
        switch (band) {
            case "帯あり": return "帯あり";
            default: return "";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.lined);
        dest.writeString(this.folded);
        dest.writeString(this.sunburned);
        dest.writeString(this.scratched);
        dest.writeString(this.noted);
        dest.writeString(this.band);
        dest.writeString(this.evaluation);
    }

    protected Condition(Parcel in) {
        this.lined = in.readString();
        this.folded = in.readString();
        this.sunburned = in.readString();
        this.scratched = in.readString();
        this.noted = in.readString();
        this.band = in.readString();
        this.evaluation = in.readString();
    }

    public static final Parcelable.Creator<Condition> CREATOR = new Parcelable.Creator<Condition>() {
        @Override
        public Condition createFromParcel(Parcel source) {
            return new Condition(source);
        }

        @Override
        public Condition[] newArray(int size) {
            return new Condition[size];
        }
    };
}
