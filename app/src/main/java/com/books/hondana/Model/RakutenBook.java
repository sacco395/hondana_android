package com.books.hondana.Model;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

public class RakutenBook extends BaseBook implements Serializable{

	//public static final String BOOK_ID = "book_id";
	public static final String TITLE = "title";
	public static final String PUBLISHER = "publisherName";
	public static final String AUTHOR = "author";
	public static final String ISBN = "isbn";
	//public static final String LANGUAGE = "language";
	public static final String ISSUE_DATE = "salesDate";  // Date
	//public static final String IMAGE = "image";  // Image
	public static final String IMAGE_URL = "largeImageUrl";
	//public static final String HEIGHT = "height"; // double
	//public static final String WIDTH = "width";   // double
	//public static final String DEPTH = "depth";   // double
	//public static final String WEIGHT = "weight"; // double
	public static final String GENRE= "booksGenreId";

	public RakutenBook(JSONObject jsonItem){
		super(jsonItem);
	}

	public KiiBook toKiiBook(){
		KiiBook kb = new KiiBook();
		for(HashMap.Entry<String, String> e : map.entrySet()) {
			if( e.getKey().equals(RakutenBook.TITLE) ) {
				kb.set(KiiBook.TITLE, e.getValue());
			} else if (e.getKey().equals(RakutenBook.AUTHOR)){
				kb.set(KiiBook.AUTHOR, e.getValue());
			} else if (e.getKey().equals(RakutenBook.ISBN)){
				kb.set(KiiBook.ISBN, e.getValue());
			} else if (e.getKey().equals(RakutenBook.PUBLISHER)){
				kb.set(KiiBook.PUBLISHER, e.getValue());
			} else if (e.getKey().equals(RakutenBook.IMAGE_URL)) {
				kb.set(KiiBook.IMAGE_URL, e.getValue());
			} else if (e.getKey().equals(RakutenBook.ISSUE_DATE)) {
				kb.set(KiiBook.ISSUE_DATE, e.getValue());
			} else if ( e.getKey().equals(RakutenBook.GENRE)){
				String genreSource = e.getValue();
				String[] genreArray = { "", "", "", "", ""};
				genreArray = genreSource.split("/",5);
				int len = genreArray.length;
				if ( len > 0 && genreArray[0] != null ){
					kb.set(KiiBook.GENRE_1,genreArray[0]);
				} else {
					kb.set(KiiBook.GENRE_1,"");
				}
				if ( len > 1 && genreArray[1] != null ){
					kb.set(KiiBook.GENRE_2,genreArray[1]);
				} else {
					kb.set(KiiBook.GENRE_2,"");
				}
				if ( len > 2 && genreArray[2] != null ){
					kb.set(KiiBook.GENRE_3,genreArray[2]);
				} else {
					kb.set(KiiBook.GENRE_3,"");
				}
				if ( len > 3 && genreArray[3] != null ){
					kb.set(KiiBook.GENRE_4,genreArray[3]);
				} else {
					kb.set(KiiBook.GENRE_4,"");
				}
				if ( len > 4 && genreArray[4] != null ){
					kb.set(KiiBook.GENRE_5,genreArray[4]);
				} else {
					kb.set(KiiBook.GENRE_5,"");
				}
			}
		}
		return kb;
	}

}
