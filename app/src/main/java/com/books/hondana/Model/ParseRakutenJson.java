package com.books.hondana.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/07/28.
 */
public class ParseRakutenJson implements ParseJson {

    @Override
    public ArrayList<KiiBook> getBookInfo( JSONObject rootObject ) {
        ArrayList<KiiBook> bookList = new ArrayList<KiiBook>();
        try {
            //JSONObject rootObject = new JSONObject(arg1);
            JSONArray itemsArray = rootObject.getJSONArray("Items");
            int count = rootObject.getInt("count");

            if (count <= 0){
                return null;
            }
            for(int i=0;i<itemsArray.length();i++){
                JSONObject obj = itemsArray.getJSONObject(i).getJSONObject("Item");
                RakutenBook book = new RakutenBook(obj);
                KiiBook kiiBook = book.toKiiBook();
                bookList.add(kiiBook);
            }// end for
        } catch (JSONException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        return bookList;
    }
}
