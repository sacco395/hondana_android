//本の受け取り完了（発送者への評価）
package com.books.hondana.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.books.hondana.Model.kii.KiiBook;
import com.books.hondana.R;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiObjectCallBack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EvaluatedActivity extends AppCompatActivity implements View.OnClickListener {

    //TodoActivityからkiiBookの情報を受け取るためcreateIntentを使う
    private static final String EXTRA_KII_BOOK = "extra_kii_book";

    public static Intent createIntent(Context context, KiiBook kiiBook) {
        Intent intent = new Intent (context, EvaluatedActivity.class);
        intent.putExtra (EXTRA_KII_BOOK, kiiBook);
        return intent;
    }

    KiiBook kiiBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluated);

        //上記のcreateIntentでデータを受け取る
        kiiBook = getIntent ().getParcelableExtra (EXTRA_KII_BOOK);
//kiiBookがないのはおかしいのでcreateIntentを使うように怒る
        if (kiiBook == null) {
            throw new IllegalArgumentException ("createIntentを使ってください");
        }

        findViewById(R.id.buttonEvaluated).setOnClickListener(this);
    }


    //kiiBookに本の受け取り日時を記録して保存する
    @Override
    public void onClick(View v) {
        Date date = new Date ();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss", Locale.JAPAN);
        String dateString = simpleDateFormat.format (date); // 2016-09-03 17:24:33

        kiiBook.set ("evaluated_date", dateString);
        kiiBook.save (new KiiObjectCallBack () {
            @Override
            public void onSaveCompleted(int token, @NonNull KiiObject object, @Nullable Exception exception) {
                Toast.makeText(getApplicationContext(), "評価を完了しました", Toast.LENGTH_LONG).show();

                //暫定的にTOPページにintentする
                Intent intent = new Intent(EvaluatedActivity.this, BookMainActivity.class);
                startActivity(intent);
            }
        });
    }
}
