package com.books.hondana.Model.api.rakuten;

import com.books.hondana.Model.api.JsonParser;
import com.books.hondana.Model.book.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/07/28.
 */
public class RakutenBookParser implements JsonParser<Book> {

    @Override
    public List<Book> parse(JSONObject rootObject ) {
        List<Book> bookList = new ArrayList<>();
        try {
            //JSONObject rootObject = new JSONObject(arg1);
            JSONArray itemsArray = rootObject.getJSONArray("Items");
            int count = rootObject.getInt("count");

            if (count <= 0){
                return null;
            }
            for(int i=0;i<itemsArray.length();i++){
                JSONObject obj = itemsArray.getJSONObject(i).getJSONObject("Item");
                RakutenBook rakutenBook = new RakutenBook(obj);
                Book book = rakutenBook.toBook();
                bookList.add(book);
            }// end for
        } catch (JSONException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        return bookList;
    }
}
