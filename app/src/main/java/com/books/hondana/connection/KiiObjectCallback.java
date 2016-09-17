package com.books.hondana.connection;

import com.books.hondana.model.abst.KiiModel;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/12/16.
 */
public interface KiiObjectCallback<T extends KiiModel> {

    void success(int token, T model);
    void failure(Exception e);
}
