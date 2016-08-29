package com.books.hondana.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.books.hondana.Model.KiiBook;
import com.books.hondana.R;
import com.nostra13.universalimageloader.core.ImageLoader;


public class BookInfoActivity extends AppCompatActivity {

    private static final String TAG = BookInfoActivity.class.getSimpleName();

    private BaseAdapter adapter;

    private KiiBook kiiBook;


    /*private static final String[] username = {
            // Scenes of Isle of Wight
            "ユーザー名",
            "ユーザー名",
            "ユーザー名",
    };

    private static final String[] evaluation = {
            // Scenes of Isle of Wight
            "評価",
            "評価",
            "評価",
    };*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        kiiBook = getIntent().getParcelableExtra(KiiBook.class.getSimpleName());
        String imgUrl =kiiBook.get(KiiBook.IMAGE_URL);
        Log.d(TAG, "onCreate: " + kiiBook.get(KiiBook.TITLE));

        if( (imgUrl != null) && (imgUrl.length() > 0)){
            // 画像データのダウンロードと設定
            ImageLoader imageLoader = ImageLoader.getInstance();
            ImageView imgView = (ImageView)findViewById(R.id.imageViewBookInfo);
            imageLoader.displayImage(imgUrl,imgView);
        }

        /*// ListViewのインスタンスを生成
        ListView listViewBookOwner = (ListView) findViewById(R.id.listViewBookOwner);

        // BaseAdapter を継承したadapterのインスタンスを生成

        adapter = new BookInfoListViewAdapter(this.getApplicationContext(), R.layout.part_book_owner, username, evaluation);

        // ListViewにadapterをセット
        listViewBookOwner.setAdapter(adapter);

        // 後で使います
//        listViewBookOwner.setOnItemClickListener(this);*/



    }
}
