package com.books.hondana.Model;

import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;

import org.json.JSONException;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/10/16.
 */
public abstract class KiiModel {

    public static final String ID = "_id";

    public KiiObject source;

    public String id;

    public long createdAt;

    public long updatedAt;

    public KiiModel() {
    }

    public KiiModel(KiiObject kiiObject) {
        source = kiiObject;
        id = kiiObject.getString(ID);
        createdAt = kiiObject.getCreatedTime();
        updatedAt = kiiObject.getModifedTime();
    }

    public abstract KiiBucket bucket();

    public abstract KiiObject createNewKiiObject() throws KiiModelException;

    public abstract KiiObject createNewKiiObject(String id) throws KiiModelException;
}
