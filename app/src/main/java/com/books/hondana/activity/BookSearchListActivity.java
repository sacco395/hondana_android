package com.books.hondana.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.books.hondana.BookListAdapter;
import com.books.hondana.Connection.BookSearchConnection;
import com.books.hondana.Connection.GoogleBookSearch;
import com.books.hondana.Connection.QueryParamSet;
import com.books.hondana.Connection.RakutenBookSearch;
import com.books.hondana.Connection.SearchAPI;
import com.books.hondana.Model.KiiBook;
import com.books.hondana.R;

import java.util.ArrayList;

public class BookSearchListActivity extends AppCompatActivity
		implements DialogInterface.OnCancelListener {

	final static String TAG = BookMainActivity.class.getSimpleName();

	private ProgressDialog progressDialog = null;
	private QueryParamSet queryParamSet;
	private String isbn;

	private ArrayList<KiiBook> bookList=null;
	private ArrayList<KiiBook> rakuten_bookList=null;
	private ArrayList<KiiBook> google_bookList=null;

	private ListView mainListView;

	private BookSearchConnection rakutenSearchConnection;
	private BookSearchConnection googleSearchConnection;

	private SearchAPI searchAPI=SearchAPI.RAKUTEN;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_list);

		mainListView = (ListView) findViewById(R.id.listView1);
		mainListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				KiiBook selectedBook = bookList.get(arg2);
				//HashMap<String,String> bookInfo = selectedBook.getMap();
				Intent intent = new Intent();
				intent.putExtra( "Book", selectedBook );
				setResult(Activity.RESULT_OK,intent);
				finish();
			}
		});//end setOnItemClickListener

		///////////////////////////////////////////////////////////////
		bookList = new ArrayList<KiiBook>();
		rakuten_bookList = new ArrayList<KiiBook>();
		google_bookList = new ArrayList<KiiBook>();
		///////////////////////////////////////////////////////////////
		Intent intent = this.getIntent();
		queryParamSet = (QueryParamSet)intent.getExtras().get("SEARCH_PARAM");
		isbn = queryParamSet.getSearchValue(KiiBook.ISBN);

		///////////////////////////////////////////////////////////////
		//selectSearchAPI(this);
	    kickBookSearch(SearchAPI.RAKUTEN);
		//kickBookSearch(SearchAPI.GOOGLE);
	}

	private int doRakutenSearch(){

		int status=0;
		dispStartMessage(SearchAPI.RAKUTEN);
		QueryParamSet queryParamSet = new QueryParamSet();
		queryParamSet.addQueryParam(QueryParamSet.ISBN, isbn);
		rakutenSearchConnection = new RakutenBookSearch(this,queryParamSet);
		rakutenSearchConnection.searchBooks( 1, new BookSearchConnection.SearchFinishListener() {
			@Override
			public void didFinish(int err, ArrayList<KiiBook> resultList) {

				dispEndMessage(SearchAPI.RAKUTEN);

				if (resultList != null) {
					int count = resultList.size();
					// for Debug
					Toast.makeText(BookSearchListActivity.this, "見つかった件数:" + count, Toast.LENGTH_SHORT).show();
					if (count > 0) {
						rakuten_bookList = resultList;
						bookList = resultList;

						BookListAdapter adapter =
								new BookListAdapter(BookSearchListActivity.this, android.R.layout.simple_expandable_list_item_1, 0, bookList);
						mainListView.setAdapter(adapter);
					} else {
						dispSorryMessage();
					}
				} else {
					dispSorryMessage();
				}
				getLoaderManager().destroyLoader(RakutenBookSearch.LOADER_ID);
			}
		});

		return status;
	}

	private int doGoogleSearch() {
		int status=0;
		dispStartMessage(SearchAPI.GOOGLE);
		QueryParamSet queryParamSet = new QueryParamSet();
		queryParamSet.addQueryParam(QueryParamSet.ISBN, isbn);
		googleSearchConnection = new GoogleBookSearch(this,queryParamSet);
		googleSearchConnection.searchBooks( 1, new BookSearchConnection.SearchFinishListener() {
			@Override
			public void didFinish(int err, ArrayList<KiiBook> resultList) {

				dispEndMessage(SearchAPI.GOOGLE);

				if (resultList != null){
					int count = resultList.size();
					// for Debug
					Toast.makeText(BookSearchListActivity.this, "見つかった件数:" + count, Toast.LENGTH_SHORT).show();
					if (count > 0) {
						google_bookList =resultList;
						bookList = resultList;

						BookListAdapter adapter =
								new BookListAdapter(BookSearchListActivity.this, android.R.layout.simple_expandable_list_item_1, 0, bookList);
						mainListView.setAdapter(adapter);

					} else {
						dispSorryMessage();
					}
				} else {
					dispSorryMessage();
				}
				getLoaderManager().destroyLoader(GoogleBookSearch.LOADER_ID);
			}
		});
		return status;
	}

	// ラジオボタン付きアイテム選択ダイアログを表示する例 //
	private void selectSearchAPI(Context context){

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("検索に使用するAPI");

		// 表示アイテムを指定する //
		final String[] items = {"楽天", "Google"};
		builder.setSingleChoiceItems(items, 0, mItemListener);

		// 決定・キャンセル用にボタンも配置 //
		builder.setPositiveButton("OK", mButtonListener );
		//builder.setNeutralButton ("Cancel", mButtonListener );

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	// アイテムのリスナー //
	DialogInterface.OnClickListener mItemListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch(which) {
				case 0:
					searchAPI = SearchAPI.RAKUTEN;
					break;
				case 1:
					searchAPI = SearchAPI.GOOGLE;
					break;
			}
			//Toast.makeText(BookSearchListActivity.this, "Item " + which + " clicked.", Toast.LENGTH_SHORT).show();
		}
	};

	// ボタンのリスナー //
	DialogInterface.OnClickListener mButtonListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			String btnStr = "";
			switch( which ){
				case AlertDialog.BUTTON_POSITIVE:
					btnStr = "OK";
					kickBookSearch(searchAPI);
					break;
//				case AlertDialog.BUTTON_NEUTRAL:
//					btnStr = "Cancel";
//					break;
			}
			//Toast.makeText(BookSearchListActivity.this, btnStr + " button clicked.", Toast.LENGTH_SHORT).show();
		}
	};

	private void kickBookSearch(SearchAPI searchAPI){
		if (searchAPI == SearchAPI.RAKUTEN){
			doRakutenSearch();
		}
		else if (searchAPI == SearchAPI.GOOGLE){
			doGoogleSearch();
		}
	}

	private void dispStartMessage( SearchAPI searchAPI){
		String title="";

		if (searchAPI == SearchAPI.RAKUTEN){
			title = "検索中(楽)です\nしばらくお待ちください。";
		} else if(searchAPI == SearchAPI.GOOGLE) {
			title = "検索中(G)です\nしばらくお待ちください。";
		}

		//getLoaderManager().initLoader(BookSearchConnection.LOADER_ID, b, this );
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(title);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(true);
		progressDialog.setOnCancelListener(this);
		progressDialog.show();

	}
	private void dispEndMessage( SearchAPI searchAPI) {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	private void dispSorryMessage(){
		Toast.makeText(BookSearchListActivity.this, "残念ながら、見つかりませんでした。", Toast.LENGTH_LONG).show();
	}

	//	@Override
	public void onCancel(DialogInterface arg0) {
		// TODO 自動生成されたメソッド・スタブ
	}
}
//	public void onLoadFinished(Loader<JSONObject> arg0, JSONObject rootObject) {
//
//		int numOfData;
//
//		if (progressDialog != null) {
//			progressDialog.dismiss();
//		}
//
//		ParseJson parseJson = new ParseRakutenJson();
//		bookList = parseJson.getBookInfo(rootObject);
//
//		if (bookList == null) {
//			Toast.makeText(this, "残念ながら、見つかりませんでした。", Toast.LENGTH_LONG).show();
//			return;
//		}
//
//		numOfData = bookList.size();
//		Toast.makeText(this, "見つかった件数:" + numOfData, Toast.LENGTH_LONG).show();
//
//		if (numOfData <= 0) {
//			Toast.makeText(this, "残念ながら、見つかりませんでした。", Toast.LENGTH_LONG).show();
//			return;
//		}
//
//		BookListAdapter adapter =
//				new BookListAdapter(this, android.R.layout.simple_expandable_list_item_1, 0, bookList);
//		mainListView.setAdapter(adapter);
//
//		getLoaderManager().destroyLoader(0);
//	}


//	@Override
//	public void onCancel(DialogInterface arg0) {
//		// TODO 自動生成されたメソッド・スタブ
//	}
//
//	@Override
//	public void onLoaderReset(Loader<JSONObject> loader) {
//		// TODO 自動生成されたメソッド・スタブ
//	}
//
///////////////////////////////////////////////////////////////////
//Toast.makeText(this, "BeginBookListActivity",Toast.LENGTH_SHORT).show();
//
//	@Override
//	public Loader<JSONObject> onCreateLoader(int arg0, Bundle arg1) {
////		progressDialog = new ProgressDialog(this);
////		progressDialog.setTitle("しばらくお待ちください。");
////		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
////		progressDialog.setCancelable(true);
////		progressDialog.setOnCancelListener(this);
////		progressDialog.show();
//
//
//
//		//HttpAsyncLoader<JSONObject> loader  = new HttpAsyncLoader<JSONObject>(this, arg1.getString("url"));
//		HttpAsyncLoader loader  = new HttpAsyncLoader(this, arg1.getString("url"));
//		loader.forceLoad();
//		return loader;
//	}
//
//	@Override
//	public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {
//
//	}
