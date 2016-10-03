package com.books.hondana.connection;

import android.support.annotation.Nullable;
import android.util.Log;

import com.books.hondana.model.Book;
import com.books.hondana.model.Genre;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiQueryCallBack;
import com.kii.cloud.storage.query.KiiQuery;
import com.kii.cloud.storage.query.KiiQueryResult;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 10/1/16.
 */

public class KiiGenreConnection extends PagingConnection<Book> {

    private static final String TAG = KiiGenreConnection.class.getSimpleName();

    private Genre genre;

    public KiiGenreConnection(Genre genre) {
        this.genre = genre;
    }

    @Override
    protected KiiQuery createQuery() {
        return new KiiQuery(genre.clause());
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
