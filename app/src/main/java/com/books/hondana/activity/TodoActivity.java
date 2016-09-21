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

import com.books.hondana.R;
import com.books.hondana.TodoListViewAdapter;

public class TodoActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    private static final String[] date = {
            // Scenes of Isle of Wight
            "3日前",
            "4日前",
            "5日前",
    };

    private static final String[] todo = {
            // Scenes of Isle of Wight
            "らっこさんから「デザイン思考は世界を変える」に交換リクエストが届きました。\nこちらから発送をしましょう。",
            "ポニータさんから「舟を編む」が発送されました。\n届いたら内容を確認して、受取評価をしてください。",
            "ポニータさんに「舟を編む」の交換リクエスト中です。\n発送完了連絡をお待ちください。",
    };


    // ちょっと冗長的ですが分かり易くするために
    private static final int[] photos = {
            R.drawable.changedesign,
            R.drawable.fune,
            R.drawable.fune,
    };

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_todo);

        // ツールバーをアクションバーとしてセット
        Toolbar toolbar = (Toolbar) findViewById (R.id.toolbar);
        toolbar.setTitle ("やることリスト");
        setSupportActionBar (toolbar);


        ActionBar actionBar = getSupportActionBar ();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled (true);
        }

        // ListViewのインスタンスを生成
        ListView listView = (ListView) findViewById (R.id.todo_list_view);

        // BaseAdapter を継承したadapterのインスタンスを生成

        BaseAdapter adapter = new TodoListViewAdapter(this.getApplicationContext(), R.layout.part_todo_list, date, todo, photos);

        // ListViewにadapterをセット
        assert listView != null;
        listView.setAdapter (adapter);

        // 後で使います
        listView.setOnItemClickListener (this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Intent intent = new Intent ();

        switch (position) {
            case 0:
                intent = new Intent (TodoActivity.this, SendBookActivity.class);
                startActivity (intent);
                break;
            case 1:
                intent = new Intent (TodoActivity.this, ReceivedBookActivity.class);
                startActivity (intent);
                break;
        }
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