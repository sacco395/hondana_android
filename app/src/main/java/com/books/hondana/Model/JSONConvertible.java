package com.books.hondana.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/10/16.
 */
public interface JSONConvertible {

    JSONObject toJSON() throws JSONException;
}
