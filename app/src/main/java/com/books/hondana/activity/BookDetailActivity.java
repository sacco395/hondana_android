package com.books.hondana.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.books.hondana.R;
import com.books.hondana.model.Book;
import com.books.hondana.model.BookCondition;
import com.books.hondana.model.BookInfo;
import com.books.hondana.model.Size;
import com.books.hondana.model.Smell;
import com.books.hondana.model.abst.KiiModel;
import com.books.hondana.util.DateUtil;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BookDetailActivity extends AppCompatActivity implements View.OnClickListener {

	final static String TAG = BookDetailActivity.class.getSimpleName();

	private Book targetBook;

	private BookCondition condition;
	private Smell smell;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_detail);

		targetBook = getIntent().getParcelableExtra(Book.class.getSimpleName());
		if (targetBook == null) {
			Log.e(TAG, "onCreate: Book が null!");
			Toast.makeText(BookDetailActivity.this, "エラー: 本の情報が取得できませんでした。", Toast.LENGTH_SHORT).show();
		}
		condition = targetBook.getCondition();
		smell = condition.getSmell();

		CheckBox cbBand = (CheckBox) findViewById(R.id.chkBand);
		assert cbBand != null;
		cbBand.setChecked(false);
		cbBand.setOnClickListener(this);

		CheckBox cbSunburned = (CheckBox) findViewById(R.id.chkSunburned);
		assert cbSunburned != null;
		cbSunburned.setChecked(false);
		cbSunburned.setOnClickListener(this);

		CheckBox cbScratched = (CheckBox) findViewById(R.id.chkScratched);
		assert cbScratched != null;
		cbScratched.setChecked(false);
		cbScratched.setOnClickListener(this);

		CheckBox cbCigarSmell = (CheckBox) findViewById(R.id.chkCigarSmell);
		assert cbCigarSmell != null;
		cbCigarSmell.setChecked(false);
		cbCigarSmell.setOnClickListener(this);

		CheckBox cbPet = (CheckBox) findViewById(R.id.chkPetSmell);
		assert cbPet != null;
		cbPet.setChecked(false);
		cbPet.setOnClickListener(this);

		CheckBox cbMold = (CheckBox) findViewById(R.id.chkMoldSmell);
		assert cbMold != null;
		cbMold.setChecked(false);
		cbMold.setOnClickListener(this);

		final BookInfo info = targetBook.getInfo();
		String imgUrl = info.getImageUrl();

		if( (imgUrl != null) && (imgUrl.length() > 0)){
			// 画像データのダウンロードと設定
			ImageLoader imageLoader = ImageLoader.getInstance();
			ImageView imgView = (ImageView)findViewById(R.id.imgBookDetail);
			assert imgView != null;
			imageLoader.displayImage(imgUrl,imgView);
		}

		TextView tv_title = (TextView) findViewById(R.id.textView_title);
		assert tv_title != null;
		tv_title.setText(info.getTitle());
		TextView tv_author = (TextView) findViewById(R.id.textView_author);
		assert tv_author != null;
		tv_author.setText(info.getAuthor());
		TextView tv_isbn = (TextView)findViewById(R.id.textView_isbn);
		assert tv_isbn != null;
		tv_isbn.setText(info.getIsbn());
		TextView tv_publisher = (TextView)findViewById(R.id.textView_publisher);
		assert tv_publisher != null;
		tv_publisher.setText(info.getPublisher());
		TextView tv_issueDate = (TextView)findViewById(R.id.textView_issueDate);
		assert tv_issueDate != null;
		tv_issueDate.setText(info.getIssueDate());

		final BookCondition condition = new BookCondition();

		((RadioGroup)findViewById(R.id.rCondition)).setOnCheckedChangeListener
				(new RadioGroup.OnCheckedChangeListener () {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if(checkedId == R.id.cond_excellent){
							//１つめを選択
							condition.setEvaluation(BookCondition.EVALUATION_EXCELLENT);
						}else if(checkedId == R.id.cond_good){
							//２つめを選択
							condition.setEvaluation(BookCondition.EVALUATION_GOOD);
						}else if(checkedId == R.id.cond_bad){
							//３つめを選択
							condition.setEvaluation(BookCondition.EVALUATION_BAD);
						}
					}

				});


		((RadioGroup)findViewById(R.id.rLined)).setOnCheckedChangeListener
				(new RadioGroup.OnCheckedChangeListener () {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if(checkedId == R.id.rLine_no){
							//１つめを選択
							condition.setLined(BookCondition.LINED_NONE);
						}else if(checkedId == R.id.rLine_5){
							//２つめを選択
							condition.setLined(BookCondition.LINED_ZERO_TO_FIVE);
						}else if(checkedId == R.id.rLine_5_10){
							//３つめを選択
							condition.setLined(BookCondition.LINED_FIVE_TO_TEN);
						}else if(checkedId == R.id.rLine_10over){
							//３つめを選択
							condition.setLined(BookCondition.LINED_MORE_THAN_TEN);
						}
					}

				});


		((RadioGroup)findViewById(R.id.rFolded)).setOnCheckedChangeListener
				(new RadioGroup.OnCheckedChangeListener () {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if(checkedId == R.id.rFolded_no){
							//１つめを選択
							condition.setFolded(BookCondition.FOLDED_NONE);
						}else if(checkedId == R.id.rFolded_5){
							//２つめを選択
							condition.setFolded(BookCondition.FOLDED_ZERO_TO_FIVE);
						}else if(checkedId == R.id.rFolded_5_10){
							//３つめを選択
							condition.setFolded(BookCondition.FOLDED_FIVE_TO_TEN);
						}else if(checkedId == R.id.rFolded_10over){
							//３つめを選択
							condition.setFolded(BookCondition.FOLDED_MORE_THAN_TEN);
						}
					}

				});


		if (!DateUtil.isOneYearAfter(info.getIssueDate())) {
			Toast.makeText(BookDetailActivity.this,"一年前以上に発行された書籍ではありません。",
					Toast.LENGTH_LONG).show();
		}

		Button btnAddKiiCloud = (Button) findViewById(R.id.btnAddKiiBook);
		// ボタンにフォーカスを移動させる
		assert btnAddKiiCloud != null;
		btnAddKiiCloud.setFocusable(true);
		btnAddKiiCloud.setFocusableInTouchMode(true);
		btnAddKiiCloud.requestFocus();

		btnAddKiiCloud.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//備考欄のテキストここから
				EditText noteField = (EditText) (findViewById (R.id.edtNote));
				assert noteField != null;
				String noteStr = noteField.getText().toString ();
				// 入力された文字を取得して保存
				condition.setNote(noteStr);
				//備考欄のテキストここまで

				Size size = info.getSize();

				//本のサイズここから
				EditText heightField = (EditText) (findViewById(R.id.Height));
				assert heightField != null;
				String heightStr = heightField.getText().toString();
				double height = getValidDouble(heightStr);
				size.setHeight(height);
				// 入力された文字を取得して保存


				EditText widthField = (EditText) (findViewById(R.id.Wide));
				assert widthField != null;
				String widthStr = widthField.getText().toString();
				double width = getValidDouble(widthStr);
				size.setWidth(width);

				EditText thicknessField = (EditText) (findViewById(R.id.Depth));
				assert thicknessField != null;
				String thicknessStr = thicknessField.getText().toString();
				double thickness = getValidDouble(thicknessStr);
				size.setThickness(thickness);

				EditText weightField = (EditText) (findViewById(R.id.Weight));
				assert weightField != null;
				String weightStr = weightField.getText().toString();
				double weight = getValidDouble(weightStr);
				size.setWeight(weight);
				//本のサイズここまで

				KiiUser user = KiiUser.getCurrentUser();
				if (user == null || user.getID() == null) {
					Log.e(TAG, "onClick: User ID が取得できません");
					Toast.makeText(BookDetailActivity.this, "エラー: ユーザ情報が取得できません", Toast.LENGTH_SHORT).show();
					return;
				}

				KiiUser kiiUser = KiiUser.getCurrentUser();
				assert kiiUser != null;
				String userId = kiiUser.getID();
				LogUtil.d (TAG, "userID = " + userId);

				targetBook.setOwnerId(user.getID());
				info.setSize(size);
				targetBook.setInfo(info);
				condition.setSmell(smell);
				targetBook.setCondition(condition);


				// show a progress dialog to the user
				final ProgressDialog progress = ProgressDialog.show(BookDetailActivity.this, "登録中", "しばらくお待ちください", true);
				targetBook.save(false, new KiiModel.KiiSaveCallback() {
					@Override
					public void success(int token, KiiObject object) {
						progress.dismiss();
						Log.d(TAG, "success: ");
						Intent intent = new Intent();
						setResult(Activity.RESULT_OK, intent);
						finish();
					}

					@Override
					public void failure(@Nullable Exception e) {
						progress.dismiss();
						Log.e(TAG, "failure: ", e);
						Toast.makeText(BookDetailActivity.this, "本の登録に失敗しました。", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	private double getValidDouble(String s) {
		if (s == null || s.equals("")) {
			return 0;
		}
		return Double.valueOf(s);
	}

	@Override
	public void onClick(View v) {
		if (!(v instanceof CheckBox)) {
			return;
		}
		CheckBox cb = (CheckBox) v;
		switch (cb.getId()) {
			case R.id.chkBand:		//帯付きの場合
				condition.setHasBand(cb.isChecked());
				break;
			case R.id.chkSunburned:	//日焼けの場合
				condition.setSunburned(cb.isChecked());
				break;
			case R.id.chkScratched:		//スレ・傷の場合
				condition.setScratched(cb.isChecked());
				break;
			case R.id.chkCigarSmell:	//たばこ臭の場合
				smell.setCigar(cb.isChecked());
				break;
			case R.id.chkPetSmell:	//ペットを飼ってる場合
				smell.setPet(cb.isChecked());
				break;
			case R.id.chkMoldSmell:	//カビ臭の場合
				smell.setMold(cb.isChecked());
				break;
		}
	}
}