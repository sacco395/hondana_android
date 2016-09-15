//本のリクエストをする
package com.books.hondana.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.books.hondana.Model.PdfLabel;
import com.books.hondana.Model.Request;
import com.books.hondana.Model.abst.KiiModel;
import com.books.hondana.R;
import com.books.hondana.util.LogUtil;
import com.books.hondana.util.UriUtil;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiObjectBodyCallback;
import com.kii.cloud.storage.callback.KiiObjectCallBack;
import com.kii.cloud.storage.callback.KiiObjectPublishCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class RequestBookActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = RequestBookActivity.class.getSimpleName();

    private Request request;
    //今回使用するインテントの結果の番号。適当な値でOK.
    private static final int PDF_CHOOSER_CODE = 2;

    private static final String PDF_CHOOSER_TITLE = "PDF ファイルを選択してください";

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
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        startChooserActivity();
                    } else {
                        RequestBookActivityPermissionsDispatcher.startChooserActivityWithPermissionWithCheck(this);
                    }
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

    private void startChooserActivity() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, PDF_CHOOSER_TITLE), PDF_CHOOSER_CODE);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void startChooserActivityWithPermission() {
        startChooserActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PDF_CHOOSER_CODE) {

            if (resultCode != RESULT_OK) {
                // Error!
                return;
            }

            Uri uri = data.getData();
            String path = UriUtil.getPath(this, uri);

            if (path != null) {
                LogUtil.d(TAG, "onActivityResult: File found!");
                postPdf(new File(path));
            } else {
                Log.e(TAG, "onActivityResult: File not found!");
            }
        }
    }

    private void postPdf(final File pdfFile) {
        PdfLabel pdfLabel = PdfLabel.createNew(request);
        pdfLabel.savePdf(pdfFile, new PdfLabel.PdfUploadCallback() {
            @Override
            public void failure(IllegalStateException e) {
                LogUtil.w(TAG, e);
            }

            @Override
            public void onTransferStart(@NonNull KiiObject kiiObject) {

            }

            @Override
            public void onTransferCompleted(@NonNull KiiObject kiiObject, @Nullable Exception e) {

                if (e != null) {
                    // Error handling
                    Log.e(TAG, "onTransferCompleted: ", e);
                    return;
                }

                kiiObject.refresh(new KiiObjectCallBack() {
                    public void onRefreshCompleted(int token, KiiObject object, Exception exception) {
                        int time = 60 * 60 * 72;//72時間後に消去
                        object.publishBodyExpiresIn(time, new KiiObjectPublishCallback() {
                            @Override
                            public void onPublishCompleted(String url, KiiObject object, Exception exception) {
                                if (exception != null) {
                                    // Error handling
                                    LogUtil.d(TAG, ("公開されてません"));
                                }
                            }
                        });
                    }
                });

                Toast.makeText(RequestBookActivity.this, "PDFが投稿されました！",
                        Toast.LENGTH_LONG).show();
                LogUtil.d(TAG, ("投稿されました"));
            }

            @Override
            public void onTransferProgress(@NonNull KiiObject kiiObject, long l, long l1) {

            }
        });
    }

    /*//投稿処理。画像のUploadがうまくいったときは、urlに公開のURLがセットされる
    public void postUrl(String url) {
        //バケット名を設定。
        KiiObject object = Kii.bucket("members").object();
        //Json形式でKeyのcommentをセット.{"comment":"こめんとです","imageUrl":"http://xxx.com/xxxx"}
        object.set ("url_Pdf", url);

        //データをKiiCloudに保存
        object.save (new KiiObjectCallBack () {
            //保存結果が帰ってくるコールバック関数。自動的に呼び出される。
            @Override
            public void onSaveCompleted(int token, KiiObject object, Exception exception) {
                //エラーがないとき
                if (exception == null) {
                    // Intent のインスタンスを取得する。getApplicationContext()で自分のコンテキストを取得。遷移先のアクティビティーを.classで指定
                    Intent intent = new Intent (getApplicationContext (), RequestBookActivity.class);
                    //Activityを終了します。
                    finish ();
                } else {
                    LogUtil.e (TAG, "投稿されません。。。", exception);
//
                }
            }
        });
    }*/

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
        if (user == null || user.getID() == null) {
            Log.e(TAG, "saveRequestDate: KiiUser is null!");
            Toast.makeText(RequestBookActivity.this, "ログイン情報を取得できませんでした。再度ログインしてください。", Toast.LENGTH_SHORT).show();
            // TODO: 9/13/16 Launch LoginActivity or something
            return;
        }

        request.setRequestedDate(dateString);
        request.save(false, new KiiModel.KiiSaveCallback() {
            @Override
            public void success(int token, KiiObject object) {
                Toast.makeText(getApplicationContext(), "本のリクエストを完了しました", Toast.LENGTH_LONG).show();

                //暫定的にTOPページにintentする
                Intent intent = new Intent(RequestBookActivity.this, BookMainActivity.class);
                startActivity(intent);
            }

            @Override
            public void failure(@Nullable Exception e) {
                Log.e(TAG, "failure: ", e);
                Toast.makeText(RequestBookActivity.this, "本のリクエストに失敗しました。", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RequestBookActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}