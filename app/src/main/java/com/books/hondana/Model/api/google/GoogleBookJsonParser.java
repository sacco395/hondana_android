package com.books.hondana.Model.api.google;

import com.books.hondana.Model.api.JsonParser;
import com.books.hondana.Model.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/07/28.
 */
public class GoogleBookJsonParser implements JsonParser<Book> {

    @Override
    public ArrayList<Book> parse(JSONObject rootObject) {

        ArrayList<Book> bookList = new ArrayList<>();

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
                Book book = googleBook.toBook();
                bookList.add(book);
            }// end for

        } catch (JSONException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        return bookList;

    }
}
