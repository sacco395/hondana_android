package com.books.hondana.connection;

import com.books.hondana.model.Book;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.query.KiiClause;
import com.kii.cloud.storage.query.KiiQuery;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class KiiUserBookConnection extends PagingConnection<Book> {

    private String userId;

    private int[] statuses;

    /**
     * @param userId 対象の KiiUser.getID
     * @param statuses status の値の配列。複数選択可能
     */
    public KiiUserBookConnection(String userId, int[] statuses) {
        this.userId = userId;
        this.statuses = statuses;
    }

    @Override
    protected KiiQuery createQuery() {
        KiiClause userClause = KiiClause.equals(Book.OWNER_ID, userId);
        KiiClause[] statusesClause = {};
        for (int i = 0; i < statuses.length; i++) {
            statusesClause[i] = KiiClause.equals(Book.STATE, statuses[i]);
        }
        KiiClause statusClause = KiiClause.or(statusesClause);
        return new KiiQuery(KiiClause.and(userClause, statusClause));
    }

    @Override
    protected KiiBucket getBucket() {
        return Kii.bucket(Book.BUCKET_NAME);
    }

    protected List<Book> convert(List<KiiObject> bookObjects) throws JSONException {
        List<Book> books = new ArrayList<>();
        for (KiiObject object : bookObjects) {
            books.add(Book.createFrom(object));
        }
        return books;
    }
}
