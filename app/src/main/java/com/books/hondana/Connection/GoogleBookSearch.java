package com.books.hondana.Connection;

import android.app.Activity;
import android.app.LoaderManager;
import android.os.Bundle;

import com.books.hondana.Model.api.JsonParser;
import com.books.hondana.Model.book.Book;
import com.books.hondana.Model.api.google.GoogleBookJsonParser;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/07/28.
 */
public class GoogleBookSearch extends BookSearchConnection {

    public static final int GOOGLE_LOADER_ID = 2;

    // Google Books API
    public static final String API_URL = "https://www.googleapis.com/books/v1/volumes?country=JP&q=";
    public static String API_KEY = "AIzaSyDO1xCN0tD_6CizVVqlnpzpdgyLwQsHmwQ"; // 奥山's appID

    public GoogleBookSearch(Activity activity, QueryParamSet queryParamSet) {
        super(activity, GOOGLE_LOADER_ID, queryParamSet );
    }

    /**
            * 検索ワードからBookを検索します。
            * @param index 検索開始位置
    * @param searchFinishListener
    */
    public void searchBooks(  final int index, final SearchFinishListener searchFinishListener) {


        // 既にローダーがある場合は破棄される
        manager.restartLoader(loaderId, null, new LoaderManager.LoaderCallbacks<JSONObject>() {

            @Override
            public HttpAsyncLoader onCreateLoader(int i, Bundle bundle) {
                //URLとパラメータをセットしてLoader起動
                String searchIsbn = queryParamSet.getSearchValue(QueryParamSet.ISBN);
                targetUrl =  API_URL + "isbn:" + searchIsbn;

                return createLoader( activity, SearchAPI.GOOGLE );
            }

            @Override
            public void onLoadFinished(android.content.Loader<JSONObject> loader, JSONObject data) {
                if (data != null) {
                    JsonParser<Book> parser = new GoogleBookJsonParser();
                    List<Book> resultList = parser.parse(data);
                    searchFinishListener.didFinish(CONNECTION_SUCCESS, resultList);
                }
                // エラー時処理
                else {
                    searchFinishListener.didFinish(CONNECTION_ERROR, null);
                }
            }

            @Override
            public void onLoaderReset(android.content.Loader<JSONObject> loader) {
                //searchFinishListener.didFinish(CONNECTION_SUCCESS, new ArrayList<KiiBook>());
            }
        });
    }
    //    @Override
//    public void onCancel(DialogInterface arg0) {
//        // TODO 自動生成されたメソッド・スタブ
//        if (loader != null) {
//            loader.cancelLoad();
//        }
//    }
}
