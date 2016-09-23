package com.books.hondana.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.books.hondana.model.abst.JSONConvertible;
import com.books.hondana.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 本の状態
 * 各本によって異なりうる値を保持
 * KiiCloud には以下のように保存される
 * {
 *     ...
 *     "condition": {
 *         "lined": value,
 *         "folded": value,
 *         "sunburned": value,
 *         "has_band": value,
 *         ...
 *     }
 *     ...
 * }
 */
public class BookCondition extends JSONConvertible implements Parcelable {

    // KiiCloud 上のフィールド名
    public static final String LINED = "lined";
    public static final String FOLDED = "folded";
    public static final String SUNBURNED = "sunburned";
    public static final String SCRATCHED = "scratched";
    public static final String HAS_BAND = "hasBand";
    public static final String EVALUATION = "evaluation";
    public static final String SMELL = "smell";
    public static final String NOTE = "note";

    public static final int LINED_NONE = 0;
    public static final int LINED_ZERO_TO_FIVE = 1;
    public static final int LINED_FIVE_TO_TEN = 2;
    public static final int LINED_MORE_THAN_TEN = 3;

    public static final int FOLDED_NONE = 0;
    public static final int FOLDED_ZERO_TO_FIVE = 1;
    public static final int FOLDED_FIVE_TO_TEN = 2;
    public static final int FOLDED_MORE_THAN_TEN = 3;

    public static final int BROKEN_NONE = 0;
    public static final int BROKEN_ZERO_TO_FIVE = 1;
    public static final int BROKEN_FIVE_TO_TEN = 2;
    public static final int BROKEN_MORE_THAN_TEN = 3;

    public static final int EVALUATION_EXCELLENT = 0;
    public static final int EVALUATION_GOOD = 1;
    public static final int EVALUATION_BAD = 2;

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


    /**
     * 破け
     * 0: x = 0
     * 1: 0 < x < 5
     * 2: 5 < x < 10
     * 3: 10 < x
     */
    private int broken;

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
        smell = new Smell();
        note = "";
    }

    public BookCondition(JSONObject json) throws JSONException {
        super(json);
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

    public String getLinedText() {
        switch (lined) {
            case LINED_ZERO_TO_FIVE:
                return "0 ~ 5 ページ書き込み線";
            case LINED_FIVE_TO_TEN:
                return "5 ~ 10 ページ書き込み線";
            case LINED_MORE_THAN_TEN:
                return "10 ページ以上書き込み線";
            default:
                return "書き込み線なし";
        }
    }

    public String getFoldedText() {
        switch (folded) {
            case FOLDED_ZERO_TO_FIVE:
                return "0 ~ 5 ページ折れ";
            case FOLDED_FIVE_TO_TEN:
                return "5 ~ 10 ページ折れ";
            case FOLDED_MORE_THAN_TEN:
                return "10 ページ以上折れ";
            default:
                return "折れなし";
        }
    }

    public String getBrokenText() {
        switch (broken) {
            case BROKEN_ZERO_TO_FIVE:
                return "0 ~ 5 ページ破け";
            case BROKEN_FIVE_TO_TEN:
                return "5 ~ 10 ページ破け";
            case BROKEN_MORE_THAN_TEN:
                return "10 ページ以上破け";
            default:
                return "破けなし";
        }
    }

    public String getEvaluationText() {
        switch (evaluation) {
            case 0:
                return "良い";
            case 1:
                return "普通";
            case 2:
                return "汚れあり";
            default:
                return "普通";
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
        json.put(NOTE, note);
        return json;
    }

    @Override
    public void setValues(JSONObject json) throws JSONException {
        lined = json.getInt(LINED);
        folded = json.getInt(FOLDED);
        sunburned = json.getBoolean(SUNBURNED);
        scratched = json.getBoolean(SCRATCHED);
        hasBand = json.getBoolean(HAS_BAND);
        evaluation = json.getInt(EVALUATION);
        smell = new Smell(json.getJSONObject(SMELL));
        note = json.getString(NOTE);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.lined);
        dest.writeInt(this.folded);
        dest.writeInt(this.broken);
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
        this.broken = in.readInt();
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
