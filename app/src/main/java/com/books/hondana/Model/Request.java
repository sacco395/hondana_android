package com.books.hondana.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.books.hondana.model.abst.KiiModel;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiObjectBodyCallback;
import com.kii.cloud.storage.callback.KiiObjectPublishCallback;

import org.json.JSONException;

import java.io.File;

/**
 * リクエストに関する情報を保持
 * 取引が進むごとに、対応するフィールドに日付がセットされる
 * Request -> Send -> Receive -> Evaluate
 * の順に行われる
 */
public class Request extends KiiModel implements Parcelable {

    public static final String BUCKET_NAME = "requests";

    public static final String PDF_CONTENT_TYPE = "application/pdf";

    public static final String CLIENT_ID = "client_id";
    public static final String SERVER_ID = "server_id";
    public static final String PDF_LABEL_ID = "pdf_label_id";
    public static final String BOOK_ID = "book_id";
    public static final String REQUESTED_DATE = "requested_date";
    public static final String SENT_DATE = "sent_date";
    public static final String RECEIVED_DATE = "received_date";
    public static final String EVALUATED_DATE = "evaluated_date";

    private String id;

    /**
     * リクエストをしたユーザID
     */
    private String clientId;

    /**
     * 本の持ち主のユーザID
     */
    private String serverId;

    /**
     * 配送に使う宛名ラベルの PDF ファイルが保存されている KiiObject の ID
     */
    private String pdfLabelId;

    private String bookId;

    private String requestedDate;

    private String sentDate;

    private String receivedDate;

    private String evaluatedDate;

    /**
     * Request を、新規に作成する。すでにサーバに保存されているオブジェクトの取得は、
     * {@link com.books.hondana.connection.KiiRequestConnection} を使用すること
     * @param clientId KiiUser#getID
     * @param book リクエストの対象の本
     * @return
     */
    public static Request createNew(String clientId, Book book)  {
        Request request = new Request();
        request.setClientId(clientId);
        request.setServerId(book.getOwnerId());
        request.setBookId(book.getId());
        return request;
    }

    /**
     * KiiCloud に保存されているオブジェクトから、Request を生成
     * @param kiiObject
     * @return
     * @throws JSONException KiiObject 内に、求めるフィールドがなかったら例外をスロー
     */
    public static Request createFrom(KiiObject kiiObject) throws JSONException {
        return new Request(kiiObject);
    }

    private Request() {
        clientId = "";
        serverId = "";
        pdfLabelId = "";
        bookId = "";
        requestedDate = "";
        sentDate = "";
        receivedDate = "";
        evaluatedDate = "";
    }

    private Request(KiiObject kiiObject) throws JSONException {
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

    /**
     * フィールドの値を保存しかつ、PDF アップロード
     * Request が、KiiCloud 上で書き換わっていた場合、データの整合性を保つためにアップデートは中断され、
     * PdfUploadCallback#failure が呼ばれる
     * @param pdfFile 公開期限（秒）
     * @param callback See {@link KiiObjectBodyCallback}
     */
    public void saveWithPdf(final File pdfFile, final PdfUploadCallback callback) {
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

    /**
     * PDF を期限付きで公開して URL を取得
     * Request が、KiiObject から生成されていない場合、PdfPublishCallback#failure が呼ばれる
     * @param expiresIn 公開期限（秒）
     * @param callback See {@link KiiObjectPublishCallback}
     */
    public void publishPdfWithExpation(int expiresIn, PdfPublishCallback callback) {
        if (source == null || id == null) {
            callback.failure(new IllegalStateException("KiiObject が空です。"));
            return;
        }
        source.publishBodyExpiresIn(expiresIn, callback);
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
//        sentDate = object.getString(sentDate);
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

    public interface PdfUploadCallback extends KiiObjectBodyCallback {
        void failure(IllegalStateException e);
    }

    public interface PdfPublishCallback extends KiiObjectPublishCallback {
        void failure(IllegalStateException e);
    }
}
