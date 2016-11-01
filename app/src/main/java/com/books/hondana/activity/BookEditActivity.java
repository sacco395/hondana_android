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
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiObjectCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BookEditActivity extends AppCompatActivity implements View.OnClickListener {

	final static String TAG = BookEditActivity.class.getSimpleName ();

	private Book targetBook;

	private BookCondition condition;
	private Smell smell;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		setContentView (R.layout.activity_book_edit);

		targetBook = getIntent ().getParcelableExtra (Book.class.getSimpleName ());
		if (targetBook == null) {
			Log.e (TAG, "onCreate: Book が null!");
			Toast.makeText (BookEditActivity.this, "エラー: 本の情報が取得できませんでした。", Toast.LENGTH_SHORT).show ();
		}
		condition = targetBook.getCondition();
		smell = condition.getSmell();

		TextView tv_bookCondition = (TextView) findViewById(R.id.bookInfoCondition);
		assert tv_bookCondition != null;
		tv_bookCondition.setText(condition.getEvaluationText());

		TextView tv_bookLine = (TextView) findViewById(R.id.bookInfoLine);
		assert tv_bookLine != null;
		tv_bookLine.setText(condition.getLinedText());

		TextView tv_bookFolded = (TextView) findViewById(R.id.bookInfoFolded);
		assert tv_bookFolded != null;
		tv_bookFolded.setText(condition.getFoldedText());

		TextView tv_bookBroken = (TextView) findViewById(R.id.bookInfoBroken);
		assert tv_bookBroken != null;
		tv_bookBroken.setText(condition.getBrokenText());


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

		final Smell smell = condition.getSmell();

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

		final BookInfo info = targetBook.getInfo ();

		findViewById (R.id.btnDeleteKiiBook).setOnClickListener (this);

		CheckBox cbBiggerThanClickpost = (CheckBox) findViewById (R.id.chkBiggerThanClickpost);
		assert cbBiggerThanClickpost != null;
		cbBiggerThanClickpost.setChecked (false);
		cbBiggerThanClickpost.setOnClickListener (this);

		CheckBox cbBand = (CheckBox) findViewById (R.id.chkBand);
		assert cbBand != null;
		cbBand.setChecked (false);
		cbBand.setOnClickListener (this);

		CheckBox cbCover = (CheckBox) findViewById (R.id.chkCover);
		assert cbCover != null;
		cbCover.setChecked (false);
		cbCover.setOnClickListener (this);

		CheckBox cbSticker = (CheckBox) findViewById (R.id.chkSticker);
		assert cbSticker != null;
		cbSticker.setChecked (false);
		cbSticker.setOnClickListener (this);

		CheckBox cbSunburned = (CheckBox) findViewById (R.id.chkSunburned);
		assert cbSunburned != null;
		cbSunburned.setChecked (false);
		cbSunburned.setOnClickListener (this);

		CheckBox cbScratched = (CheckBox) findViewById (R.id.chkScratched);
		assert cbScratched != null;
		cbScratched.setChecked (false);
		cbScratched.setOnClickListener (this);

		CheckBox cbCigarSmell = (CheckBox) findViewById (R.id.chkCigarSmell);
		assert cbCigarSmell != null;
		cbCigarSmell.setChecked (false);
		cbCigarSmell.setOnClickListener (this);

		CheckBox cbPet = (CheckBox) findViewById (R.id.chkPetSmell);
		assert cbPet != null;
		cbPet.setChecked (false);
		cbPet.setOnClickListener (this);

		CheckBox cbMold = (CheckBox) findViewById (R.id.chkMoldSmell);
		assert cbMold != null;
		cbMold.setChecked (false);
		cbMold.setOnClickListener (this);

		final String imgUrl = info.getImageUrl ();

		if ((imgUrl != null) && (imgUrl.length () > 0)) {
			// 画像データのダウンロードと設定
			ImageLoader imageLoader = ImageLoader.getInstance ();
			ImageView imgView = (ImageView) findViewById (R.id.imgBookDetail);
			assert imgView != null;
			imageLoader.displayImage (imgUrl, imgView);
		}

		TextView tv_title = (TextView) findViewById (R.id.textView_title);
		assert tv_title != null;
		tv_title.setText (info.getTitle ());
		TextView tv_author = (TextView) findViewById (R.id.textView_author);
		assert tv_author != null;
		tv_author.setText (info.getAuthor ());
		TextView tv_isbn = (TextView) findViewById (R.id.textView_isbn);
		assert tv_isbn != null;
		tv_isbn.setText (info.getIsbn ());
		TextView tv_publisher = (TextView) findViewById (R.id.textView_publisher);
		assert tv_publisher != null;
		tv_publisher.setText (info.getPublisher ());
		TextView tv_issueDate = (TextView) findViewById (R.id.textView_issueDate);
		assert tv_issueDate != null;
		tv_issueDate.setText (info.getIssueDate ());


		((RadioGroup) findViewById (R.id.rCondition)).setOnCheckedChangeListener
				(new RadioGroup.OnCheckedChangeListener () {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == R.id.cond_excellent) {
							//１つめを選択
							condition.setEvaluation (BookCondition.EVALUATION_EXCELLENT);
						} else if (checkedId == R.id.cond_good) {
							//２つめを選択
							condition.setEvaluation (BookCondition.EVALUATION_GOOD);
						} else if (checkedId == R.id.cond_bad) {
							//３つめを選択
							condition.setEvaluation (BookCondition.EVALUATION_BAD);
						}
					}

				});


		((RadioGroup) findViewById (R.id.rLined)).setOnCheckedChangeListener
				(new RadioGroup.OnCheckedChangeListener () {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == R.id.rLine_no) {
							//１つめを選択
							condition.setLined (BookCondition.LINED_NONE);
						} else if (checkedId == R.id.rLine_5) {
							//２つめを選択
							condition.setLined (BookCondition.LINED_ZERO_TO_FIVE);
						} else if (checkedId == R.id.rLine_5_10) {
							//３つめを選択
							condition.setLined (BookCondition.LINED_FIVE_TO_TEN);
						} else if (checkedId == R.id.rLine_10over) {
							//３つめを選択
							condition.setLined (BookCondition.LINED_MORE_THAN_TEN);
						}
					}

				});


		((RadioGroup) findViewById (R.id.rFolded)).setOnCheckedChangeListener
				(new RadioGroup.OnCheckedChangeListener () {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == R.id.rFolded_no) {
							//１つめを選択
							condition.setFolded (BookCondition.FOLDED_NONE);
						} else if (checkedId == R.id.rFolded_5) {
							//２つめを選択
							condition.setFolded (BookCondition.FOLDED_ZERO_TO_FIVE);
						} else if (checkedId == R.id.rFolded_5_10) {
							//３つめを選択
							condition.setFolded (BookCondition.FOLDED_FIVE_TO_TEN);
						} else if (checkedId == R.id.rFolded_10over) {
							//３つめを選択
							condition.setFolded (BookCondition.FOLDED_MORE_THAN_TEN);
						}
					}

				});

		((RadioGroup) findViewById (R.id.rBroken)).setOnCheckedChangeListener
				(new RadioGroup.OnCheckedChangeListener () {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == R.id.rBroken_no) {
							//１つめを選択
							condition.setBroken (BookCondition.BROKEN_NONE);
						} else if (checkedId == R.id.rBroken_5) {
							//２つめを選択
							condition.setBroken (BookCondition.BROKEN_ZERO_TO_FIVE);
						} else if (checkedId == R.id.rBroken_5_10) {
							//３つめを選択
							condition.setBroken (BookCondition.BROKEN_FIVE_TO_TEN);
						} else if (checkedId == R.id.rBroken_10over) {
							//３つめを選択
							condition.setBroken (BookCondition.BROKEN_MORE_THAN_TEN);
						}
					}

				});


//		if (!DateUtil.isOneYearAfter (info.getIssueDate ())) {
//			Toast.makeText (BookEditActivity.this, "一年前以上に発行された書籍ではありません。",
//					Toast.LENGTH_LONG).show ();
//		}

		Button btnAddKiiCloud = (Button) findViewById (R.id.btnAddKiiBook);
		// ボタンにフォーカスを移動させる
		assert btnAddKiiCloud != null;
		btnAddKiiCloud.setFocusable (true);
		btnAddKiiCloud.setFocusableInTouchMode (true);
		btnAddKiiCloud.requestFocus ();

		btnAddKiiCloud.setOnClickListener (new View.OnClickListener () {
			@Override
			public void onClick(View v) {
//				if (!DateUtil.isOneYearAfter (info.getIssueDate ())) {
//					Toast.makeText (BookEditActivity.this, "一年以内に発行された書籍は登録できません。",
//							Toast.LENGTH_LONG).show ();
//				} else {
					//備考欄のテキストここから
					EditText noteField = (EditText) (findViewById (R.id.edtNote));
					assert noteField != null;
					String noteStr = noteField.getText ().toString ();
					// 入力された文字を取得して保存
					condition.setNote (noteStr);
					//備考欄のテキストここまで

					Size size = info.getSize ();

					//本のサイズここから
					EditText heightField = (EditText) (findViewById (R.id.Height));
					assert heightField != null;
					String heightStr = heightField.getText ().toString ();
					double height = getValidDouble (heightStr);
					size.setHeight (height);
					// 入力された文字を取得して保存


					EditText widthField = (EditText) (findViewById (R.id.Wide));
					assert widthField != null;
					String widthStr = widthField.getText ().toString ();
					double width = getValidDouble (widthStr);
					size.setWidth (width);

					EditText thicknessField = (EditText) (findViewById (R.id.Depth));
					assert thicknessField != null;
					String thicknessStr = thicknessField.getText ().toString ();
					double thickness = getValidDouble (thicknessStr);
					size.setThickness (thickness);

					EditText weightField = (EditText) (findViewById (R.id.Weight));
					assert weightField != null;
					String weightStr = weightField.getText ().toString ();
					double weight = getValidDouble (weightStr);
					size.setWeight (weight);
					//本のサイズここまで

//					KiiUser user = KiiUser.getCurrentUser ();
//					if (user == null || user.getID () == null) {
//						Log.e (TAG, "onClick: User ID が取得できません");
//						Toast.makeText (BookEditActivity.this, "エラー: ユーザ情報が取得できません", Toast.LENGTH_SHORT).show ();
//						return;
//					}
//
//					KiiUser kiiUser = KiiUser.getCurrentUser ();
//					assert kiiUser != null;
//					String userId = kiiUser.getID ();
//					LogUtil.d (TAG, "userID = " + userId);
//					String userName = kiiUser.getUsername ();
//					LogUtil.d (TAG, "userName = " + userName);
//
//
//					targetBook.setOwnerId (user.getID ());
//					targetBook.setOwnerName (user.getUsername ());

					info.setSize (size);
					targetBook.setInfo (info);
					condition.setSmell (smell);
					targetBook.setCondition (condition);
					// show a progress dialog to the user
					final ProgressDialog progress = ProgressDialog.show (BookEditActivity.this, "登録中", "しばらくお待ちください", true);
					targetBook.save (false, new KiiModel.KiiSaveCallback () {
						@Override
						public void success(int token, KiiObject object) {
							progress.dismiss ();
							Log.d (TAG, "success: ");
							Intent intent = new Intent ();
							setResult (Activity.RESULT_OK, intent);
							finish ();
						}

						@Override
						public void failure(@Nullable Exception e) {
							progress.dismiss ();
							Log.e (TAG, "failure: ", e);
							Toast.makeText (BookEditActivity.this, "本の登録に失敗しました。", Toast.LENGTH_SHORT).show ();
						}
					});


					String bookId = targetBook.getId ();
					KiiObject object = Kii.bucket("appBooks").object(bookId);
					object.refresh(new KiiObjectCallBack() {
						@Override
						public void onRefreshCompleted(int token, KiiObject object, Exception exception) {
							if (exception != null) {
								// Error handling
								return;
							}
							LogUtil.d(TAG,"本の情報はね" + object.toString ());
							String genre1 = object.getString("genre_1");
							LogUtil.d(TAG,"genre1:"+ genre1);
							object.set ("genre_1",genre1);
							String genre2 = object.getString("genre_2");
							LogUtil.d(TAG,"genre2:"+ genre2);
							object.set ("genre_2",genre2);
							String genre3 = object.getString("genre_3");
							LogUtil.d(TAG,"genre3:"+ genre3);
							object.set ("genre_3",genre3);
							String genre4 = object.getString("genre_4");
							LogUtil.d(TAG,"genre4:"+ genre4);
							object.set ("genre_4",genre4);
							String genre5 = object.getString("genre_5");
							LogUtil.d(TAG,"genre5:"+ genre5);
							object.set ("genre_5",genre5);

							object.save(new KiiObjectCallBack() {
								@Override
								public void onSaveCompleted(int token, KiiObject object, Exception exception) {
									if (exception != null) {
										// Error handling
										return;
									}LogUtil.d(TAG,"ジャンルだけ保存したよ");
								}
							});
						}
					});
				}
		});
	}

	private double getValidDouble(String s) {
		if (s == null || s.equals ("")) {
			return 0;
		}
		return Double.valueOf (s);
	}

	@Override
	public void onClick(View v) {
		LogUtil.d (TAG, "onClick");
		if (v != null) {
			switch (v.getId ()) {
				case R.id.btnDeleteKiiBook:
					// クリック処理
					LogUtil.d (TAG, "onClickDelete");
					deleteBook ();
					break;
			}
			if (!(v instanceof CheckBox)) {
				return;
			}
			CheckBox cb = (CheckBox) v;
			switch (cb.getId ()) {
				case R.id.chkBand:        //帯付きの場合
					condition.setHasBand (cb.isChecked ());
					break;
				case R.id.chkCover:        //表紙違いの場合
					condition.setDifferentCover (cb.isChecked ());
					break;
				case R.id.chkSticker:        //シール跡がある場合
					condition.setHasSticker (cb.isChecked ());
					break;
				case R.id.chkSunburned:    //日焼けの場合
					condition.setSunburned (cb.isChecked ());
					break;
				case R.id.chkScratched:        //スレ・傷の場合
					condition.setScratched (cb.isChecked ());
					break;
				case R.id.chkCigarSmell:    //たばこ臭の場合
					smell.setCigar (cb.isChecked ());
					break;
				case R.id.chkPetSmell:    //ペットを飼ってる場合
					smell.setPet (cb.isChecked ());
					break;
				case R.id.chkMoldSmell:    //カビ臭の場合
					smell.setMold (cb.isChecked ());
					break;
				case R.id.chkBiggerThanClickpost:
					//規格外サイズの場合
					final BookInfo info = targetBook.getInfo ();
					Size size = info.getSize ();
					size.setBiggerThanClickpost (cb.isChecked ());
					break;
			}
		}
	}
	private void deleteBook() {
		final String bookId = targetBook.getId();
		LogUtil.d (TAG, "bookId = " + bookId);
		KiiObject bookObject = Kii.bucket(Book.BUCKET_NAME).object(bookId);
		bookObject.delete(new KiiObjectCallBack () {
			@Override
			public void onDeleteCompleted(int token, Exception exception) {
				if (exception != null) {
					Log.d(TAG, "削除できないよ!");
					return;
				}
				Log.d(TAG, "削除完了!");
				finish ();
			}
		});
	}
}
