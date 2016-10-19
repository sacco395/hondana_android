package com.books.hondana.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.books.hondana.R;
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
 * Request -> Send -> Receive
 * の順に行われる
 */
public class Request extends KiiModel implements Parcelable {
    public static final String BUCKET_NAME = "requests";
    public static final String PDF_CONTENT_TYPE = "application/pdf";
    public static final String CLIENT_ID = "client_id";
    public static final String SERVER_ID = "server_id";
    public static final String PDF_URL = "pdf_url";
    public static final String BOOK_ID = "book_id";
    public static final String BOOK_TITLE = "book_title";
    public static final String BOOK_IMAGE_URL = "book_image_url";
    public static final String REQUESTED_DATE = "requested_date";
    public static final String SENT_DATE = "sent_date";
    public static final String RECEIVED_DATE = "received_date";
    public static final String EVALUATION_BY_CLIENT = "evaluation_by_client";
    public static final String EVALUATE_MESSAGE = "evaluate_message";
    public static final String SEVERAL_BOOKS = "several_books";
    public static final String ACCEPT_SEVERAL_BOOKS = "accept_several_books";
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
    private String bookId;
    private String bookTitle;
    private String bookImageUrl;
    private String requestedDate;
    private String sentDate;
    private String receivedDate;
    private String pdf_url;
    private int evaluationByClient;
    private String evaluateMessage;
    private boolean severalBooks;
    private boolean acceptSeveralBooks;

    /**
     * Request を、新規に作成する。すでにサーバに保存されているオブジェクトの取得は、
     * {@link com.books.hondana.connection.KiiRequestConnection} を使用すること
     *
     * @param clientId KiiUser#getID
     * @param book     リクエストの対象の本
     * @return
     */
    public static Request createNew(String clientId, Book book) {
        Request request = new Request();
        request.setClientId(clientId);
        request.setServerId(book.getOwnerId());
        request.setBookId(book.getId());
        BookInfo info = book.getInfo();
        request.setBookTitle(info.getTitle());
        request.setBookImageUrl(info.getImageUrl());
        return request;
    }

    /**
     * KiiCloud に保存されているオブジェクトから、Request を生成
     *
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
        bookId = "";
        bookTitle = "";
        bookImageUrl="";
        requestedDate = "";
        sentDate = "";
        receivedDate = "";
        pdf_url = "";
        evaluateMessage = "";
        severalBooks = false;
        acceptSeveralBooks = false;
    }

    public static final int EVALUATION_EXCELLENT = 0;
    public static final int EVALUATION_GOOD = 1;
    public static final int EVALUATION_BAD = 2;

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

    public String getPdfUrl() {
        return pdf_url;
    }

    public void setPdfUrl(String pdf_url) {
        this.pdf_url = pdf_url;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookImageUrl() {
        return bookImageUrl;
    }

    public void setBookImageUrl(String bookImageUrl) {
        this.bookImageUrl = bookImageUrl;
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
    //public String getEvaluatedDate() {return evaluatedDate;}

    public void setEvaluationByClient(int evaluationByClient) {
        this.evaluationByClient = evaluationByClient;
    }

    public int getEvaluationByClient() {
        return evaluationByClient;
    }

    public String getEvaluateMessage() {
        return evaluateMessage;
    }

    public void setEvaluateMessage(String evaluateMessage) {
        this.evaluateMessage = evaluateMessage;
    }

    public boolean getSeveralBooks() {
        return severalBooks;
    }

    public void setSeveralBooks(boolean severalBooks) {
        this.severalBooks = severalBooks;
    }

    public boolean getAcceptSeveralBooks() {
        return acceptSeveralBooks;
    }

    public void setAcceptSeveralBooks(boolean acceptSeveralBooks) {
        this.acceptSeveralBooks = acceptSeveralBooks;
    }


    public String getEvaluationByClientText() {
        switch (evaluationByClient) {
            case 0:
                return "良い";
            case 1:
                return "普通";
            case 2:
                return "悪い";
            default:
                return "普通";
        }
    }

    public int getIconDrawableResId() {
        switch (evaluationByClient) {
            case 0:
                return R.drawable.icon_excellent;
            case 1:
                return R.drawable.icon_good;
            case 2:
                return R.drawable.icon_bad;
            default:
                return 1;
        }
    }

    public String getSeveralBooksText() {
        if (getSeveralBooks()) {
            return "*相手のユーザーは同梱を希望しています";
        } else {
            return "";
        }
    }

    public String getAcceptSeveralBooksText() {
        if (getAcceptSeveralBooks()) {
            return "同梱して送付中です";
        } else {
            return "同梱できなかったので個別に送付中です";
        }
    }

    /**
     * フィールドの値を保存しかつ、PDF アップロード
     * Request が、KiiCloud 上で書き換わっていた場合、データの整合性を保つためにアップデートは中断され、
     * PdfUploadCallback#failure が呼ばれる
     *
     * @param pdfFile  公開期限（秒）
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
     *
     * @param expiresIn 公開期限（秒）
     * @param callback  See {@link KiiObjectPublishCallback}
     */
    public void publishPdfWithExpiration(int expiresIn, PdfPublishCallback callback) {
        if (source == null || id == null) {
            callback.failure(new IllegalStateException("KiiObject が空です。"));
            return;
        }
        source.publishBodyExpiresIn(expiresIn, callback);
    }


    public void downloadPdf(final File destinationFile, final DownloadCallback callback) {

        source.downloadBody(destinationFile, new KiiObjectBodyCallback() {
            @Override
            public void onTransferStart(@NonNull KiiObject kiiObject) {
                callback.start();
            }

            @Override
            public void onTransferProgress(@NonNull KiiObject kiiObject, long completedInBytes, long totalSizeinBytes) {
                float progress = (float) completedInBytes / (float) totalSizeinBytes * 100.0f;
                callback.progress(progress);
            }

            @Override
            public void onTransferCompleted(@NonNull KiiObject kiiObject, @Nullable Exception e) {
                if (e == null) {
                    callback.success(destinationFile);
                    return;
                }
                callback.failure(e);
            }
        });
    }


    //public void setEvaluatedDate(String evaluatedDate) {this.evaluatedDate = evaluatedDate;}
    @Override
    public KiiBucket bucket() {
        return Kii.bucket(BUCKET_NAME);
    }

    @Override
    public void setValuesFrom(KiiObject object) throws JSONException {
        clientId = object.getString(CLIENT_ID);
        serverId = object.getString(SERVER_ID);
        pdf_url = object.getString(PDF_URL, "");
        bookId = object.getString(BOOK_ID);
        bookTitle = object.getString(BOOK_TITLE);
        bookImageUrl = object.getString(BOOK_IMAGE_URL);
        requestedDate = object.getString(REQUESTED_DATE);
        sentDate = object.getString(SENT_DATE);
        receivedDate = object.getString(RECEIVED_DATE);
        evaluationByClient = object.getInt(EVALUATION_BY_CLIENT);
        evaluateMessage = object.getString(EVALUATE_MESSAGE);
        severalBooks = object.getBoolean(SEVERAL_BOOKS);
        acceptSeveralBooks = object.getBoolean(ACCEPT_SEVERAL_BOOKS);

    }

    @Override
    public KiiObject createKiiObject() throws JSONException {
        if (source == null) {
            source = bucket().object();
        }
        source.set(CLIENT_ID, clientId);
        source.set(SERVER_ID, serverId);
        source.set(PDF_URL, pdf_url);
        source.set(BOOK_ID, bookId);
        source.set(BOOK_TITLE, bookTitle);
        source.set(BOOK_IMAGE_URL, bookImageUrl);
        source.set(REQUESTED_DATE, requestedDate);
        source.set(SENT_DATE, sentDate);
        source.set(RECEIVED_DATE, receivedDate);
        source.set(EVALUATION_BY_CLIENT, evaluationByClient);
        source.set(EVALUATE_MESSAGE, evaluateMessage);
        source.set(SEVERAL_BOOKS, severalBooks);
        source.set (ACCEPT_SEVERAL_BOOKS, acceptSeveralBooks);
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
        dest.writeString(this.clientId);
        dest.writeString(this.serverId);
        dest.writeString(this.pdf_url);
        dest.writeString(this.bookId);
        dest.writeString(this.bookTitle);
        dest.writeString(this.bookImageUrl);
        dest.writeString(this.requestedDate);
        dest.writeString(this.sentDate);
        dest.writeString(this.receivedDate);
        dest.writeInt(this.evaluationByClient);
        dest.writeString(this.evaluateMessage);
        dest.writeByte(this.severalBooks ? (byte) 1 : (byte) 0);
        dest.writeByte (this.acceptSeveralBooks ? (byte) 1 : (byte) 0);
    }

    protected Request(Parcel in) {
        this.source = in.readParcelable(KiiObject.class.getClassLoader());
        this.id = in.readString();
        this.createdAt = in.readLong();
        this.updatedAt = in.readLong();
        this.clientId = in.readString();
        this.serverId = in.readString();
        this.pdf_url = in.readString();
        this.bookId = in.readString();
        this.bookTitle = in.readString();
        this.bookImageUrl = in.readString();
        this.requestedDate = in.readString();
        this.sentDate = in.readString();
        this.receivedDate = in.readString();
        this.evaluationByClient = in.readInt();
        this.evaluateMessage = in.readString();
        this.severalBooks = in.readByte() != 0;
        this.acceptSeveralBooks = in.readByte() != 0;
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

    public interface DownloadCallback {
        void start();
        void progress(float percent);
        void success(File file);
        void failure(@Nullable Exception e);
    }
}
