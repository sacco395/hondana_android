package com.books.hondana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.books.hondana.ListViewAdapter;
import com.books.hondana.R;

public class TodoActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    private BaseAdapter adapter;


    // Isle of Wight in U.K.
    private static final String[] scenes = {
            // Scenes of Isle of Wight
            "デザイン思考は世界を変える",
            "十月の旅人",
            "無印良品は仕組みが９割",
    };

    private static final String[] authors = {
            // Scenes of Isle of Wight
            "ティム・ブラウン",
            "レイ・ブラッドベリ",
            "松井忠三",
    };

    // ちょっと冗長的ですが分かり易くするために
    private static final int[] photos = {
            R.drawable.changedesign,
            R.drawable.october,
            R.drawable.muji,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);

        // ツールバーをアクションバーとしてセット
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar02);
        setSupportActionBar(toolbar);

        // ListViewのインスタンスを生成
        ListView listView = (ListView) findViewById(R.id.list_view);

        // BaseAdapter を継承したadapterのインスタンスを生成

        adapter = new ListViewAdapter(this.getApplicationContext(), R.layout.part_book_list, scenes, authors, photos);

        // ListViewにadapterをセット
        listView.setAdapter(adapter);

        // 後で使います
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Intent intent = new Intent(this.getApplicationContext(), SelectedBooksActivity.class);
        // clickされたpositionのtextとphotoのID
        String selectedText = scenes[position];
        int selectedPhoto = photos[position];
        // インテントにセット
        intent.putExtra("Text", selectedText);
        intent.putExtra("Photo", selectedPhoto);
        // Activity をスイッチする
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}