//本の受け取り完了（発送者への評価）
package com.books.hondana.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.books.hondana.Model.Request;
import com.books.hondana.Model.abst.KiiModel;
import com.books.hondana.R;
import com.kii.cloud.storage.KiiObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReceivedBookActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ReceivedBookActivity.class.getSimpleName();

    private Request request;

    public static Intent createIntent(Context context, Request request) {
        Intent intent = new Intent(context, ReceivedBookActivity.class);
        intent.putExtra(Request.class.getSimpleName(), request);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieved_book);
        request = getIntent ().getParcelableExtra(Request.class.getSimpleName());
        if (request == null) {
            throw new IllegalArgumentException ("createIntentを使ってください");
        }

        findViewById(R.id.buttonReceived).setOnClickListener(this);
    }

    //kiiBookに本の受け取り日時を記録して保存する
    @Override
    public void onClick(View v) {
        Date date = new Date ();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss", Locale.JAPAN);
        String dateString = simpleDateFormat.format (date); // 2016-09-03 17:24:33

        request.setReceivedDate(dateString);
        request.save(false, new KiiModel.KiiSaveCallback() {
            @Override
            public void success(int token, KiiObject object) {
                Toast.makeText(ReceivedBookActivity.this, "本の受け取りと評価を完了しました", Toast.LENGTH_LONG).show();
                //暫定的にTOPページにintentする
                Intent intent = new Intent(ReceivedBookActivity.this, BookMainActivity.class);
                startActivity(intent);
            }

            @Override
            public void failure(@Nullable Exception e) {
                Log.e(TAG, "failure: ", e);
                Toast.makeText(ReceivedBookActivity.this, "保存に失敗しました。", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
