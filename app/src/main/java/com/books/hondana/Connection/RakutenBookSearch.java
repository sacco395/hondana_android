package com.books.hondana.connection;

import android.app.Activity;
import android.app.LoaderManager;
import android.os.Bundle;

import com.books.hondana.model.api.JsonParser;
import com.books.hondana.model.Book;
import com.books.hondana.model.api.rakuten.RakutenBookParser;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/07/28.
 */
public class RakutenBookSearch extends BookSearchConnection {
    public static final int RAKUTEN_LOADER_ID = 1;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 楽天ブックスAPI
    public static String API_KEY = "a26268c98c2e75dc961181dc5d1247cb"; // 奥山's appID
    public static String API_URL = "https://app.rakuten.co.jp/services/api/BooksBook/Search/20130522?";
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public RakutenBookSearch(Activity activity,QueryParamSet queryParamSet) {
        super(activity,RAKUTEN_LOADER_ID, queryParamSet);
    }

    /**
     * 検索ワードからBookを検索します。
     * @param index 検索開始位置
     * @param searchFinishListener
     */
    public void searchBooks( final int index, final SearchFinishListener searchFinishListener) {

         // 既にローダーがある場合は破棄される
        manager.restartLoader(loaderId, null, new LoaderManager.LoaderCallbacks<JSONObject>() {

            @Override
            public HttpAsyncLoader onCreateLoader(int i, Bundle bundle) {
                //URLとパラメータをセットしてLoader起動
                String searchIsbn = queryParamSet.getSearchValue(QueryParamSet.ISBN);
                // 楽天ブックスAPI
                targetUrl = API_URL + "applicationId=" + API_KEY;
                targetUrl += "&isbn=" + searchIsbn + "&outOfStockFlag=1";

                return createLoader( activity, SearchAPI.RAKUTEN );
            }

            @Override
            public void onLoadFinished(android.content.Loader<JSONObject> loader, JSONObject data) {
                if ( data != null) {
                    JsonParser<Book> parser = new RakutenBookParser();
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
    ///////////////////////////////////////////////////////////////////
    //    @Override
//    public void onCancel(DialogInterface arg0) {
//        // TODO 自動生成されたメソッド・スタブ
//        if (loader != null) {
//            loader.cancelLoad();
//        }
//    }

}
