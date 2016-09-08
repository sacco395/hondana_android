package com.books.hondana.Connection;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.util.Log;

import com.books.hondana.Model.KiiBook;
import com.books.hondana.Model.KiiCloudBucket;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiQueryCallBack;
import com.kii.cloud.storage.query.KiiClause;
import com.kii.cloud.storage.query.KiiQuery;
import com.kii.cloud.storage.query.KiiQueryResult;

/**
 * Created by Administrator on 2016/08/02.
 */
public class KiiCloudConnection {

    protected  QueryParamSet queryParamSet;
    protected  Activity activity;
    protected  KiiCloudBucket kiiCloudBucket;

    // データ取得終了時のCallBack
    public interface SearchFinishListener {
        public void didFinish(int token, KiiQueryResult<KiiObject> result, Exception e);
    }

    // Constructor
    public  KiiCloudConnection(Activity activity, QueryParamSet queryParamSet,
                               KiiCloudBucket kiiCloudBucket) {
        this.queryParamSet = queryParamSet;
        this.activity = activity;
        this.kiiCloudBucket= kiiCloudBucket;
    }

    public KiiCloudConnection(KiiCloudBucket kiiCloudBucket) {
        this.kiiCloudBucket = kiiCloudBucket;
    }

    public void loadHondanaBooksByGenres(final int index, final SearchFinishListener searchFinishListener) {

        int gCount = queryParamSet.genreTbl.length;
        String defaultIsbn;

        if ( gCount == 0){
            defaultIsbn = "";      // ジャンル指定なしなら、DefaultはAll
        } else {
            defaultIsbn = "###";  // ジャンル指定ありなら、DefaultはNothing
        }
        // Dummy
       KiiClause kiiClause=KiiClause.startsWith(KiiBook.ISBN, defaultIsbn);
        for ( int i=0; i<gCount; i++ ) {
            KiiClause kc1 = KiiClause.startsWith(KiiBook.GENRE_1, queryParamSet.genreTbl[i]);
            KiiClause kc2 = KiiClause.startsWith(KiiBook.GENRE_2, queryParamSet.genreTbl[i]);
            KiiClause kc3 = KiiClause.startsWith(KiiBook.GENRE_3, queryParamSet.genreTbl[i]);
            KiiClause kc4 = KiiClause.startsWith(KiiBook.GENRE_4, queryParamSet.genreTbl[i]);
            KiiClause kc5 = KiiClause.startsWith(KiiBook.GENRE_5, queryParamSet.genreTbl[i]);
            kiiClause = KiiClause.or(kiiClause,kc1,kc2,kc3,kc4,kc5);
        }

        KiiQuery query = new KiiQuery( kiiClause );
        // Default 新しい順
        query.sortByDesc("_created");

        KiiBucket kiiBucket = Kii.bucket(this.kiiCloudBucket.getName());
        kiiBucket.query(
            new KiiQueryCallBack<KiiObject>() {

                // catch the callback's "done" request
                public void onQueryCompleted(int token, KiiQueryResult<KiiObject> result, Exception e) {
                     searchFinishListener.didFinish(token, result, e);
                }
            },
         query);
    }

    public void loadMember(String userId, final SearchFinishListener searchFinishListener) {
        Log.d("KiiCloudConnection", "loadMember(userId: " + userId + ")");
        final KiiBucket kiiBucket = Kii.bucket(kiiCloudBucket.getName());
        kiiBucket.query(new KiiQueryCallBack<KiiObject>() {
            @Override
            public void onQueryCompleted(int token, @Nullable KiiQueryResult<KiiObject> result, @Nullable Exception exception) {
                super.onQueryCompleted(token, result, exception);
                searchFinishListener.didFinish(token, result, exception);
            }
        }, new KiiQuery(KiiClause.equals("_id", userId)));
    }
}
