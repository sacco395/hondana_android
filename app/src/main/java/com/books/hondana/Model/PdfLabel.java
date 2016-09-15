package com.books.hondana.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.books.hondana.Model.abst.KiiModel;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;

import org.json.JSONException;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/15/16.
 */
public class PdfLabel extends KiiModel implements Parcelable {

    public static final String BUCKET_NAME = "pdfs";

    public static final String REQUEST_ID = "request_id";

    private String requestId;

    public static PdfLabel createNew(String requestId) {
        PdfLabel label = new PdfLabel();
        label.requestId = requestId;
        return label;
    }

    public static PdfLabel createFrom(KiiObject kiiObject) throws JSONException {
        return new PdfLabel(kiiObject);
    }

    private PdfLabel(KiiObject kiiObject) throws JSONException {
        super(kiiObject);
    }

    private PdfLabel() {
        requestId = "";
    }

    @Override
    public KiiBucket bucket() {
        return Kii.bucket(BUCKET_NAME);
    }

    @Override
    public void setValuesFrom(KiiObject kiiObject) throws JSONException {
        requestId = kiiObject.getString(REQUEST_ID);
    }

    @Override
    public KiiObject createKiiObject() throws JSONException {
        if (source == null) {
            source = bucket().object();
        }
        source.set(REQUEST_ID, requestId);
        return source;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.source, flags);
        dest.writeString(this.id);
        dest.writeLong(this.createdAt);
        dest.writeLong(this.updatedAt);
        dest.writeString(this.requestId);
    }

    protected PdfLabel(Parcel in) {
        this.source = in.readParcelable(KiiObject.class.getClassLoader());
        this.id = in.readString();
        this.createdAt = in.readLong();
        this.updatedAt = in.readLong();
        this.requestId = in.readString();
    }

    public static final Creator<PdfLabel> CREATOR = new Creator<PdfLabel>() {
        @Override
        public PdfLabel createFromParcel(Parcel source) {
            return new PdfLabel(source);
        }

        @Override
        public PdfLabel[] newArray(int size) {
            return new PdfLabel[size];
        }
    };
}
