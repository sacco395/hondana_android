package com.books.hondana.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.books.hondana.R;

public class GuideFirstActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String str = "";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_first);

//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            str = extras.getString("SELECTED_DATA");
//        }
//        TextView txtView = (TextView)findViewById(R.id.textView1);
//        txtView.setText("テスト\nこんにちは");
//
//        Button button = (Button) findViewById(R.id.button1);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();   // アクティビティ終了
//            }
//        });
    }
}