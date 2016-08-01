package com.books.hondana;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class LikesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("気になる本一覧");
        setSupportActionBar(toolbar);



            // 表示させるデータを設定する。
            // データを格納するためのArrayListを宣言
            ArrayList<HashMap<String,Object>> outputArray = new ArrayList<HashMap<String,Object>>();
            // ↑ここがポイント１

            for( int i = 0; i < 3; i++ ) {
                HashMap<String, Object> item = new HashMap<String, Object>();
                // 画像の設定（とりあえず全ての項目に同じ画像を入れています。）
                item.put("iconKey", R.drawable.changedesign);
                // 文字列の設定（とりあえず、ループのカウンタを表示させています。）
                item.put("textKey", i + "番目");
                // 表示用のArrayListに設定
                outputArray.add(item);
            }

            // 画像表示用に作成したCustomAdapterに、上記ArrayListを設定
            SimpleAdapter myAdapter = new SimpleAdapter(
                    this,
                    outputArray,
                    R.layout.part_book_list,  // ここがポイント２
                    new String[]{"iconKey","textKey"}, // ここがポイント３－１
                    new int[]{R.id.book_image,R.id.book_title} // ここがポイント３－２
            );

            // ListViewにmyAdapterをセット
            ListView listView = (ListView)findViewById(R.id.listViewMain);
            listView.setAdapter(myAdapter);


//        ListView list = (ListView) findViewById(R.id.listViewMain);
//        String[] item01 = getResources().getStringArray(R.array.array01);
//
//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, item01);
//        list.setAdapter(adapter);
//
//
//
//        // リスト項目がクリックされた時の処理
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView parent, View view, int position, long id) {
//                String strData = adapter.getItem(position);
//
//                Intent intent = new Intent();
//
//                switch (position) {
//                    case 0:
//                        intent.setClass(LikesActivity.this, LikesActivity_01.class);
//                        break;
//                    case 1:
//                        intent.setClass(LikesActivity.this, LikesActivity_02.class);
//                        break;
//                }
//                intent.putExtra("SELECTED_DATA", strData);
//                startActivity(intent);
//            }
//        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //スキャン画面へ移動
                Intent intent = new Intent(LikesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.likes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}