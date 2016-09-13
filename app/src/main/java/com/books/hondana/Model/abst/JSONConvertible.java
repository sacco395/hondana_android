package com.books.hondana.Model.abst;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/10/16.
 */
public abstract class JSONConvertible {

    public JSONConvertible() {
    }

    public JSONConvertible(JSONObject json) throws JSONException {
        setValues(json);
    }

    public abstract void setValues(JSONObject json) throws JSONException;

    public abstract JSONObject toJSON() throws JSONException;
}
