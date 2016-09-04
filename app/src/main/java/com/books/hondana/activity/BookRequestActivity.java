package com.books.hondana.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.books.hondana.Model.KiiBook;
import com.books.hondana.R;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiObjectCallBack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BookRequestActivity extends AppCompatActivity implements View.OnClickListener {

//BookInfoActivityからkiiBookの情報を受け取るためcreateIntentを使う
    private static final String EXTRA_KII_BOOK = "extra_kii_book";

    public static Intent createIntent(Context context, KiiBook kiiBook) {
        Intent intent = new Intent (context, BookRequestActivity.class);
        intent.putExtra (EXTRA_KII_BOOK, kiiBook);
        return intent;
    }

    KiiBook kiiBook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_request);

//BookInfoActivityからkiiBookの情報を受け取るためcreateIntentを使う
        kiiBook = getIntent ().getParcelableExtra (EXTRA_KII_BOOK);
//kiiBookがないのはおかしいのでcreateIntentを使うように怒る
        if (kiiBook == null) {
            throw new IllegalArgumentException ("createIntentを使ってください");
        }

        findViewById(R.id.buttonClickPost).setOnClickListener(this);
        findViewById(R.id.buttonCancel).setOnClickListener(this);
        findViewById(R.id.buttonRequest).setOnClickListener(this);


//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.buttonClickPost:
                    // クリック処理
                    Uri uri = Uri.parse("http://www.post.japanpost.jp/service/clickpost/index.html");
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(i);
                    break;

                case R.id.buttonCancel:
                    // クリック処理
                    finish();
                    break;

                case R.id.buttonRequest:
                    // クリック処理
                    saveRequestDate ();
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

    private void saveRequestDate() {
        Date date = new Date ();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss", Locale.JAPAN);
        String dateString = simpleDateFormat.format (date); // 2016-09-03 17:24:33

        kiiBook.set ("request_date", dateString);
        kiiBook.save (new KiiObjectCallBack () {
            @Override
            public void onSaveCompleted(int token, @NonNull KiiObject object, @Nullable Exception exception) {
                Toast.makeText(getApplicationContext(), "本のリクエストを完了しました", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(BookRequestActivity.this, BookMainActivity.class);
                startActivity(intent);
            }
        });
    }

}