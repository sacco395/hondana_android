package com.books.hondana.model.api;

import org.json.JSONObject;

import java.util.List;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/9/16.
 */
public interface JsonParser<T> {

    List<T> parse(JSONObject json);
}
