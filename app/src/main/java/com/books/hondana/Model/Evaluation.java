package com.books.hondana.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.books.hondana.model.abst.KiiModel;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;

import org.json.JSONException;


public class Evaluation extends KiiModel implements Parcelable {

    public static final String BUCKET_NAME = "evaluations";

    public static final String CLIENT_ID = "client_id";
    public static final String SERVER_ID = "server_id";
    public static final String EVALUATE_BY_CLIENT = "evaluation_byClient";
    public static final String EVALUATE_BY_SERVER = "evaluation_byServer";
    public static final String COMMENT_BY_CLIENT = "comment_byClient";
    public static final String COMMENT_BY_SERVER = "comment_byServer";

    private String id;

    /**
     * リクエストをしたユーザID
     */
    private String clientId;

    /**
     * 本の持ち主のユーザID
     */
    private String serverId;

    private int evaluation_by_client;

    private int evaluation_by_server;

    private String comment_by_client;

    private String comment_by_server;

    public static final int EVALUATION_EXCELLENT = 0;
    public static final int EVALUATION_GOOD = 1;
    public static final int EVALUATION_BAD = 2;



    /**
     * Request を、新規に作成する。すでにサーバに保存されているオブジェクトの取得は、
     * {@link com.books.hondana.connection.KiiRequestConnection} を使用すること
     * @param clientId KiiUser#getID
     * @param book リクエストの対象の本
     * @return
     */
    public static Evaluation createNew(String clientId, Book book)  {
        Evaluation evaluation = new Evaluation();
        evaluation.setClientId(clientId);
        evaluation.setServerId(book.getOwnerId());
        return evaluation;
    }

    /**
     * KiiCloud に保存されているオブジェクトから、Request を生成
     * @param kiiObject
     * @return
     * @throws JSONException KiiObject 内に、求めるフィールドがなかったら例外をスロー
     */
    public static Evaluation createFrom(KiiObject kiiObject) throws JSONException {
        return new Evaluation(kiiObject);
    }

    public Evaluation() {
        clientId = "";
        serverId = "";
        comment_by_client = "";
        comment_by_server = "";
    }

    private Evaluation(KiiObject kiiObject) throws JSONException {
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

    public void setEvaluationByClient(int evaluation_by_client) { this.evaluation_by_client = evaluation_by_client; }

    public int getEvaluationByClient() { return evaluation_by_client; }

    public void setEvaluationByServer(int evaluation_by_server) { this.evaluation_by_server = evaluation_by_server; }

    public int getEvaluationByServer() { return evaluation_by_server; }

    public void setCommentByClient(String comment_by_client) { this.comment_by_client = comment_by_client; }

    public  String getCommentByClient() { return comment_by_client; }

    public void setCommentByServer(String comment_by_server) { this.comment_by_server = comment_by_server; }

    public String getCommentByServer() { return comment_by_server; }

    @Override
    public KiiBucket bucket() {
        return Kii.bucket(BUCKET_NAME);
    }


    public String getEvaluationByClientText() {
        switch (evaluation_by_server) {
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

    public String getEvaluationByServerText() {
        switch (evaluation_by_client) {
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

    @Override
    public void setValuesFrom(KiiObject object) throws JSONException {
        clientId = object.getString(CLIENT_ID);
        serverId = object.getString(SERVER_ID);
        evaluation_by_client = object.getInt(EVALUATE_BY_CLIENT);
        evaluation_by_server = object.getInt(EVALUATE_BY_SERVER);
        comment_by_client = object.getString(COMMENT_BY_CLIENT);
        comment_by_server = object.getString(COMMENT_BY_SERVER);
    }

    @Override
    public KiiObject createKiiObject() throws JSONException {
        if (source == null) {
            source = bucket().object();
        }
        source.set(CLIENT_ID, clientId);
        source.set(SERVER_ID, serverId);
        source.set(EVALUATE_BY_CLIENT, evaluation_by_client);
        source.set(EVALUATE_BY_SERVER, evaluation_by_server);
        source.set(COMMENT_BY_CLIENT, comment_by_client);
        source.set(COMMENT_BY_SERVER, comment_by_server);
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
        dest.writeInt(this.evaluation_by_client);
        dest.writeInt(this.evaluation_by_server);
        dest.writeString(this.comment_by_client);
        dest.writeString(this.comment_by_server);
    }

    protected Evaluation(Parcel in) {
        this.id = in.readString();
        this.clientId = in.readString();
        this.serverId = in.readString();
        this.evaluation_by_client = in.readInt();
        this.evaluation_by_server = in.readInt();
        this.comment_by_client = in.readString();
        this.comment_by_server = in.readString();
    }

    public static final Creator<Evaluation> CREATOR = new Creator<Evaluation>() {
        @Override
        public Evaluation createFromParcel(Parcel source) {
            return new Evaluation(source);
        }

        @Override
        public Evaluation[] newArray(int size) {
            return new Evaluation[size];
        }
    };
}
