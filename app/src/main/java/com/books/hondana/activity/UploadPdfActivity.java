//ユーザーの編集
package com.books.hondana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.books.hondana.R;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiObjectBodyCallback;
import com.kii.cloud.storage.callback.KiiObjectCallBack;

import java.io.File;

public class UploadPdfActivity extends AppCompatActivity {

    //今回使用するインテントの結果の番号。適当な値でOK.
    private static final int IMAGE_CHOOSER_RESULTCODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);
        //画像ボタンにクリックイベントを追加しています。
        Button attachBtn = (Button) findViewById(R.id.attach_button2);
        attachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //クリックした時は画像選択
                onAttachFileButtonClicked(v);
            }
        });
    }

    //画像の添付ボタンをおした時の処理
    public void onAttachFileButtonClicked(View v) {

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
    }
}