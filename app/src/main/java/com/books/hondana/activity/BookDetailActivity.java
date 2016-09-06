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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.books.hondana.Model.KiiBook;
import com.books.hondana.R;
import com.books.hondana.util.DateUtil;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiObjectCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BookDetailActivity extends AppCompatActivity	{

	final static String TAG = BookDetailActivity.class.getSimpleName();

	private KiiBook targetBook;
	private Button btnAddKiiCloud;

	// define the UI elements
	private ProgressDialog mProgress=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_detail);

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
				// KiiBook bKobj = new KiiBook()
				KiiBook bKobj = targetBook;
				// 確認 by 奥山 2016/08/26
				if (targetBook.get(KiiBook.ISBN)==null || targetBook.get(KiiBook.ISBN).length()<=0 ){
					Toast.makeText(BookDetailActivity.this,"BookDetailActivity targetBook空だよ！",
							Toast.LENGTH_LONG).show();
				}
				Toast.makeText(BookDetailActivity.this,"ISBN:"+targetBook.get(KiiBook.ISBN),
						Toast.LENGTH_SHORT).show();
				//
				// Dummy Data
				bKobj.set(KiiBook.BOOK_ID, "2");  // 適当すぎる！
				bKobj.set(KiiBook.LANGUAGE, "日本"); // Dummy
				bKobj.set(KiiBook.IMAGE, "Now Printing"); // Dummy
				bKobj.set(KiiBook.HEIGHT, "0.0"); // Dummy
				bKobj.set(KiiBook.WIDTH, "0.0"); // Dummy
				bKobj.set(KiiBook.DEPTH, "0.0"); // Dummy
				bKobj.set(KiiBook.WEIGHT, "0.0"); // Dummy
				bKobj.set(KiiBook.USER_ID, "");

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