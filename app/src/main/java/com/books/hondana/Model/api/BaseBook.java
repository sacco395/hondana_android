package com.books.hondana.model.api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Administrator on 2016/07/28.
 */
public class BaseBook {

    protected String jsonString; // for Debug
    protected HashMap<String,String> map;

    public BaseBook(){
        map = new HashMap<String,String>();
    }

    public BaseBook(JSONObject jsonItem){
        this();
        jsonString = jsonItem.toString();
        Iterator<?> keys = jsonItem.keys();
        while(keys.hasNext()){
            String key = (String) keys.next();
            try {
                String val = jsonItem.getString(key);
                map.put(key, val);
            } catch (JSONException e) {
                // TODO 自動生成された catch ブロック
                e.printStackTrace();
            }
        }
    }
    public String get(String key){
        return map.get(key);
    }
    public String getJsonString() {
        return jsonString;
    }
    public HashMap<String, String> getMap() {
        return map;
    }
}
