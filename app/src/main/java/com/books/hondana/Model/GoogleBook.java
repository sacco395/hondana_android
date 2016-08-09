package com.books.hondana.Model;

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

    public KiiBook toKiiBook(){
        KiiBook kb = new KiiBook();
        kb.set(KiiBook.GENRE_1,"");
        kb.set(KiiBook.GENRE_2,"");
        kb.set(KiiBook.GENRE_3,"");
        kb.set(KiiBook.GENRE_4,"");
        kb.set(KiiBook.GENRE_5,"");

        for(HashMap.Entry<String, String> e : map.entrySet()) {
            if( e.getKey().equals(GoogleBook.TITLE) ) {
                kb.set(KiiBook.TITLE, e.getValue());
            } else if (e.getKey().equals(GoogleBook.AUTHOR)){
                kb.set(KiiBook.AUTHOR, e.getValue());
            } else if (e.getKey().equals(GoogleBook.ISBN)){
                kb.set(KiiBook.ISBN, e.getValue());
            } else if (e.getKey().equals(GoogleBook.PUBLISHER)){
                kb.set(KiiBook.PUBLISHER, e.getValue());
            } else if (e.getKey().equals(GoogleBook.IMAGE_URL)) {
                kb.set(KiiBook.IMAGE_URL, e.getValue());
            }
        }
        return kb;
    }

}
