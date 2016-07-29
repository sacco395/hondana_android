package com.books.hondana;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GuideListActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_list);


        ListView list = (ListView) findViewById(R.id.listViewMain);
        String[] item01 = getResources().getStringArray(R.array.array01);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, item01);
        list.setAdapter(adapter);

        // リスト項目がクリックされた時の処理
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                String strData = adapter.getItem(position);

                Intent intent = new Intent();

                switch (position) {
                    case 0:
                        intent.setClass(GuideListActivity.this, GuideActivity_01.class);
                        break;
                    case 1:
                        intent.setClass(GuideListActivity.this, GuideActivity_02.class);
                        break;
                }
                intent.putExtra("SELECTED_DATA", strData);
                startActivity(intent);
            }
        });
    }
}
