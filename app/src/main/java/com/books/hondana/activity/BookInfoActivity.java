//本の詳細ページ
package com.books.hondana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.books.hondana.Model.KiiBook;
import com.books.hondana.R;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiUser;
import com.nostra13.universalimageloader.core.ImageLoader;


public class BookInfoActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = BookInfoActivity.class.getSimpleName();

    //private BaseAdapter adapter;

    private KiiBook kiiBook;


    /*private static final String[] username = {
            // Scenes of Isle of Wight
            "ユーザー名",
            "ユーザー名",
            "ユーザー名",
    };

    private static final String[] evaluation = {
            // Scenes of Isle of Wight
            "評価",
            "評価",
            "評価",
    };*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        LogUtil.d (TAG, "onCreate");

        kiiBook = getIntent().getParcelableExtra(KiiBook.class.getSimpleName());
        String imgUrl = kiiBook.get(KiiBook.IMAGE_URL);
        LogUtil.d(TAG, kiiBook.get(KiiBook.TITLE));

        if ((imgUrl != null) && (imgUrl.length() > 0)) {
            // 画像データのダウンロードと設定
            ImageLoader imageLoader = ImageLoader.getInstance();
            ImageView imgView = (ImageView) findViewById(R.id.imageViewBookInfo);
            imageLoader.displayImage(imgUrl, imgView);
        }

        TextView tv_title = (TextView) findViewById(R.id.textViewBookInfoTitle);
        tv_title.setText(kiiBook.get(KiiBook.TITLE));
        TextView tv_author = (TextView) findViewById(R.id.textViewBookInfoAuthor);
        tv_author.setText(kiiBook.get(KiiBook.AUTHOR));
        TextView tv_isbn = (TextView) findViewById(R.id.textViewBookInfoIsbn);
        tv_isbn.setText(kiiBook.get(KiiBook.ISBN));
        TextView tv_publisher = (TextView) findViewById(R.id.textViewBookInfoPublisher);
        tv_publisher.setText(kiiBook.get(KiiBook.PUBLISHER));
        TextView tv_issueDate = (TextView) findViewById(R.id.textViewBookInfoDataOfIssue);
        tv_issueDate.setText(kiiBook.get(KiiBook.ISSUE_DATE));
//        TextView tv_large_title = (TextView) findViewById(R.id.textViewBookInfoLargeTitle);
//        tv_large_title.setText(kiiBook.get(KiiBook.TITLE));
//        TextView tv_large_author = (TextView) findViewById(R.id.textViewBookInfoLargeAuthor);
//        tv_large_author.setText(kiiBook.get(KiiBook.AUTHOR));


        findViewById(R.id.buttonPreRequest).setOnClickListener(this);

//        getCurrentUser();

        /*// ListViewのインスタンスを生成
        ListView listViewBookOwner = (ListView) findViewById(R.id.listViewBookOwner);

        // BaseAdapter を継承したadapterのインスタンスを生成

        adapter = new BookInfoListViewAdapter(this.getApplicationContext(), R.layout.part_book_owner, username, evaluation);

        // ListViewにadapterをセット
        listViewBookOwner.setAdapter(adapter);

        // 後で使います
//        listViewBookOwner.setOnItemClickListener(this);*/
    }

    @Override
    public void onClick(View v) {
        LogUtil.d(TAG, "onClick");

        KiiUser kiiUser = KiiUser.getCurrentUser ();
        LogUtil.d (TAG, "kiiUser: " + kiiUser);
//        String owner = kiiBook.get("_owner");
//        LogUtil.d(TAG, "owner: " + owner);
//
//
//        if ((kiiUser != null) && (KiiUser != owner)) {

        if(kiiUser != null){

            //kiiBookのデータを持って、BookRequestActivityにintentする
            startActivity(RequestBookActivity.createIntent (this, kiiBook));
        } else {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            showToast("会員登録をお願いします！");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}