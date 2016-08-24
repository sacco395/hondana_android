package com.books.hondana.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.books.hondana.BookInfoListViewAdapter;
import com.books.hondana.R;


public class BookInfoActivity extends AppCompatActivity {

    private BaseAdapter adapter;


    private static final String[] username = {
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
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);


        // ListViewのインスタンスを生成
        ListView listViewBookOwner = (ListView) findViewById(R.id.listViewBookOwner);

        // BaseAdapter を継承したadapterのインスタンスを生成

        adapter = new BookInfoListViewAdapter(this.getApplicationContext(), R.layout.part_book_owner, username, evaluation);

        // ListViewにadapterをセット
        listViewBookOwner.setAdapter(adapter);

        // 後で使います
//        listViewBookOwner.setOnItemClickListener(this);



    }
}
