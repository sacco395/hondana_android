package com.books.hondana.connection;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/07/28.
 */
public class QueryParamSet implements Serializable{


    // 検索用のパラメータ
    public static final String ISBN = "isbn";
    public static final int GENRE_SET_MAX = 16;
    public HashMap<String,String> queryParamas;
    public String[] genreTbl;
    //private String searchKey;
    //private String searchValue;

    public QueryParamSet()
    {
        queryParamas = new HashMap<String,String>();

    }

    public void addQueryParam(String searchKey, String searchValue){
        queryParamas.put(searchKey,searchValue);
    }

    public void setQueryByGenre(String[] genreTbl){
        this.genreTbl = new String[genreTbl.length];
        for( int i=0; i<genreTbl.length;i++){
                this.genreTbl[i] = genreTbl[i];
        }
    }

    public String getSearchValue( String searchKey ) {
        return queryParamas.get(searchKey);
    }

    @Override
    public String toString() {
        return queryParamas.toString();
    }
}
