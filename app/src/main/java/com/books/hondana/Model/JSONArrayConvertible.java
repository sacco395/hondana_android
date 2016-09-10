package com.books.hondana.Model;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/10/16.
 */
public interface JSONArrayConvertible {

    JSONArray toJSON() throws JSONException;
}
