package com.books.hondana.Model;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/07/28.
 */
public interface ParseJson {
    public  ArrayList<KiiBook> getBookInfo(JSONObject json);
}
