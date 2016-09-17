package com.books.hondana.connection;

import android.support.annotation.Nullable;

import com.books.hondana.model.abst.KiiModel;

import java.util.List;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/9/16.
 */
public interface KiiObjectListCallback<T extends KiiModel> {
    void success(int token, List<T> result);
    void failure(@Nullable Exception e);
}
