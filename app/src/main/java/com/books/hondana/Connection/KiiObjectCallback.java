package com.books.hondana.Connection;

import com.books.hondana.Model.abst.KiiModel;
import com.kii.cloud.storage.KiiObject;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/12/16.
 */
public interface KiiObjectCallback<T extends KiiModel> {

    void success(int token, T model);
    void failure(Exception e);
}
