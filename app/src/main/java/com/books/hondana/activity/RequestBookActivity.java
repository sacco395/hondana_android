package com.books.hondana.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.books.hondana.Model.KiiBook;
import com.books.hondana.R;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiObjectBodyCallback;
import com.kii.cloud.storage.callback.KiiObjectCallBack;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RequestBookActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "BookRequestActivity";

    //今回使用するインテントの結果の番号。適当な値でOK.
    private static final int IMAGE_CHOOSER_RESULTCODE = 2;

    //BookInfoActivityからkiiBookの情報を受け取るためcreateIntentを使う
    private static final String EXTRA_KII_BOOK = "extra_kii_book";

    public static Intent createIntent(Context context, KiiBook kiiBook) {
        Intent intent = new Intent (context, RequestBookActivity.class);
        intent.putExtra (EXTRA_KII_BOOK, kiiBook);
        return intent;
    }

    KiiBook kiiBook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_book);

//上記のcreateIntentでデータを受け取る
        kiiBook = getIntent ().getParcelableExtra (EXTRA_KII_BOOK);
//kiiBookがないのはおかしいのでcreateIntentを使うように怒る
        if (kiiBook == null) {
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
                    // Create an object in an application-scope bucket.
                    KiiObject object = Kii.bucket("pdf").object();
                    // Set key-value pairs.
                    object.set("pdf", "");
                    // Save KiiObject
                    object.save(new KiiObjectCallBack() {
                        @Override
                        public void onSaveCompleted(int token, KiiObject object, Exception exception) {
                            if (exception != null) {
                                // Error handling
                                return;
                            }
                            // Prepare file to upload.
                            File localFile = new File(Environment.getExternalStorageDirectory(),
                                    "myPdf.pdf");

                            // Start uploading
                            object.uploadBody(localFile, "application/pdf", new KiiObjectBodyCallback() {
                                @Override
                                public void onTransferStart(KiiObject kiiObject) {
                                }

                                @Override
                                public void onTransferProgress(KiiObject object, long completedInBytes, long totalSizeinBytes) { /* compiled code */
                                    float progress = (float) completedInBytes / (float) totalSizeinBytes * 100.0f;
                                }

                                @Override
                                public void onTransferCompleted(KiiObject object, Exception exception) {
                                    if (exception != null) {
                                        // Error handling
                                        return;
                                    }
                                }
                            });
                        }
                    });
                    //ギャラリーを開くインテントを作成して起動する。
                    Intent intent = new Intent();
                    //フアイルのタイプを設定
                    intent.setType("application/pdf");
                    //画像のインテント
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    //Activityを起動
                    startActivityForResult(Intent.createChooser(intent, "Select Pdf"), IMAGE_CHOOSER_RESULTCODE);

                    Toast.makeText(RequestBookActivity.this,"PDFが投稿されました！",
                            Toast.LENGTH_LONG).show();
                    LogUtil.d (TAG, ("投稿されました"));

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

        KiiUser user = KiiUser.getCurrentUser();
        LogUtil.d(TAG, "kiiUser: " + user);

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