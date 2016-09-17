package com.books.hondana.model.exception;

import org.json.JSONException;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/10/16.
 */
public class KiiModelException extends Exception {

    public KiiModelException(JSONException e) {
        super(e);
    }

    public KiiModelException() {
        super("KiiObject を生成できません。ID が無効である可能性があります。");
    }
}
