package com.books.hondana.Model.api.google;

import com.books.hondana.Model.api.BaseBook;
import com.books.hondana.Model.book.Book;
import com.books.hondana.Model.book.BookInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/07/28.
 */
public class GoogleBook extends BaseBook {

    //public static final String BOOK_ID = "book_id";
    public static final String VOLINFO = "volumeInfo";
    public static final String TITLE = "title";
    public static final String PUBLISHER = "publisher";
    public static final String AUTHOR = "authors";
    public static final String ISBN = "isbn13";
    public static final String ISBN_ROOT = "industryIdentifiers";
    public static final String ISBN_ELEMENT = "identifier";


    //public static final String LANGUAGE = "language";
    public static final String ISSUE_DATE = "publishedDate";  // Date
    //public static final String IMAGE = "image";  // Image
    public static final String IMAGE_LINKS = "imageLinks";
    public static final String IMAGE_URL = "smallThumbnail";
    //public static final String HEIGHT = "height"; // double
    //public static final String WIDTH = "width";   // double
    //public static final String DEPTH = "depth";   // double
    //public static final String WEIGHT = "weight"; // doubble

    public GoogleBook(JSONObject jsonItem){
        super();
        try {
            map.put(GoogleBook.TITLE, jsonItem.getString(GoogleBook.TITLE));

            JSONArray tmpArray = jsonItem.getJSONArray(GoogleBook.AUTHOR);
            String sAuthor = tmpArray.get(0).toString();
            for ( int j=1; j<tmpArray.length(); j++ ){
                sAuthor += ", "+ tmpArray.get(j).toString();
            }

            map.put(GoogleBook.AUTHOR,sAuthor);
            map.put(GoogleBook.PUBLISHER, jsonItem.getString(GoogleBook.PUBLISHER));
            map.put(GoogleBook.ISSUE_DATE, jsonItem.getString(GoogleBook.ISSUE_DATE));
            map.put(GoogleBook.ISBN,jsonItem.getJSONArray(GoogleBook.ISBN_ROOT).getJSONObject(0).getString(GoogleBook.ISBN_ELEMENT));
            map.put(GoogleBook.IMAGE_URL,jsonItem.getJSONObject(GoogleBook.IMAGE_LINKS).getString(GoogleBook.IMAGE_URL));

        }   catch (JSONException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }

    public Book toBook(){
        BookInfo info = new BookInfo();
        for(HashMap.Entry<String, String> e : map.entrySet()) {
            if( e.getKey().equals(GoogleBook.TITLE) ) {
                info.setTitle(e.getValue());
            } else if (e.getKey().equals(GoogleBook.AUTHOR)){
                info.setAuthor(e.getValue());
            } else if (e.getKey().equals(GoogleBook.ISBN)){
                info.setIsbn(e.getValue());
            } else if (e.getKey().equals(GoogleBook.PUBLISHER)){
                info.setPublisher(e.getValue());
            } else if (e.getKey().equals(GoogleBook.IMAGE_URL)) {
                info.setImageUrl(e.getValue());
            }
        }
        Book book = new Book();
        book.setInfo(info);
        return book;
    }

}
