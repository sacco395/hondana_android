//本の受け取り完了（発送者への評価）
package com.books.hondana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.books.hondana.R;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiObjectCallBack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReceivedBookActivity extends AppCompatActivity implements View.OnClickListener {

//    //TodoActivityからkiiBookの情報を受け取るためcreateIntentを使う
//    private static final String EXTRA_KII_BOOK = "extra_kii_book";
//
//    public static Intent createIntent(Context context, KiiBook kiiBook) {
//        Intent intent = new Intent (context, ReceivedBookActivity.class);
//        intent.putExtra (EXTRA_KII_BOOK, kiiBook);
//        return intent;
//    }
//
//    KiiBook kiiBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieved_book);

        // Intent を取得。
        //Intentでアクティビティー間のデータを受け渡しします。Intentの値を受け取るために作成。
//        Intent intent = getIntent();
//        // キーを元にインテントデータを取得する
//        String id  = intent.getParcelableExtra("_id");
//        //デバッグログ
//        LogUtil.d("get book_id", id);

//        //上記のcreateIntentでデータを受け取る
//        kiiBook = getIntent ().getParcelableExtra (EXTRA_KII_BOOK);
////kiiBookがないのはおかしいのでcreateIntentを使うように怒る
//        if (kiiBook == null) {
//            throw new IllegalArgumentException ("createIntentを使ってください");
//        }

        findViewById(R.id.buttonReceived).setOnClickListener(this);
    }

    //kiiBookに本の受け取り日時を記録して保存する
    @Override
    public void onClick(View v) {
        Date date = new Date ();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss", Locale.JAPAN);
        String dateString = simpleDateFormat.format (date); // 2016-09-03 17:24:33

        Intent intent = getIntent();
        // キーを元にインテントデータを取得する
        String id  = intent.getStringExtra("_id");
        LogUtil.d("get book_id", id);
        KiiObject object = Kii.bucket("appbooks").object(id);

        object.set ("received_date", dateString);
        object.save (new KiiObjectCallBack () {
            @Override
            public void onSaveCompleted(int token, @NonNull KiiObject object, @Nullable Exception exception) {
                Toast.makeText(getApplicationContext(), "本の受け取りと評価を完了しました", Toast.LENGTH_LONG).show();
                //評価機能をつけたら"本の受け取りと評価を完了しました"に変更する


                //暫定的にTOPページにintentする
                Intent intent = new Intent(ReceivedBookActivity.this, BookMainActivity.class);
                startActivity(intent);
            }
        });
    }
}
