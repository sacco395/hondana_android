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

import com.books.hondana.R;
import com.books.hondana.connection.KiiMemberConnection;
import com.books.hondana.connection.KiiObjectCallback;
import com.books.hondana.model.Book;
import com.books.hondana.model.BookCondition;
import com.books.hondana.model.BookInfo;
import com.books.hondana.model.Member;
import com.books.hondana.model.Request;
import com.books.hondana.model.Size;
import com.books.hondana.model.Smell;
import com.books.hondana.start.StartActivity;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiUser;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.MessageFormat;


public class BookInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = BookInfoActivity.class.getSimpleName();

    private Book book;

    private Request request;

    final ImageLoader imageLoader = ImageLoader.getInstance();


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        LogUtil.d (TAG, "onCreate");

        book = getIntent().getParcelableExtra(Book.class.getSimpleName());
        BookInfo info = book.getInfo();
        String imgUrl = info.getImageUrl();
        LogUtil.d(TAG, info.getTitle());

        if ((imgUrl != null) && (imgUrl.length() > 0)) {
            // 画像データのダウンロードと設定
            ImageLoader imageLoader = ImageLoader.getInstance();
            ImageView imgView = (ImageView) findViewById(R.id.imageViewBookInfo);
            assert imgView != null;
            imageLoader.displayImage(imgUrl, imgView);
        }

        TextView tv_title = (TextView) findViewById(R.id.textViewBookInfoTitle);
        assert tv_title != null;
        tv_title.setText(info.getTitle());
        TextView tv_author = (TextView) findViewById(R.id.textViewBookInfoAuthor);
        assert tv_author != null;
        tv_author.setText(info.getAuthor());
        TextView tv_isbn = (TextView) findViewById(R.id.textViewBookInfoIsbn);
        assert tv_isbn != null;
        tv_isbn.setText(info.getIsbn());
        TextView tv_publisher = (TextView) findViewById(R.id.textViewBookInfoPublisher);
        assert tv_publisher != null;
        tv_publisher.setText(info.getIsbn());
        TextView tv_issueDate = (TextView) findViewById(R.id.textViewBookInfoDataOfIssue);
        assert tv_issueDate != null;
        tv_issueDate.setText(info.getIssueDate());

        BookCondition condition = book.getCondition();
        TextView tv_bookCondition = (TextView) findViewById(R.id.bookInfoCondition);
        assert tv_bookCondition != null;
        tv_bookCondition.setText(condition.getEvaluationText());

        ImageView iv_bookCondition = (ImageView) findViewById(R.id.bookInfoBookConditionIcon);
        int resId = condition.getIconDrawableResId();
        if (resId == 0) return;
        Drawable conditionDrawable = ResourcesCompat.getDrawable(getResources(), resId, null);
        assert iv_bookCondition != null;
        iv_bookCondition.setImageDrawable(conditionDrawable);

        TextView tv_bookLine = (TextView) findViewById(R.id.bookInfoLine);
        assert tv_bookLine != null;
        tv_bookLine.setText(condition.getLinedText());

        TextView tv_bookFolded = (TextView) findViewById(R.id.bookInfoFolded);
        assert tv_bookFolded != null;
        tv_bookFolded.setText(condition.getFoldedText());

        TextView tv_bookNotes = (TextView) findViewById(R.id.bookInfoNotes);
        assert tv_bookNotes != null;
        tv_bookNotes.setText(condition.getNote());

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

        Smell smell = condition.getSmell();

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
        assert tv_bookEtc != null;
        tv_bookEtc.setText(etcText);
//本のその他の状態ここまで

        Size size = info.getSize();

        //本のサイズここから
        TextView tv_bookInfoSize = (TextView) findViewById(R.id.bookInfoSize);
        assert tv_bookInfoSize != null;
        tv_bookInfoSize.setText(MessageFormat.format ("縦{0}cm × 横{1}cm × 厚さ{2}cm", size.getHeight(), size.getWidth(), size.getThickness()));


        TextView tv_bookInfoWeight = (TextView) findViewById(R.id.bookInfoWeight);
        assert tv_bookInfoWeight != null;
        tv_bookInfoWeight.setText(size.getWeight() + "g");

        //本のサイズここまで

        findViewById(R.id.buttonPreRequest).setOnClickListener(this);
        findViewById(R.id.bookInfoLike).setOnClickListener(this);

        final TextView bookOwner = (TextView) findViewById(R.id.textViewBookInfoUserName);
        final ImageView userIcon = (ImageView) findViewById(R.id.bookInfoUserIcon);

        final String userId = book.getOwnerId();
        KiiMemberConnection.fetch(userId, new KiiObjectCallback<Member>() {
            @Override
            public void success(int token, Member member) {
                final String name = member.getName();
                Log.d(TAG, "name: " + name);
                assert bookOwner != null;
                bookOwner.setText(name);
                if (!member.hasValidImageUrl()) {
                    return;
                }
                final String imageUrl = member.getImageUrl();
                Log.d(TAG, "imageUrl: " + imageUrl);
                assert userIcon != null;
                imageLoader.displayImage(imageUrl, userIcon);
            }

            @Override
            public void failure(Exception e) {
                Log.e(TAG, "failure: ", e);
            }
        });
    }

    @Override
    public void onClick(View v) {
        LogUtil.d(TAG, "onClick");
        if (v != null) {
            switch (v.getId()) {
                case R.id.buttonPreRequest:
                    // クリック処理
                    clickToRequest();
                    break;

                case R.id.bookInfoLike:
                    clickToStared();
                    break;
            }
        }
    }


    private void clickToRequest() {
        LogUtil.d(TAG, "onClick");

        final KiiUser currentUser = KiiUser.getCurrentUser ();

        if (currentUser == null) {
            Log.d(TAG, "onClick: Current KiiUser is null!");
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            showToast("会員登録をお願いします！");
            return;
        }

        final String userId = currentUser.getID();
        LogUtil.d (TAG, "userId: " + userId);
        KiiMemberConnection.fetch(userId, new KiiObjectCallback<Member>() {
            @Override
            public void success(int token, Member member) {
                int current = member.getPoint() + member.getPointsByBooks();
                Log.d(TAG, "point:" + current);
                if(current < 1){
                    Toast.makeText(getApplicationContext(), "ブクが足りないのでリクエストできません", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Request request = Request.createNew(currentUser.getID(), book);
                    startActivity(RequestBookActivity.createIntent(BookInfoActivity.this, request));
                }
            }

            @Override
            public void failure(Exception e) {

            }
        });
    }

    private void clickToStared(){ //星のクリックしての処理
        final ImageView  = (ImageView) findViewById(R.id.bookInfoLike);//星の画像
        final ImageView userIcon = (ImageView) findViewById(R.id.user_icon);//星の画像
        KiiUser kiiUser = KiiUser.getCurrentUser();
        boolean request.
        if(like){

        }

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}