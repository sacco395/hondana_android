//本の詳細ページ
package com.books.hondana.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.books.hondana.R;
import com.books.hondana.connection.KiiLikeConnection;
import com.books.hondana.connection.KiiMemberConnection;
import com.books.hondana.connection.KiiObjectCallback;
import com.books.hondana.model.Book;
import com.books.hondana.model.BookCondition;
import com.books.hondana.model.BookInfo;
import com.books.hondana.model.Like;
import com.books.hondana.model.Member;
import com.books.hondana.model.Request;
import com.books.hondana.model.Size;
import com.books.hondana.model.Smell;
import com.books.hondana.model.abst.KiiModel;
import com.books.hondana.start.StartActivity;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiObjectCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.MessageFormat;


public class BookInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = BookInfoActivity.class.getSimpleName();

    private Book book;

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
        String bookId = book.getId ();

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

        TextView tv_bookBroken = (TextView) findViewById(R.id.bookInfoBroken);
        assert tv_bookBroken != null;
        tv_bookBroken.setText(condition.getBrokenText());

        TextView tv_bookNotes = (TextView) findViewById(R.id.bookInfoNotes);
        assert tv_bookNotes != null;
        tv_bookNotes.setText(condition.getNote());

        TextView tv_bookState = (TextView) findViewById(R.id.book_state);
        assert tv_bookState != null;
        String state_text = book.getStateText();
        LogUtil.d(TAG,"state_text:" + state_text);
        tv_bookState.setText(state_text);


//本のその他の状態
        // 空の文字列を作成
        String etcText = "";

        String cover = condition.getCoverText();
        if (!cover.equals("")) {
            cover += "／";
        }
        etcText += cover;

        // 日焼け情報を追加
        String band = condition.getBandText();
        // sunburned が空の文字列でなければ、読点を挿入（ここは趣味で）
        if (!band.equals("")) {
            band += "／";
        }
        etcText += band;

        String sticker = condition.getStickerText();
        if (!sticker.equals("")) {
            sticker += "／";
        }
        etcText += sticker;

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

        TextView tv_bigger_than_clickpost = (TextView)findViewById(R.id.bookInfoBiggerThanClickpost);
        String biggerThanClickpost = size.getBiggerThanClickpostText();
        if (!biggerThanClickpost.equals("")) {
            tv_bigger_than_clickpost.setText (biggerThanClickpost);
        }

        //本のサイズここまで

        findViewById(R.id.buttonPreRequest).setOnClickListener(this);
        findViewById(R.id.bookInfoLike).setOnClickListener(this);

        final TextView bookOwner = (TextView) findViewById(R.id.textViewBookInfoUserName);
        final ImageView userIcon = (ImageView) findViewById(R.id.bookInfoUserIcon);

        final String ownerName = book.getOwnerName();
        assert bookOwner != null;
        bookOwner.setText(ownerName);
        Log.d(TAG, "ownerName: " + ownerName);

        final String ownerId = book.getOwnerId();
        Log.d(TAG, "ownerId: " + ownerId);
        KiiMemberConnection.fetch(ownerId, new KiiObjectCallback<Member>() {
            @Override
            public void success(int token, Member member) {
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

        final ImageView star = (ImageView) findViewById(R.id.bookInfoLike);
        KiiUser kiiUser = KiiUser.getCurrentUser();
        if (kiiUser == null) {
            assert star != null;
            star.setBackgroundResource(R.drawable.star_off);
            return;
        }
        String userId = kiiUser.getID();
        KiiLikeConnection.fetchLikeByBookId(bookId, userId, new KiiObjectCallback<Like>() {
            @Override
            public void success(int token, Like like) {
                if (like == null) {
                    assert star != null;
                    star.setBackgroundResource(R.drawable.star_off);
                return;
                }
                assert star != null;
                star.setBackgroundResource(R.drawable.star_on);
                Log.d(TAG, "この本はお気に入り!");
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
                    LogUtil.d(TAG, "onClickStar");
                    clickToStarred();
                    }
            }
        }


    private void clickToRequest() {
        LogUtil.d(TAG, "onClick");

        final KiiUser currentUser = KiiUser.getCurrentUser ();
        int bookState = book.getState();
        LogUtil.d(TAG,"bookState:" + bookState);
        switch(bookState) {
            case 1:
                Toast.makeText(getApplicationContext(), "リクエスト受付済みなので\nリクエストできません", Toast.LENGTH_LONG).show();
            break;

            case 2:
            Toast.makeText(getApplicationContext(), "交換成立済みなので\nリクエストできません", Toast.LENGTH_LONG).show();
            break;

            case 0:
                if (currentUser == null) {
                    Log.d(TAG, "onClick: Current KiiUser is null!");
                    Intent intent = new Intent(this, StartActivity.class);
                    startActivity(intent);
                    showToast("会員登録をお願いします！");
                    return;
                }

                final String userId = currentUser.getID();
                LogUtil.d (TAG, "userId: " + userId);
                book = getIntent().getParcelableExtra(Book.class.getSimpleName());
                final BookInfo info = book.getInfo();
                final Size size = info.getSize();
                final String biggerThanClickpost = size.getBiggerThanClickpostText();
                KiiMemberConnection.fetch(userId, new KiiObjectCallback<Member>() {
                    @Override
                    public void success(int token, Member member) {
                        int current = member.getPoint() + member.getPointsByBooks();
                        Log.d(TAG, "point:" + current);
                        if(current < 1){
                            Toast.makeText(getApplicationContext(), "ブクが足りないのでリクエストできません", Toast.LENGTH_LONG).show();
                            finish();
                        }else if(!biggerThanClickpost.equals("")) {
                            Request request = Request.createNew(currentUser.getID(), book);
                            startActivity(RequestBookLargerActivity.createIntent(BookInfoActivity.this, request));
                        }else{
                            Request request = Request.createNew(currentUser.getID(), book);
                            startActivity(RequestBookActivity.createIntent(BookInfoActivity.this, request));
                        }
                    }

                    @Override
                    public void failure(Exception e) {
                        Log.e(TAG, "failure: ", e);
                    }
                });
        }
    }



    private void clickToStarred() {
        String bookId = book.getId();
        LogUtil.d(TAG, "bookId = " + bookId);
        KiiUser kiiUser = KiiUser.getCurrentUser();
        if (kiiUser == null) {
            Log.d (TAG, "onClick: Current KiiUser is null!");
            Intent intent = new Intent (this, StartActivity.class);
            startActivity (intent);
            showToast ("会員登録をお願いします！");
            return;
        }
        final String userId = kiiUser.getID();
        KiiLikeConnection.fetchLikeByBookId(bookId, userId, new KiiObjectCallback<Like>() {
            @Override
            public void success(int token, Like like) {
                if (like == null) {
                    like = Like.createNew(userId, book);
                    like.save(false, new KiiModel.KiiSaveCallback() {
                        @Override
                        public void success(int token, KiiObject object) {
                            LogUtil.d(TAG, "object: " + object.toString());
                            ImageView star = (ImageView) findViewById(R.id.bookInfoLike);
                            assert star != null;
                            star.setBackgroundResource(R.drawable.star_on);
                        }

                        @Override
                        public void failure(@Nullable Exception e) {
                            Log.e(TAG, "failure: ", e);
                        }
                    });
                    return;
                }
                String likeId = like.getId();
                LogUtil.d (TAG, "likeId = " + likeId);

                KiiObject likeObject = Kii.bucket(Like.BUCKET_NAME).object(likeId);
                likeObject.delete(new KiiObjectCallBack() {
                    @Override
                    public void onDeleteCompleted(int token, Exception exception) {
                        if (exception != null) {
                            Log.d(TAG, "削除できないよ!");
                            return;
                        }
                        Log.d(TAG, "削除完了!");
                        ImageView star = (ImageView) findViewById(R.id.bookInfoLike);
                        assert star != null;
                        star.setBackgroundResource(R.drawable.star_off);
                    }
                });
            }

            @Override
            public void failure(Exception e) {
                Log.e(TAG, "failure: ", e);

            }
        });
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}