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

import com.books.hondana.Model.Request;
import com.books.hondana.Model.kii.KiiBook;
import com.books.hondana.R;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiObjectCallBack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RequestBookActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = RequestBookActivity.class.getSimpleName();

    private Request request;

    public static Intent createIntent(Context context, Request request) {
        Intent intent = new Intent (context, RequestBookActivity.class);
        intent.putExtra(Request.class.getSimpleName(), request);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_book);

//上記のcreateIntentでデータを受け取る
        request = getIntent ().getParcelableExtra (Request.class.getSimpleName());
//kiiBookがないのはおかしいのでcreateIntentを使うように怒る
        if (request == null) {
            throw new IllegalArgumentException ("createIntentを使ってください");
        }

        findViewById(R.id.buttonClickPost).setOnClickListener(this);
        findViewById(R.id.buttonCancel).setOnClickListener(this);
        findViewById(R.id.buttonRequest).setOnClickListener(this);
        findViewById(R.id.buttonSelectFile).setOnClickListener(this);


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

                case R.id.buttonSelectFile:
                    // クリック処理
                    Intent intent = new Intent(this, UploadPdfActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                case R.id.buttonCancel:
                    // クリック処理
                    finish();
                    break;

                case R.id.buttonRequest:
                    // クリック処理（交換リクエストの日時とユーザーIDを保存）
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

    //kiiBookに交換リクエストの日時と申請したユーザーIDを保存
    private void saveRequestDate() {
        Date date = new Date ();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss", Locale.JAPAN);
        String dateString = simpleDateFormat.format (date); // 2016-09-03 17:24:33

        KiiBook kiiBook = KiiBook.create(request.getBook());

        kiiBook.set ("request_date", dateString);
        //kiiBook.set ("request_userId",user.getID());

        kiiBook.save (new KiiObjectCallBack () {
            @Override
            public void onSaveCompleted(int token, @NonNull KiiObject object, @Nullable Exception exception) {
                Toast.makeText(getApplicationContext(), "本のリクエストを完了しました", Toast.LENGTH_LONG).show();

                //暫定的にTOPページにintentする
                Intent intent = new Intent(RequestBookActivity.this, BookMainActivity.class);
                startActivity(intent);
            }
        });
    }

}