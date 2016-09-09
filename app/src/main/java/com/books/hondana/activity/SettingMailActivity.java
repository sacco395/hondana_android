package com.books.hondana.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.books.hondana.R;

public class SettingMailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String str = "";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_setting);

        // ツールバーをアクションバーとしてセット
        Toolbar toolbar = (Toolbar) findViewById (R.id.toolbar);
        toolbar.setTitle ("メールアドレスの設定");
        setSupportActionBar (toolbar);


        ActionBar actionBar = getSupportActionBar ();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled (true);
        }


    }

}