package com.books.hondana.Connection;

import android.support.annotation.Nullable;

import com.books.hondana.Model.KiiModel;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.query.KiiQueryResult;

import java.util.List;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/9/16.
 */
public interface KiiObjectCallback<T extends KiiModel> {
    void success(int token, List<T> result);
    void failure(@Nullable Exception e);
}
