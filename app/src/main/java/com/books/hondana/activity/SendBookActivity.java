//本の発送完了（ラベルダウンロード）
package com.books.hondana.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class SendBookActivity extends AppCompatActivity
        implements View.OnClickListener {

    //TodoActivityからkiiBookの情報を受け取るためcreateIntentを使う
    private static final String EXTRA_KII_BOOK = "extra_kii_book";

    public static Intent createIntent(Context context, KiiBook kiiBook) {
        Intent intent = new Intent (context, SendBookActivity.class);
        intent.putExtra (EXTRA_KII_BOOK, kiiBook);
        return intent;
    }

    KiiBook kiiBook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_book);

        //上記のcreateIntentでデータを受け取る
        kiiBook = getIntent ().getParcelableExtra (EXTRA_KII_BOOK);
//kiiBookがないのはおかしいのでcreateIntentを使うように怒る
        if (kiiBook == null) {
            throw new IllegalArgumentException ("createIntentを使ってください");
        }


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
                    saveSendDate ();
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

    //kiiBookに本の発送完了日時を記録して保存する
    private void saveSendDate() {
        Date date = new Date ();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss", Locale.JAPAN);
        String dateString = simpleDateFormat.format (date); // 2016-09-03 17:24:33

        kiiBook.set ("send_date", dateString);
        kiiBook.save (new KiiObjectCallBack () {
            @Override
            public void onSaveCompleted(int token, @NonNull KiiObject object, @Nullable Exception exception) {
                Toast.makeText(getApplicationContext(), "本の発送を完了しました", Toast.LENGTH_LONG).show();

                //暫定的にTOPページにintentする
                Intent intent = new Intent(SendBookActivity.this, BookMainActivity.class);
                startActivity(intent);
            }
        });
    }
}
