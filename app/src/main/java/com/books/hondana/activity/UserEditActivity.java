//ユーザーの編集
package com.books.hondana.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.books.hondana.R;
import com.books.hondana.connection.KiiMemberConnection;
import com.books.hondana.connection.KiiObjectCallback;
import com.books.hondana.model.Member;
import com.books.hondana.model.abst.KiiModel;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiObjectCallBack;
import com.kii.cloud.storage.callback.KiiObjectPublishCallback;
import com.kii.cloud.storage.resumabletransfer.KiiRTransfer;
import com.kii.cloud.storage.resumabletransfer.KiiRTransferCallback;
import com.kii.cloud.storage.resumabletransfer.KiiUploader;

import java.io.File;
import java.io.FileOutputStream;

public class UserEditActivity extends AppCompatActivity {

    private static final String TAG = "UserEditActivity";
    //今回使用するインテントの結果の番号。適当な値でOK.
    private static final int IMAGE_CHOOSER_RESULTCODE = 1;
    //画像のパスを保存しておく
    private String mImagePath = null;
    //UPした画像のKiiObject
    private KiiObject mKiiImageObject = null;
    //入力したコメント
    private String profile;
    //カメラで撮影した画像のuri
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        // ツールバーをアクションバーとしてセット
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle("プロフィールの設定");
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //画像ボタンにクリックイベントを追加しています。
        Button attachBtn = (Button) findViewById(R.id.attach_button);
        attachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //クリックした時は画像選択
                onAttachFileButtonClicked(v);
            }
        });

        //投稿ボタンにクリックイベントを追加しています。
        Button postBtn = (Button) findViewById(R.id.post_button);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //クリックした時は投稿する
                onPostButtonClicked(v);
            }
        });
    }

    //画像の添付ボタンをおした時の処理
    public void onAttachFileButtonClicked(View v) {
        //ギャラリーを開くインテントを作成して起動する。
        Intent intent = new Intent();
        //フアイルのタイプを設定
        intent.setType("image/*");
        //画像のインテント
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //Activityを起動
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_CHOOSER_RESULTCODE);
    }

    //画像を選択した後に実行されるコールバック関数。インテントの実行された後にコールバックされる。自動的に実行されます。
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //他のインテントの実行結果と区別するためstartActivityで指定した定数IMAGE_CHOOSER_RESULTCODEと一致するか確認
        if (requestCode == IMAGE_CHOOSER_RESULTCODE) {
            //失敗の時
            if (resultCode != RESULT_OK) {
                return;
            }

            //画像を取得する。Xperiaの場合はdataに画像が入っている。それ以外はintentで設定したmImageUriに入っている。
            Uri result;
            if (data != null) {
                result = data.getData();
            } else {
                result = mImageUri;
                LogUtil.d ("hondana:mImageUri:", result.toString());
            }
            //画面に画像を表示
            ImageView iv = (ImageView) findViewById(R.id.image_view1);
            assert iv != null;
            iv.setImageURI(result);


            //画像のパスを設定。Uploadでつかう。
            mImagePath = getFilePathByUri (result);

        }
    }

    //uriからファイルのパスを取得する。バージョンによって処理が違う。KiiCloudのチュートリアルから取り込んだ。汎用的に使えます。
    private String getFilePathByUri(Uri selectedFileUri) {
        //4.2以降の時
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // Workaround of retrieving file image through ContentResolver
            // for Android4.2 or later
            String filePath = null;
            FileOutputStream fos = null;
            try {
                //ビットマップを取得
                Bitmap bmp = MediaStore.Images.Media.getBitmap(
                        this.getContentResolver(), selectedFileUri);
                //一時保存するディレクトリ。
                String cacheDir = getCacheDir() + File.separator + "hondana";
                //ディレクトリ作成
                File createDir = new File(cacheDir);
                if (!createDir.exists()) {
                    createDir.mkdir();
                }
                //一時ファイル名を作成。毎回上書き
                filePath = cacheDir + File.separator + "upload.jpg";
                File file = new File(filePath);
                //ビットマップをjpgに変換して一時的に保存する。
                fos = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.JPEG, 95, fos);
                fos.flush();
                fos.getFD().sync();
            } catch (Exception e) {//エラーの時
                filePath = null;
                LogUtil.e (TAG, "投稿されません", e);
            } finally {//かならず最後に閉じる実行する処理
                if (fos != null) {
                    try {
                        //ファイルを閉じる
                        fos.close();
                    } catch (Exception e) {
                        // Nothing to do
                    }
                }
            }
            return filePath;
        } else {
            //データから探す
            String[] filePathColumn = {MediaStore.MediaColumns.DATA};
            Cursor cursor = this.getContentResolver().query(
                    selectedFileUri, filePathColumn, null, null, null);

            if (cursor == null)
                return null;
            try {
                if (!cursor.moveToFirst())
                    return null;
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                if (columnIndex < 0) {
                    return null;
                }
                //これがファイルのパス
                return cursor.getString(columnIndex);
            } finally {
                cursor.close();
            }
        }
    }

    //投稿ボタンを御した時の処理
    public void onPostButtonClicked(View v) {
        //入力文字を得る
        EditText mCommentField = (EditText) (findViewById(R.id.comment_field));
        assert mCommentField != null;
        profile = mCommentField.getText().toString();
        LogUtil.d ("profile", ":" + profile + ":");

        //未入力の時はエラー.""は文字が空
        if (profile.equals("")) {
            //ダイアログを表示
//            showAlert(getString(R.string.no_data_message));
//            return;
        }
        //画像をUPしてからusersに投稿。
        if (mImagePath != null) {
            //ファイルをUP、完了した時にpostImagesを実行している。
            uploadFile(mImagePath);
        } else {
            //画像がないときはcommentだけ登録
            postImages(null);
        }
    }

    //投稿処理。画像のUploadがうまくいったときは、urlに公開のURLがセットされる
    KiiUser kiiUser = KiiUser.getCurrentUser();
    final String userId = kiiUser.getID();
    final String user_name = kiiUser.getUsername();
    public void postImages(final String url) {
        KiiMemberConnection.fetch(userId, new KiiObjectCallback<Member>() {
            @Override
            public void success(int token, Member member) {
                member.setName(user_name);
                member.setImageUrl(url);
                member.setProfile(profile);
                member.save(false, new KiiModel.KiiSaveCallback() {
                    @Override
                    public void success(int token, KiiObject object) {
//                        Intent intent = new Intent(getApplicationContext(), UserpageActivity.class);
//                        //Activityを終了します。
//                        startActivity(intent);
//                        finish();
                        Log.d (TAG, "success: ");
                        Intent intent = new Intent ();
                        setResult (Activity.RESULT_OK, intent);
                        finish ();
                    }

                    @Override
                    public void failure(@Nullable Exception e) {
                        LogUtil.e(TAG, "投稿されません。。。", e);
                    }
                });
            }

            @Override
            public void failure(Exception e) {

            }
        });
    }

                    //KiiCloudのimagesバケットにオブジェクトを作成する。参考：チュートリアル、http://www.riaxdnp.jp/?p=6775
                    private void uploadFile(String path) {
                        //イメージを保存するバケット名を設定。すべてここに保存してusersにはそのhttpパスを設定する。
                        KiiObject object = Kii.bucket("images").object();
                        object.set("image", "");
                        object.save(new KiiObjectCallBack() {
                            @Override
                            public void onSaveCompleted(int token, @NonNull KiiObject object, Exception exception) {

                            }
                        });

                        //Up後に公開設定するので保存
                        mKiiImageObject = object;
                        File f = new File(path);
                        //KiiCloudにUPするインスタンス
                        KiiUploader uploader = object.uploader(this, f);
                        //非同期でUpする。
                        uploader.transferAsync(new KiiRTransferCallback() {
                            //完了した時
                            @Override
                            public void onTransferCompleted(@NonNull KiiRTransfer operator, Exception e) {
                                if (e == null) {
                                    //成功の時
                                    //画像を一覧で表示するため、公開状態にする。参考：http://www.riaxdnp.jp/?p=6841
                                    // URI指定Objをリフレッシュして、最新状態にする
                                    mKiiImageObject.refresh(new KiiObjectCallBack() {
                                        public void onRefreshCompleted(int token, @NonNull KiiObject object, Exception e) {
                                            if (e == null) {
                                                // ObjectBodyの公開設定する
                                                object.publishBody(new KiiObjectPublishCallback() {
                                                    @Override
                                                    public void onPublishCompleted(String url, @NonNull KiiObject kiiObject, Exception e) {
                                                        LogUtil.d(TAG, ("公開されました！"));
                                                        //画像のURL付きでusersに投稿する。
                                                        postImages(url);
                                                    }
                                                });
                                            }
                                        }
                                    });


                                } else {
                                    LogUtil.d(TAG, ("公開されないっす"));
                                }
                            }
                        });
                    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
