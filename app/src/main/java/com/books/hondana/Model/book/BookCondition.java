package com.books.hondana.Model.book;

import android.os.Parcel;
import android.os.Parcelable;

import com.books.hondana.Model.JSONConvertible;
import com.books.hondana.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/9/16.
 */
public class BookCondition implements JSONConvertible, Parcelable {

    // KiiCloud 上のフィールド名
    public static final String LINED = "lined";
    public static final String FOLDED = "folded";
    public static final String SUNBURNED = "sunburned";
    public static final String SCRATCHED = "scratched";
    public static final String HAS_BAND = "hasBand";
    public static final String EVALUATION = "evaluation";
    public static final String SMELL = "smell";

    /**
     * 下線などの書き込み
     * 0: x = 0
     * 1: 0 < x < 5
     * 2: 5 < x < 10
     * 3: 10 < x
     */
    private int lined;

    /**
     * 折れ
     * 0: x = 0
     * 1: 0 < x < 5
     * 2: 5 < x < 10
     * 3: 10 < x
     */
    private int folded;

    // 日焼け
    private boolean sunburned;

    // 傷
    private boolean scratched;

    // 帯
    private boolean hasBand;

    /**
     * 総合的な評価。良い、普通、汚れあり
     * 0: 良い
     * 1: 普通
     * 2: 汚れあり
     *
     * 初期値は普通の 1
     */
    private int evaluation = 1;

    private Smell smell;

    private String note;

    public BookCondition() {
    }

    public BookCondition(JSONObject json) throws JSONException {
        lined = json.getInt(LINED);
        folded = json.getInt(FOLDED);
        sunburned = json.getBoolean(SUNBURNED);
        scratched = json.getBoolean(SCRATCHED);
        hasBand = json.getBoolean(HAS_BAND);
        evaluation = json.getInt(EVALUATION);
        smell = new Smell(json.getJSONObject(SMELL));
        note = json.getString(note);
    }

    public int getLined() {
        return lined;
    }

    public void setLined(int lined) {
        this.lined = lined;
    }

    public int getFolded() {
        return folded;
    }

    public void setFolded(int folded) {
        this.folded = folded;
    }

    public boolean isSunburned() {
        return sunburned;
    }

    public void setSunburned(boolean sunburned) {
        this.sunburned = sunburned;
    }

    public boolean isScratched() {
        return scratched;
    }

    public void setScratched(boolean scratched) {
        this.scratched = scratched;
    }

    public boolean isHasBand() {
        return hasBand;
    }

    public void setHasBand(boolean hasBand) {
        this.hasBand = hasBand;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public Smell getSmell() {
        return smell;
    }

    public void setSmell(Smell smell) {
        this.smell = smell;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getIconDrawableResId() {
        switch (evaluation) {
            case 0:
                return R.drawable.book_icon_excellent;
            case 1:
                return R.drawable.book_icon_good;
            case 2:
                return R.drawable.book_icon_bad;
            default:
                return 1;
        }
    }

    public String getSunburnedText() {
        if (sunburned) {
            return "日焼け・変色";
        } else {
            return "";
        }
    }

    public String getScratchedText() {
        if (scratched) {
            return "スレ・傷など";
        } else {
            return "";
        }
    }

    public String getBandText() {
        if (hasBand) {
            return "帯あり";
        } else {
            return "";
        }
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(LINED, lined);
        json.put(FOLDED, folded);
        json.put(SUNBURNED, sunburned);
        json.put(SCRATCHED, scratched);
        json.put(HAS_BAND, hasBand);
        json.put(EVALUATION, evaluation);
        json.put(SMELL, smell.toJSON());
        return json;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.lined);
        dest.writeInt(this.folded);
        dest.writeByte(this.sunburned ? (byte) 1 : (byte) 0);
        dest.writeByte(this.scratched ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasBand ? (byte) 1 : (byte) 0);
        dest.writeInt(this.evaluation);
        dest.writeParcelable(this.smell, flags);
        dest.writeString(this.note);
    }

    protected BookCondition(Parcel in) {
        this.lined = in.readInt();
        this.folded = in.readInt();
        this.sunburned = in.readByte() != 0;
        this.scratched = in.readByte() != 0;
        this.hasBand = in.readByte() != 0;
        this.evaluation = in.readInt();
        this.smell = in.readParcelable(Smell.class.getClassLoader());
        this.note = in.readString();
    }

    public static final Creator<BookCondition> CREATOR = new Creator<BookCondition>() {
        @Override
        public BookCondition createFromParcel(Parcel source) {
            return new BookCondition(source);
        }

        @Override
        public BookCondition[] newArray(int size) {
            return new BookCondition[size];
        }
    };
}
