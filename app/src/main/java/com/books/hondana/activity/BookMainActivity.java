//
//
// Copyright 2012 Kii Corporation
// http://kii.com
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
//

package com.books.hondana.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.books.hondana.BookMainFragmentPagerAdapter;
import com.books.hondana.HondanaBooksFragment;
import com.books.hondana.R;
import com.books.hondana.Connection.QueryParamSet;
import com.books.hondana.Model.KiiBook;

public class BookMainActivity extends AppCompatActivity
implements HondanaBooksFragment.OnFragmentInteractionListener {

    //private static final String TAG = "BookMainActivity";
    final static String TAG = BookMainActivity.class.getSimpleName();

    // Intent Parameter
    private static final int ACT_READ_BARCODE = 1;
    private static final int ACT_BOOK_SEARCH_LIST = 2;
    private static final int ACT_BOOK_DETAIL_TO_ADD = 3;

    ////////////////////////////////////////
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;

    // Search Param
    private String search_Isbn;

    //    private String search_Keyword;
    private Button btnScan;
    private Button btnReturn;
    private FloatingActionButton mFab;

    // define the UI elements
//    private ProgressDialog mProgress;

    // define the list
//    private ListView mListView;
//    private GridView mGridView;

//    private HondanaBookAdapter mListAdapter;
//    private KiiCloudConnection kiiCloudConnection;
//    private QueryParamSet queryParamSet;


    private ViewPager viewPager;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_main_frag);

        //        // BarCodeScanner起動
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  launchSimpleActivity(v);
              }
        });

        // 戻るボタン
        btnReturn = (Button)findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();// 戻る
            }
        });

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter( new BookMainFragmentPagerAdapter( getSupportFragmentManager()));

    }

    // the user can add items from the options menu.
    // create that menu here - from the res/menu/menu.xml file
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //BookMainActivity.this.addItem(null);
        return true;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //  Barcode Scanner 起動関連
    public void launchSimpleActivity(View v) {
        launchActivity(SimpleScannerActivity.class);
    }

    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivityForResult(intent, ACT_READ_BARCODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACT_READ_BARCODE) {
            if (resultCode == RESULT_OK) {
                /////////////////////////////////
                kickBookSearch(data);
                //////////////////////////////////
            }
        }
        else if (requestCode == ACT_BOOK_SEARCH_LIST){
            if (resultCode == RESULT_OK) {
                /////////////////////////////////
                kickListSearchResult(data);
                /////////////////////////////////
            }
        }
        else if (requestCode == ACT_BOOK_DETAIL_TO_ADD){
            if(resultCode == RESULT_OK){
                // 再起動しなくてもいいかも？
                //kickLoadHondanaBooks();
                // No Action
            }
        }
    }

    private void kickBookSearch( Intent data ){

        Bundle extras = data.getExtras();
        String code = extras.getString("READ_CODE");
        String format = extras.getString("READ_FORMAT");
        Toast.makeText(this, "Contents = " + code +
                ", Format = " + format, Toast.LENGTH_SHORT).show();
        this.search_Isbn = code;
        // 書籍情報を検索
        Intent intent = new Intent(BookMainActivity.this,BookSearchListActivity.class);
        QueryParamSet queryParamSet = new QueryParamSet();
        queryParamSet.addQueryParam(KiiBook.ISBN,this.search_Isbn);
        intent.putExtra( "SEARCH_PARAM", queryParamSet );
        startActivityForResult(intent, ACT_BOOK_SEARCH_LIST);
    }

    private void kickListSearchResult(Intent data){
        Bundle extras = data.getExtras();
        //HashMap<String,String> bookInfo = (HashMap<String, String>) data.getSerializableExtra("Book");
        KiiBook kiiBook = (KiiBook)extras.get("Book");
        Intent intent = new Intent(BookMainActivity.this,BookDetailActivity.class);
        intent.putExtra("Book", kiiBook );
        startActivityForResult(intent, ACT_BOOK_DETAIL_TO_ADD);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        ;
    }
}
