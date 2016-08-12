package com.books.hondana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.books.hondana.R;

public class PassedActivity extends AppCompatActivity
        implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passed);


        findViewById(R.id.buttonCancel).setOnClickListener(this);
        findViewById(R.id.buttonSending).setOnClickListener(this);

// ツールバーをアクションバーとしてセット
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar02);
        setSupportActionBar(toolbar);

    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.buttonCancel:
                    // クリック処理
                    finish();
                    break;

                case R.id.buttonSending:
                    // クリック処理
                    Intent intent = new Intent(this, EvaluateActivity.class);
                    startActivity(intent);
                    break;


                default:
                    break;
            }
        }
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
