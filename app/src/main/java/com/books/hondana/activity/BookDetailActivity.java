package com.books.hondana.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.books.hondana.Model.kii.KiiBook;
import com.books.hondana.R;
import com.books.hondana.util.DateUtil;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiObjectCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BookDetailActivity extends AppCompatActivity implements View.OnClickListener {

	final static String TAG = BookDetailActivity.class.getSimpleName();

	private KiiBook targetBook;
	private Button btnAddKiiCloud;

	//メンバー変数
	private CheckBox mCheckBoxBand;
	private CheckBox mCheckBoxSunburned;
	private CheckBox mCheckBoxScratched;
	private CheckBox mCheckBoxCigar;
	private CheckBox mCheckBoxPet;
	private CheckBox mCheckBoxMold;

	private String Note;
	private String Height;
	private String Wide;
	private String Depth;
	private String Weight;

	// define the UI elements
	private ProgressDialog mProgress=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_detail);


		//CheckBoxBandとcheckboxBandを結び付ける
		mCheckBoxBand = (CheckBox) findViewById(R.id.chkBand);
		//チェックボックスの状態を設定
		mCheckBoxBand.setChecked(false);
		//チェックボックスがクリックされた時に呼び出されるコールバックリスナーを登録
		mCheckBoxBand.setOnClickListener(this);

		//同様に、CheckBoxSunburned
		mCheckBoxSunburned = (CheckBox) findViewById(R.id.chkSunburned);
		mCheckBoxSunburned.setChecked(false);
		mCheckBoxSunburned.setOnClickListener(this);

		//同様に、CheckBoxScratched
		mCheckBoxScratched = (CheckBox) findViewById(R.id.chkScratched);
		mCheckBoxScratched.setChecked(false);
		mCheckBoxScratched.setOnClickListener(this);

		//同様に、CheckBoxCigar
		mCheckBoxCigar = (CheckBox) findViewById(R.id.chkCigarSmell);
		mCheckBoxCigar.setChecked(false);
		mCheckBoxCigar.setOnClickListener(this);

		//同様に、CheckBoxPet
		mCheckBoxPet = (CheckBox) findViewById(R.id.chkPetSmell);
		mCheckBoxPet.setChecked(false);
		mCheckBoxPet.setOnClickListener(this);

		//同様に、CheckBoxMold
		mCheckBoxMold = (CheckBox) findViewById(R.id.chkMoldSmell);
		mCheckBoxMold.setChecked(false);
		mCheckBoxMold.setOnClickListener(this);



		//targetBook = new KiiBook();
		//HashMap<String,String> bookInfo = (HashMap<String, String>) getIntent().getSerializableExtra("Book");
		//targetBook.setUpMap(bookInfo);

		targetBook = getIntent().getParcelableExtra("Book");
		String imgUrl = targetBook.get(KiiBook.IMAGE_URL);

		if( (imgUrl != null) && (imgUrl.length() > 0)){
			// 画像データのダウンロードと設定
			ImageLoader imageLoader = ImageLoader.getInstance();
			ImageView imgView = (ImageView)findViewById(R.id.imgBookDetail);
			imageLoader.displayImage(imgUrl,imgView);
		}

		TextView tv_title = (TextView) findViewById(R.id.textView_title);
		tv_title.setText(targetBook.get(KiiBook.TITLE));
		TextView tv_author = (TextView) findViewById(R.id.textView_author);
		tv_author.setText(targetBook.get(KiiBook.AUTHOR));
		TextView tv_isbn = (TextView)findViewById(R.id.textView_isbn);
		tv_isbn.setText(targetBook.get(KiiBook.ISBN));
		TextView tv_publisher = (TextView)findViewById(R.id.textView_publisher);
		tv_publisher.setText(targetBook.get(KiiBook.PUBLISHER));
		TextView tv_issueDate = (TextView)findViewById(R.id.textView_issueDate);
		tv_issueDate.setText(targetBook.get(KiiBook.ISSUE_DATE));

//本の状態ラジオボタンここから
		((RadioGroup)findViewById(R.id.rCondition)).setOnCheckedChangeListener
				(new RadioGroup.OnCheckedChangeListener () {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == R.id.cond_excellent){
					//１つめを選択
					targetBook.set ("condition","良い");
				}else if(checkedId == R.id.cond_good){
					//２つめを選択
					targetBook.set ("condition","普通");
				}else if(checkedId == R.id.cond_bad){
					//３つめを選択
					targetBook.set ("condition","汚れあり");
				}
			}

		});
//本の状態ラジオボタンここまで
//書き込み線ラジオボタンここから
		((RadioGroup)findViewById(R.id.rLined)).setOnCheckedChangeListener
				(new RadioGroup.OnCheckedChangeListener () {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if(checkedId == R.id.rLine_no){
							//１つめを選択
							targetBook.set ("line","なし");
						}else if(checkedId == R.id.rLine_5){
							//２つめを選択
							targetBook.set ("line","5P未満");
						}else if(checkedId == R.id.rLine_5_10){
							//３つめを選択
							targetBook.set ("line","5P以上10P未満");
						}else if(checkedId == R.id.rLine_10over){
							//３つめを選択
							targetBook.set ("line","10P以上");
						}
					}

				});
//書き込み線ラジオボタンここまで
//ページの折れラジオボタンここから
		((RadioGroup)findViewById(R.id.rBroken)).setOnCheckedChangeListener
				(new RadioGroup.OnCheckedChangeListener () {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if(checkedId == R.id.rBroken_no){
							//１つめを選択
							targetBook.set ("broken","なし");
						}else if(checkedId == R.id.rBroken_5){
							//２つめを選択
							targetBook.set ("broken","5P未満");
						}else if(checkedId == R.id.rBroken_5_10){
							//３つめを選択
							targetBook.set ("broken","5P以上10P未満");
						}else if(checkedId == R.id.rBroken_10over){
							//３つめを選択
							targetBook.set ("broken","10P以上");
						}
					}

				});
//ページの折れラジオボタンここまで



		// 2016/09/06 Okuyama Upadate
		if ( !DateUtil.isOneYearAfter(targetBook.get(KiiBook.ISSUE_DATE)) ) {
			Toast.makeText(BookDetailActivity.this,"一年前以上に発行された書籍ではありません。",
					Toast.LENGTH_LONG).show();
		}
		//////////////////////////////////////////////

		btnAddKiiCloud = (Button)findViewById(R.id.btnAddKiiBook);
		// ボタンにフォーカスを移動させる
		btnAddKiiCloud.setFocusable(true);
		btnAddKiiCloud.setFocusableInTouchMode(true);
		btnAddKiiCloud.requestFocus();

		btnAddKiiCloud.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
		//備考欄のテキストここから
				EditText noteField = (EditText) (findViewById (R.id.edtNote));
				assert noteField != null;
				Note = noteField.getText ().toString ();
				// 入力された文字を取得して保存
				targetBook.set ("notes",Note);
		//備考欄のテキストここまで

		//本のサイズここから
				EditText HeightField = (EditText) (findViewById (R.id.Height));
				Height = HeightField.getText ().toString ();
		// 入力された文字を取得して保存
				targetBook.set ("size_height",Height);

				EditText WideField = (EditText) (findViewById (R.id.Wide));
				Wide = WideField.getText ().toString ();
				// 入力された文字を取得して保存
				targetBook.set ("size_wide",Wide);

				EditText DepthField = (EditText) (findViewById (R.id.Depth));
				Depth = DepthField.getText ().toString ();
				// 入力された文字を取得して保存
				targetBook.set ("size_depth",Depth);

				EditText WeightField = (EditText) (findViewById (R.id.Weight));
				Weight = WeightField.getText ().toString ();
				// 入力された文字を取得して保存
				targetBook.set ("size_weight",Weight);
		//本のサイズここまで

				// KiiBook bKobj = new KiiBook()
				KiiBook bKobj = targetBook;
				// 確認 by 奥山 2016/08/26
				if (targetBook.get(KiiBook.ISBN)==null || targetBook.get(KiiBook.ISBN).length()<=0 ){
					Toast.makeText(BookDetailActivity.this,"BookDetailActivity targetBook空だよ！",
							Toast.LENGTH_LONG).show();
				}
				Toast.makeText(BookDetailActivity.this,"ISBN:"+targetBook.get(KiiBook.ISBN),
						Toast.LENGTH_SHORT).show();

				KiiUser kiiUser = KiiUser.getCurrentUser ();
				targetBook.set ("user_id",kiiUser.getID());

				// show a progress dialog to the user
				mProgress = ProgressDialog.show( BookDetailActivity.this, "登録中", "しばらくお待ちください",	true);

				bKobj.save(	new KiiObjectCallBack() {
					@Override
					public void onSaveCompleted(int token, KiiObject object, Exception exception) {
						// hide our progress UI element
						mProgress.dismiss();

						// go to the next screen
						Intent intent = new Intent();
						setResult(Activity.RESULT_OK,intent);
						finish();

						if (exception != null) {
							// Error handling
							LogUtil.w("BookDetailActivity", exception);
							return;
						}
					}
				});
			}
		});
	}
	//本のその他の状態ここから
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.chkBand:		//帯付きの場合
				if(mCheckBoxBand.isChecked() == true){	// チェックされている場合
					targetBook.set ("band", "帯あり");
				}
				break;
			case R.id.chkSunburned:	//日焼けの場合
				if(mCheckBoxSunburned.isChecked() == true){	// チェックされている場合
					targetBook.set ("sunburned", "日焼け・変色");
				}
				break;
			case R.id.chkScratched:		//スレ・傷の場合
				if(mCheckBoxScratched.isChecked() == true){	// チェックされている場合
					targetBook.set ("scratched", "スレ・傷など");
				}
				break;
			case R.id.chkCigarSmell:	//たばこ臭の場合
				if(mCheckBoxCigar.isChecked() == true){	// チェックされている場合
					targetBook.set ("cigar_smell", "たばこ臭");
				}
				break;
			case R.id.chkPetSmell:	//ペットを飼ってる場合
				if(mCheckBoxPet.isChecked() == true){	// チェックされている場合
					targetBook.set ("pet_smell", "ペットを飼っている");
				}
				break;
			case R.id.chkMoldSmell:	//カビ臭の場合
				if(mCheckBoxMold.isChecked() == true){	// チェックされている場合
					targetBook.set ("mold_smell", "カビ臭");
				}
				break;

			default:
				break;
		}
	}
//本のその他の状態ここまで

//	@Override
//	public Loader<Bitmap> onCreateLoader(int arg0, Bundle arg1) {
//		ImageAsyncLoader loader  = new ImageAsyncLoader(this, targetBook.get(KiiBook.IMAGE_URL));
//		loader.forceLoad();
//		return loader;
//	}
//
//	@Override
//	public void onLoadFinished(Loader<Bitmap> arg0, Bitmap arg1) {
//		ImageView iv = (ImageView)findViewById(R.id.imgBookDetail);
//		iv.setImageBitmap(arg1);
//
//		getLoaderManager().destroyLoader(0);
//	}
//
//	@Override
//	public void onLoaderReset(Loader<Bitmap> arg0) {
//		// TODO 自動生成されたメソッド・スタブ
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.book_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

//	@Override
//	public void onCancel(DialogInterface arg0) {
//		// TODO 自動生成されたメソッド・スタブ
//
//	}
}