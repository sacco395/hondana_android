//あなたへのお知らせ
package com.books.hondana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.books.hondana.InfoListViewAdapter;
import com.books.hondana.R;

public class InfoActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    private BaseAdapter adapter;


    private static final String[] info = {
            // Scenes of Isle of Wight
            "有効期限が近づいているブクがあります",
            "事務局から個別メッセージ1ブクをプレゼントしました",
            "ホンダナで交換申請されるための秘訣教えます!",
    };

    private static final String[] date = {
            // Scenes of Isle of Wight
            "3日前",
            "4日前",
            "5日前",
    };

    // ちょっと冗長的ですが分かり易くするために
    private static final int[] photos = {
            R.drawable.usericon,
            R.drawable.usericon,
            R.drawable.usericon,
    };

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_info);

        // ツールバーをアクションバーとしてセット
        Toolbar toolbar = (Toolbar) findViewById (R.id.toolbar);
        toolbar.setTitle ("あなたへのお知らせ");
        setSupportActionBar (toolbar);


        ActionBar actionBar = getSupportActionBar ();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled (true);
        }

        // ListViewのインスタンスを生成
        ListView listView = (ListView) findViewById (R.id.info_list_view);

        // BaseAdapter を継承したadapterのインスタンスを生成

        adapter = new InfoListViewAdapter (this.getApplicationContext (), R.layout.part_info_list, info ,date, photos);

        // ListViewにadapterをセット
        listView.setAdapter (adapter);

        // 後で使います
        listView.setOnItemClickListener (this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Intent intent = new Intent(this.getApplicationContext(), SelectedBooksActivity.class);
        // clickされたpositionのtextとphotoのID
        String selectedText = info[position];
        int selectedPhoto = photos[position];
        // インテントにセット
        intent.putExtra("Text", selectedText);
        intent.putExtra("Photo", selectedPhoto);
        // Activity をスイッチする
        startActivity(intent);
    }


//        Intent intent = new Intent(this.getApplicationContext(), SelectedBooksActivity.class);
//        // clickされたpositionのtextとphotoのID
//        String selectedText = todo[position];
//        int selectedPhoto = photos[position];
//        // インテントにセット
//        intent.putExtra("Text", selectedText);
//        intent.putExtra("Photo", selectedPhoto);
//        // Activity をスイッチする
//        startActivity(intent);
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId ()) {
            case android.R.id.home:
                onBackPressed ();
                return true;
            default:
                return super.onOptionsItemSelected (item);
        }

    }
}