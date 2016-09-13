package com.books.hondana.Model.api.rakuten;

import com.books.hondana.Model.api.BaseBook;
import com.books.hondana.Model.Book;
import com.books.hondana.Model.BookInfo;
import com.books.hondana.Model.GenreList;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	public Book toBook(){
		Book book = new Book();
		BookInfo info = new BookInfo();
		for(HashMap.Entry<String, String> e : map.entrySet()) {
			if( e.getKey().equals(RakutenBook.TITLE) ) {
				info.setTitle(e.getValue());
			} else if (e.getKey().equals(RakutenBook.AUTHOR)){
				info.setAuthor(e.getValue());
			} else if (e.getKey().equals(RakutenBook.ISBN)){
				info.setIsbn(e.getValue());
			} else if (e.getKey().equals(RakutenBook.PUBLISHER)){
				info.setPublisher(e.getValue());
			} else if (e.getKey().equals(RakutenBook.IMAGE_URL)) {
				info.setImageUrl(e.getValue());
			} else if (e.getKey().equals(RakutenBook.ISSUE_DATE)) {
				info.setIssueDate(e.getValue());
			} else if ( e.getKey().equals(RakutenBook.GENRE)){
				String genreSource = e.getValue();
				String [] rakutenGenres = genreSource.split("/",5);
				List<String> genres = new ArrayList<>(5);
				int len = rakutenGenres.length;
				if ( len > 0 && rakutenGenres[0] != null ) {
                    genres.add(rakutenGenres[0]);
				} else {
                    genres.add("");
                }
				if ( len > 1 && rakutenGenres[1] != null ) {
                    genres.add(rakutenGenres[1]);
                } else {
                    genres.add("");
                }
				if ( len > 2 && rakutenGenres[2] != null ) {
                    genres.add(rakutenGenres[2]);
                } else {
                    genres.add("");
                }
				if ( len > 3 && rakutenGenres[3] != null ) {
                    genres.add(rakutenGenres[3]);
                } else {
                    genres.add("");
                }
				if ( len > 4 && rakutenGenres[4] != null ) {
                    genres.add(rakutenGenres[4]);
                } else {
                    genres.add("");
                }
				info.setGenres(new GenreList(genres));
			}
		}
        book.setInfo(info);
		return book;
	}

}
