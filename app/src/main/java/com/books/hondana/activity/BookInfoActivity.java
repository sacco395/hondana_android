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
import com.books.hondana.Model.Request;
import com.books.hondana.Model.book.Book;
import com.books.hondana.Model.book.Condition;
import com.books.hondana.Model.book.Info;
import com.books.hondana.Model.book.Size;
import com.books.hondana.Model.book.Smell;
import com.books.hondana.Model.kii.KiiCloudBucket;
import com.books.hondana.Model.kii.KiiMember;
import com.books.hondana.R;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.query.KiiQueryResult;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.MessageFormat;
import java.util.List;


public class BookInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = BookInfoActivity.class.getSimpleName();

    //private BaseAdapter adapter;

    private Book book;

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

        book = getIntent().getParcelableExtra(Book.class.getSimpleName());
        Info info = book.getInfo();
        String imgUrl = info.getImageUrl();
        LogUtil.d(TAG, info.getTitle());

        if ((imgUrl != null) && (imgUrl.length() > 0)) {
            // 画像データのダウンロードと設定
            ImageLoader imageLoader = ImageLoader.getInstance();
            ImageView imgView = (ImageView) findViewById(R.id.imageViewBookInfo);
            imageLoader.displayImage(imgUrl, imgView);
        }

        TextView tv_title = (TextView) findViewById(R.id.textViewBookInfoTitle);
        tv_title.setText(info.getTitle());
        TextView tv_author = (TextView) findViewById(R.id.textViewBookInfoAuthor);
        tv_author.setText(info.getAuthor());
        TextView tv_isbn = (TextView) findViewById(R.id.textViewBookInfoIsbn);
        tv_isbn.setText(info.getIsbn());
        TextView tv_publisher = (TextView) findViewById(R.id.textViewBookInfoPublisher);
        tv_publisher.setText(info.getIsbn());
        TextView tv_issueDate = (TextView) findViewById(R.id.textViewBookInfoDataOfIssue);
        tv_issueDate.setText(info.getIssueDate());

        Condition condition = book.getCondition();
        TextView tv_bookCondition = (TextView) findViewById(R.id.bookInfoCondition);
        tv_bookCondition.setText(condition.getEvaluation());

        ImageView iv_bookCondition = (ImageView) findViewById(R.id.bookInfoBookConditionIcon);
        int resId = condition.getIconDrawableResId();
        if (resId == 0) return;
        Drawable conditionDrawable = ResourcesCompat.getDrawable(getResources(), resId, null);
        iv_bookCondition.setImageDrawable(conditionDrawable);

        TextView tv_bookLine = (TextView) findViewById(R.id.bookInfoLine);
        tv_bookLine.setText(condition.getLined());

        TextView tv_bookBroken = (TextView) findViewById(R.id.bookInfoBroken);
        tv_bookBroken.setText(condition.getBand());

        TextView tv_bookNotes = (TextView) findViewById(R.id.bookInfoNotes);
        tv_bookNotes.setText(condition.getNoted());

//本のその他の状態
        // 空の文字列を作成
        String etcText = "";
        // 日焼け情報を追加
        String band = condition.getBandText();
        // sunburned が空の文字列でなければ、読点を挿入（ここは趣味で）
        if (!band.equals("")) {
            band += "／";
        }
        etcText += band;

        String sunburned = condition.getSunburnedText();
        // sunburned が空の文字列でなければ、読点を挿入（ここは趣味で）
        if (!sunburned.equals("")) {
            sunburned += "／";
        }
        etcText += sunburned;

        String scratched = condition.getScratchedText();
        if (!scratched.equals("")) {
            scratched += "／";
        }
        etcText += scratched;

        Smell smell = book.getSmell();

        String cigar_smell = smell.getCigarSmellText();
        if (!cigar_smell.equals("")) {
            cigar_smell += "／";
        }
        etcText += cigar_smell;

        String petSmell = smell.getPetSmellText();
        if (!petSmell.equals("")) {
            petSmell += "／";
        }
        etcText += petSmell;

        String mold_smell = smell.getMoldSmellText();
        if (!mold_smell.equals("")) {
            mold_smell += "／";
        }
        etcText += mold_smell;


        TextView tv_bookEtc = (TextView) findViewById(R.id.bookInfoEtc);
        tv_bookEtc.setText(etcText);
//本のその他の状態ここまで

        Size size = book.getSize();

        //本のサイズここから
        TextView tv_bookInfoSize = (TextView) findViewById(R.id.bookInfoSize);
        tv_bookInfoSize.setText(MessageFormat.format ("縦{0}cm × 横{1}cm × 厚さ{2}cm", size.getHeight(), size.getWidth(), size.getDepth()));


        TextView tv_bookInfoWeight = (TextView) findViewById(R.id.bookInfoWeight);
        tv_bookInfoWeight.setText(size.getWeight() + "g");

        //本のサイズここまで

        findViewById(R.id.buttonPreRequest).setOnClickListener(this);

        final TextView bookOwner = (TextView) findViewById(R.id.textViewBookInfoUserName);
        final ImageView userIcon = (ImageView) findViewById(R.id.bookInfoUserIcon);

        final String userId = book.getOwnerId();
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
                    final KiiMember kiiMember = new KiiMember(kiiObject);

                    final String name = kiiMember.get(KiiMember.NAME);
                    Log.d(TAG, "name: " + name);
                    bookOwner.setText(name);

                    final String imageUrl = kiiMember.get(KiiMember.IMAGE_URL);
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

        KiiUser currentUser = KiiUser.getCurrentUser ();
        if (currentUser == null) {
            Log.d(TAG, "onClick: Current KiiUser is null!");
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            showToast("会員登録をお願いします！");
            return;
        }
        LogUtil.d (TAG, "kiiUser: " + currentUser);

        Request request = new Request();
        request.setClientUserId(currentUser.getID());
        request.setBook(book);

        startActivity(RequestBookActivity.createIntent (this, request));
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}