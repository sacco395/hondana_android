package com.books.hondana.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.books.hondana.Model.abst.KiiModel;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiObjectBodyCallback;

import org.json.JSONException;

import java.io.File;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/15/16.
 */
public class PdfLabel extends KiiModel implements Parcelable {

    public static final String BUCKET_NAME = "pdf_labels";

    public static final String REQUEST_ID = "request_id";

    public static final String PDF_CONTENT_TYPE = "application/pdf";

    private String requestId;

    public static PdfLabel createNew(Request request) {
        PdfLabel label = new PdfLabel();
        label.requestId = request.getId();
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

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void savePdf(final File pdfFile, final PdfUploadCallback callback) {
        save(false, new KiiSaveCallback() {
            @Override
            public void success(int token, KiiObject object) {
                object.uploadBody(pdfFile, PDF_CONTENT_TYPE, callback);
            }

            @Override
            public void failure(@Nullable Exception e) {
                callback.failure(new IllegalStateException("Failed to save fields before uploading Object body!"));
            }
        });
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

    public interface PdfUploadCallback extends KiiObjectBodyCallback {
        void failure(IllegalStateException e);
    }
}
