package com.books.hondana.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class BaseScannerActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //    public void setupToolbar() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        final ActionBar ab = getSupportActionBar();
//        if(ab != null) {
//            ab.setDisplayHomeAsUpEnabled(true);
//        }
//    }
}