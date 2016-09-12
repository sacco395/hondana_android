package com.books.hondana;

import com.books.hondana.Model.KiiBook;

/**
 * Created by sacco on 2016/09/11.
 */
public class MyBookList {
    //保存するデータ全てを変数で定義します。
    private String image_url;
    private String title;
    private String author;

    //データを１つ作成する関数です。項目が増えたら増やしましょう。
    public MyBookList(String image_url, String title, String author) {
        this.image_url = image_url;
        this.title = title;
        this.author = author;
    }

    //それぞれの項目を返す関数です。項目が増えたら増やしましょう。
    public String getImageUrl() {return image_url;}
    public String getTitle() {return title;}
    public String getAuthor() {return author;}
}