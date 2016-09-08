//本の詳細ページ
package com.books.hondana.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.books.hondana.Connection.KiiCloudConnection;
import com.books.hondana.Model.KiiBook;
import com.books.hondana.Model.KiiCloudBucket;
import com.books.hondana.Model.Member;
import com.books.hondana.R;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.query.KiiQueryResult;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class BookInfoActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = BookInfoActivity.class.getSimpleName();

    //private BaseAdapter adapter;

    private KiiBook kiiBook;

    final ImageLoader imageLoader = ImageLoader.getInstance();


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


    @SuppressLint("SetTextI18n")
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

        TextView tv_bookCondition = (TextView) findViewById(R.id.bookInfoCondition);
        tv_bookCondition.setText(kiiBook.get(KiiBook.CONDITION));

        ImageView iv_bookCondition = (ImageView) findViewById(R.id.bookInfoBookConditionIcon);
        int resId = kiiBook.getConditionDrawableResId();
        if (resId == 0) return;
        Drawable conditionDrawable = ResourcesCompat.getDrawable(getResources(), resId, null);
        iv_bookCondition.setImageDrawable(conditionDrawable);

        TextView tv_bookLine = (TextView) findViewById(R.id.bookInfoLine);
        tv_bookLine.setText(kiiBook.get(KiiBook.LINE));

        TextView tv_bookBroken = (TextView) findViewById(R.id.bookInfoBroken);
        tv_bookBroken.setText(kiiBook.get(KiiBook.BROKEN));

        TextView tv_bookNotes = (TextView) findViewById(R.id.bookInfoNotes);
        tv_bookNotes.setText(kiiBook.get(KiiBook.NOTES));

//本のその他の状態
        TextView tv_bookEtc = (TextView) findViewById(R.id.bookInfoEtc);
        tv_bookEtc.setText(kiiBook.get(KiiBook.BAND));

        TextView tv_bookEtc2 = (TextView) findViewById(R.id.bookInfoEtc);
        tv_bookEtc2.setText(kiiBook.get(KiiBook.SUNBURNED));

        TextView tv_bookEtc3 = (TextView) findViewById(R.id.bookInfoEtc);
        tv_bookEtc3.setText(kiiBook.get(KiiBook.SCRATCHED));

        TextView tv_bookEtc4 = (TextView) findViewById(R.id.bookInfoEtc);
        tv_bookEtc4.setText(kiiBook.get(KiiBook.CIGAR_SMELL));

        TextView tv_bookEtc5 = (TextView) findViewById(R.id.bookInfoEtc);
        tv_bookEtc5.setText(kiiBook.get(KiiBook.PET_SMELL));

        TextView tv_bookEtc6 = (TextView) findViewById(R.id.bookInfoEtc);
        tv_bookEtc6.setText(kiiBook.get(KiiBook.MOLD_SMELL));
//本のその他の状態ここまで

        //本のサイズここから
        TextView tv_bookInfoSize = (TextView) findViewById(R.id.bookInfoSize);
        tv_bookInfoSize.setText("縦"+kiiBook.get(KiiBook.HEIGHT)+"cm × 横"+kiiBook.get(KiiBook.WIDE)+"cm × 厚さ"+kiiBook.get(KiiBook.DEPTH) +"cm");


        TextView tv_bookInfoWeight = (TextView) findViewById(R.id.bookInfoWeight);
        tv_bookInfoWeight.setText(kiiBook.get(KiiBook.WEIGHT)+"g");

        //本のサイズここまで

        findViewById(R.id.buttonPreRequest).setOnClickListener(this);

        final TextView bookOwner = (TextView) findViewById(R.id.textViewBookInfoUserName);
        final ImageView userIcon = (ImageView) findViewById(R.id.bookInfoUserIcon);

        final String userId = kiiBook.get(KiiBook.USER_ID);
        final KiiCloudConnection membersConnection = new KiiCloudConnection(KiiCloudBucket.MEMBERS);
        membersConnection.loadMember(userId, new KiiCloudConnection.SearchFinishListener() {
            @Override
            public void didFinish(int token, KiiQueryResult<KiiObject> result, Exception e) {
                Log.d(TAG, "didFinish(result: " + result + ")");
                if (result == null) {
                    Log.w(TAG, e);
                    return;
                }

                final List<KiiObject> kiiObjects = result.getResult();
                Log.d(TAG, "members.size: " + kiiObjects.size());
                if (kiiObjects != null && kiiObjects.size() > 0) {
                    final KiiObject kiiObject = kiiObjects.get(0);// ひとつしか来ていないはずなので0番目だけ使う
                    final Member member = new Member(kiiObject);

                    final String name = member.get(Member.NAME);
                    Log.d(TAG, "name: " + name);
                    bookOwner.setText(name);

                    final String imageUrl = member.get(Member.IMAGE_URL);
                    Log.d(TAG, "imageUrl: " + imageUrl);
                    imageLoader.displayImage(imageUrl, userIcon);
                }
            }
        });

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
        kiiBook.set ("request_userId",kiiUser.getID());
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