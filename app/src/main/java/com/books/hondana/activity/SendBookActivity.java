//本の発送完了（ラベルダウンロード）
package com.books.hondana.activity;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.books.hondana.R;
import com.books.hondana.model.Request;
import com.books.hondana.model.abst.KiiModel;
import com.kii.cloud.storage.KiiObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SendBookActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = SendBookActivity.class.getSimpleName();

    private Request request;

    private File pdfFile;

    public static Intent createIntent(Context context, Request request) {
        Intent intent = new Intent(context, SendBookActivity.class);
        intent.putExtra(Request.class.getSimpleName(), request);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_book);

        //上記のcreateIntentでデータを受け取る
        request = getIntent().getParcelableExtra(Request.class.getSimpleName());
        //kiiBookがないのはおかしいのでcreateIntentを使うように怒る
        if (request == null) {
            throw new IllegalArgumentException("createIntentを使ってください");
        }

        findViewById(R.id.buttonDownload).setOnClickListener(this);
        findViewById(R.id.buttonCancel).setOnClickListener(this);
        findViewById(R.id.buttonSent).setOnClickListener(this);

// ツールバーをアクションバーとしてセット
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar02);
        setSupportActionBar(toolbar);

    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.buttonDownload:
                    downLoadPdf(pdfFile);
                    break;

                case R.id.buttonCancel:
                    // クリック処理
                    finish();
                    break;

                case R.id.buttonSent:
                    // クリック処理
                    saveSendDate();
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
        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.JAPAN);
        String dateString = simpleDateFormat.format(date); // 2016-09-03 17:24:33

        request.setSentDate(dateString);
        request.save(false, new KiiModel.KiiSaveCallback() {
            @Override
            public void success(int token, KiiObject object) {
                Toast.makeText(getApplicationContext(), "本の発送を完了しました", Toast.LENGTH_LONG).show();

                //暫定的にTOPページにintentする
                Intent intent = new Intent(SendBookActivity.this, BookMainActivity.class);
                startActivity(intent);
            }

            @Override
            public void failure(@Nullable Exception e) {
                Log.e(TAG, "failure: ", e);
                Toast.makeText(SendBookActivity.this, "本の発送に失敗しました。", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downLoadPdf(final File pdfFile) {
        request.downloadPdf(pdfFile, new Request.PdfDownloadCallback() {

            @Override
            public void onTransferStart(@NonNull KiiObject kiiObject) {

            }

            @Override//繋がっていないため未チェック
            public void onTransferCompleted(@NonNull KiiObject kiiObject, @Nullable Exception e) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(pdfFile), "application/pdf");
                startActivity(intent);
            }

            @Override
            public void onTransferProgress(@NonNull KiiObject kiiObject, long l, long l1) {

            }
        });
    }
}

