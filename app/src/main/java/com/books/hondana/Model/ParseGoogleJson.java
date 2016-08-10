package com.books.hondana.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/07/28.
 */
public class ParseGoogleJson implements ParseJson {
    @Override
    public ArrayList<KiiBook> getBookInfo(JSONObject rootObject) {

        ArrayList<KiiBook> bookList = new ArrayList<KiiBook>();

        try {
            JSONArray itemsArray = rootObject.getJSONArray("items");
            int count = rootObject.getInt("totalItems");

            if (count <= 0){
                return null;
            }

            for(int i=0;i<itemsArray.length();i++){

                JSONObject obj = itemsArray.getJSONObject(i);
                JSONObject volInfo = obj.getJSONObject(GoogleBook.VOLINFO);
                GoogleBook googleBook = new GoogleBook(volInfo);
                KiiBook book = googleBook.toKiiBook();
                bookList.add(book);
            }// end for

        } catch (JSONException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        return bookList;

    }
}
