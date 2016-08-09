package com.books.hondana.Connection;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.ProgressDialog;

import com.books.hondana.Model.KiiBook;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/07/28.
 */
public abstract class BookSearchConnection implements ParseQueryParamSet {

    // 通信成功
    public final static int CONNECTION_SUCCESS = 1;
    // 通信エラー
    public final static int CONNECTION_ERROR = CONNECTION_SUCCESS + 1;
    // Dummy Loader ID
    public final static int LOADER_ID = 0;

    // Request用 URL
    protected  String targetUrl;
    // パラメータ保存用
    protected QueryParamSet queryParamSet;
    // 呼び出し元 Activity
    protected  Activity activity;

    protected int loaderId; //
    protected LoaderManager manager;
    protected HttpAsyncLoader loader;
    protected ProgressDialog progressDialog = null;

    // データ取得終了時のCallBack
    public interface SearchFinishListener {
        public void didFinish(final int err, final ArrayList<KiiBook> resultList);
    }

    // Constructor
    public  BookSearchConnection(Activity activity, int lorderID, QueryParamSet queryParamSet) {
        this.queryParamSet = queryParamSet;
        this.activity = activity;
        this.manager = activity.getLoaderManager();
        this.loaderId = lorderID;
    }

    // Setup 検索用パラメータ
    public void setQueryParam( QueryParamSet queryParamSet ){
        this.queryParamSet = queryParamSet;
    }

    // Create Loader (setup)
    protected HttpAsyncLoader createLoader(Activity activity, SearchAPI searchAPI) {
        HttpAsyncLoader httpAsyncLoader= new HttpAsyncLoader(activity, targetUrl, searchAPI );
        httpAsyncLoader.forceLoad();
        loader = httpAsyncLoader;
        return loader;
    }

    /**
     * パラメータからBookを検索します。
     * @param index 検索開始位置
     * @param searchFinishListener
     */
    public  abstract  void searchBooks( final int index, final SearchFinishListener searchFinishListener);

    public void cancel() {
        if (loader != null) {
            loader.cancelLoad();
        }
    }

    public void destroy() {
        manager.destroyLoader(loaderId);
    }

}

//    public void searchBooks(  final int index, final SearchFinishListener searchFinishListener) {
//
//        // 既にローダーがある場合は破棄される
//        manager.restartLoader(loaderId, null, new LoaderManager.LoaderCallbacks<JSONObject>() {
//
//            @Override
//            public HttpAsyncLoader onCreateLoader(int i, Bundle bundle) {
//                //URLとパラメータをセットしてLoader起動
//                return createLoader( activity );
//            }
//
//            @Override
//            public void onLoadFinished(android.content.Loader<JSONObject> loader, JSONObject data) {
//                if (data != null) {
//
//                    ParseJson parseJson = new ParseRakutenJson();
//                    ArrayList<KiiBook> resultList = parseJson.getBookInfo(data);
//
//                    searchFinishListener.didFinish(CONNECTION_SUCCESS, resultList);
//                }
//                // エラー時処理
//                else {
//                    searchFinishListener.didFinish(CONNECTION_ERROR, null);
//                }
//            }
//
//            @Override
//            public void onLoaderReset(android.content.Loader<JSONObject> loader) {
//                searchFinishListener.didFinish(CONNECTION_SUCCESS, new ArrayList<KiiBook>());
//            }
//        });
//    }
//    @Override
//    public void onCancel(DialogInterface arg0) {
//        // TODO 自動生成されたメソッド・スタブ
//        if (loader != null) {
//            loader.cancelLoad();
//        }
//    }
