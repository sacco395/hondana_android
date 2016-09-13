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
 *         Created on 9/9/16.
 */
public class Request extends KiiModel implements Parcelable {

    public static final String BUCKET_NAME = "requests";

    public static final String CLIENT_ID = "client_id";
    public static final String SERVER_ID = "server_id";
    public static final String PDF_LABEL_ID = "pdf_label_id";
    public static final String BOOK_ID = "book_id";
    public static final String REQUESTED_DATE = "requested_date";
    public static final String SENT_DATE = "sent_date";
    public static final String RECEIVED_DATE = "received_date";
    public static final String EVALUATED_DATE = "evaluated_date";

    private String id;

    private String clientId;

    private String serverId;

    private String pdfLabelId;

    private String bookId;

    private String requestedDate;

    private String sentDate;

    private String receivedDate;

    private String evaluatedDate;

    public Request() {
        clientId = "";
        serverId = "";
        pdfLabelId = "";
        bookId = "";
        requestedDate = "";
        sentDate = "";
        receivedDate = "";
        evaluatedDate = "";
    }

    public Request(KiiObject kiiObject) throws JSONException {
        super(kiiObject);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getPdfLabelId() {
        return pdfLabelId;
    }

    public void setPdfLabelId(String pdfLabelId) {
        this.pdfLabelId = pdfLabelId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(String requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getEvaluatedDate() {
        return evaluatedDate;
    }

    public void setEvaluatedDate(String evaluatedDate) {
        this.evaluatedDate = evaluatedDate;
    }

    @Override
    public KiiBucket bucket() {
        return Kii.bucket(BUCKET_NAME);
    }

    @Override
    public void setValuesFrom(KiiObject object) throws JSONException {
        clientId = object.getString(CLIENT_ID);
        serverId = object.getString(SERVER_ID);
        pdfLabelId = object.getString(PDF_LABEL_ID);
        bookId = object.getString(BOOK_ID);
        requestedDate = object.getString(REQUESTED_DATE);
        sentDate = object.getString(sentDate);
        receivedDate = object.getString(RECEIVED_DATE);
        evaluatedDate = object.getString(EVALUATED_DATE);
    }

    @Override
    public KiiObject createKiiObject() throws JSONException {
        if (source == null) {
            source = bucket().object();
        }
        source.set(CLIENT_ID, clientId);
        source.set(SERVER_ID, serverId);
        source.set(PDF_LABEL_ID, pdfLabelId);
        source.set(BOOK_ID, bookId);
        source.set(REQUESTED_DATE, requestedDate);
        source.set(SENT_DATE, sentDate);
        source.set(RECEIVED_DATE, receivedDate);
        source.set(EVALUATED_DATE, evaluatedDate);
        return source;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.clientId);
        dest.writeString(this.serverId);
        dest.writeString(this.pdfLabelId);
        dest.writeString(this.bookId);
        dest.writeString(this.requestedDate);
        dest.writeString(this.sentDate);
        dest.writeString(this.receivedDate);
        dest.writeString(this.evaluatedDate);
    }

    protected Request(Parcel in) {
        this.id = in.readString();
        this.clientId = in.readString();
        this.serverId = in.readString();
        this.pdfLabelId = in.readString();
        this.bookId = in.readString();
        this.requestedDate = in.readString();
        this.sentDate = in.readString();
        this.receivedDate = in.readString();
        this.evaluatedDate = in.readString();
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel source) {
            return new Request(source);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };
}
