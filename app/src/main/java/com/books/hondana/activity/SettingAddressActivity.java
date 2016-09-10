package com.books.hondana.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.books.hondana.R;

public class SettingAddressActivity extends AppCompatActivity {

    private static final String TAG = "SettingAddressActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_address_setting);

        // ツールバーをアクションバーとしてセット
        Toolbar toolbar = (Toolbar) findViewById (R.id.toolbar);
        toolbar.setTitle ("住所の設定");
        setSupportActionBar (toolbar);


        ActionBar actionBar = getSupportActionBar ();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled (true);
        }
    }


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