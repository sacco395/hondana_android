package com.books.hondana.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.books.hondana.MyBookList;
import com.books.hondana.MyBookListAdapter;
import com.books.hondana.R;
import com.books.hondana.connection.KiiMemberConnection;
import com.books.hondana.connection.KiiObjectCallback;
import com.books.hondana.connection.QueryParamSet;
import com.books.hondana.guide.GuideActivity;
import com.books.hondana.model.Book;
import com.books.hondana.model.BookInfo;
import com.books.hondana.model.Member;
import com.books.hondana.setting.SettingActivity;
import com.books.hondana.start.StartActivity;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiQueryCallBack;
import com.kii.cloud.storage.query.KiiClause;
import com.kii.cloud.storage.query.KiiQuery;
import com.kii.cloud.storage.query.KiiQueryResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserpageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "UserpageActivity";

    private MyBookListAdapter mAdapter;

    final ImageLoader imageLoader = ImageLoader.getInstance();

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
    private FloatingActionButton mFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        KiiUser user = KiiUser.getCurrentUser();
        toolbar.setTitle(user.getUsername ().toString() + "さん");
        setSupportActionBar(toolbar);


        //カメラボタン
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSimpleActivity(v);
            }
        });


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setProfileInMenu(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);

        toggle.syncState();


        TextView Username = (TextView)findViewById(R.id.user_name);
        Username.setText(user.getUsername ().toString());

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View header = navigationView.getHeaderView(0);
        final ImageView navUserIcon = (ImageView) header.findViewById(R.id.iv_user_icon);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d(TAG, "onClick: User click!");

                KiiUser kiiUser = KiiUser.getCurrentUser ();

                LogUtil.d (TAG, "kiiUser: " + kiiUser);

                if (kiiUser != null) {
                    Intent intent = new Intent(UserpageActivity.this,
                            UserpageActivity.class);
                    UserpageActivity.this.startActivity(intent);
                } else {
                    Intent intent = new Intent(UserpageActivity.this,
                            StartActivity.class);
                    UserpageActivity.this.startActivity(intent);
                    showToast("会員登録をお願いします！");
                }
            }

        });

        TextView userName = (TextView) header.findViewById(R.id.tv_user_name);
        userName.setText(user.getUsername ().toString());

        LinearLayout UserEdit = (LinearLayout)findViewById(R.id.user_edit);

        //評価機能がついたら復活
//        LinearLayout Evaluation = (LinearLayout)findViewById(R.id.evaluation);
//
        assert UserEdit != null;
        UserEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d(TAG, "onClick");
                Intent intent = new Intent(UserpageActivity.this, UserEditActivity.class);
                startActivity(intent);
            }
        });

////評価機能がついたら復活
//        Evaluation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LogUtil.d(TAG, "onClick");
//                Intent intent = new Intent(UserpageActivity.this, EvaluationActivity.class);
//                startActivity(intent);
//            }
//        });


        final ImageView userIcon = (ImageView) findViewById(R.id.user_icon);

        KiiUser kiiUser = KiiUser.getCurrentUser();
        assert kiiUser != null;
        String userId = kiiUser.getID();
        LogUtil.d (TAG, "userID = " + userId);
        KiiMemberConnection.fetch(userId, new KiiObjectCallback<Member>() {
                    @Override
                    public void success(int token, Member member) {
                        if (!member.hasValidImageUrl()) {
                            return;
                        }

                        final String imageUrl = member.getImageUrl();
                        Log.d(TAG, "imageUrl: " + imageUrl);
                        Picasso.with(UserpageActivity.this).load(imageUrl).into(navUserIcon);
                        Picasso.with(UserpageActivity.this).load(imageUrl).into(userIcon);
                    }

                    @Override
                    public void failure(Exception e) {
                        Log.w(TAG, e);
                    }
                });

//アダプターを作成します。newでクラスをインスタンス化しています。
        mAdapter = new MyBookListAdapter(this);

        //ListViewのViewを取得
        ListView listView = (ListView) findViewById(R.id.list_view);
        //GridViewにアダプターをセット。
        listView.setAdapter(mAdapter);

        //一覧のデータを作成して表示します。
        fetch();
    }

    //KiiCLoud対応のfetchです。
    //自分で作った関数です。一覧のデータを作成して表示します。
    KiiUser kiiUser = KiiUser.getCurrentUser();
    String userId = kiiUser.getID();

    private void fetch() {
        KiiQuery query = new KiiQuery(KiiClause.and(
                KiiClause.equals("owner_id", userId)));

        query.sortByDesc("_created");

        Kii.bucket("appbooks")
                .query(new KiiQueryCallBack<KiiObject>() {

                    @Override
                    public void onQueryCompleted(int token, KiiQueryResult<KiiObject> result, Exception exception) {
                        if (exception != null) {
                            return;
                        }
                        //空のMyBookListデータの配列を作成
                        ArrayList<MyBookList> MyBooks = new ArrayList<MyBookList>();
                        //検索結果をListで得る
                        List<KiiObject> objLists = result.getResult();
                        //得られたListをMyBookListに設定する
                        for (KiiObject obj : objLists) {
                            //_id(KiiCloudのキー)を得る。空の時は""が得られる。jsonで
                            String id = obj.getString("_id", "");
                            LogUtil.d(TAG,"id" + id);
//                            String url = obj.getString("image_url", "");
//                            LogUtil.d(TAG,"image_url" + url);
                            String title = obj.getString("title", "");
                            LogUtil.d(TAG,"title" + title);
                            String author = obj.getString("author", "");
                            LogUtil.d(TAG,"author" + author);


                            //MyBookListを新しく作ります。
                            MyBookList list = new MyBookList(id, title, author);
                            //MyBookListの配列に追加します。
                            MyBooks.add(list);
                        }
                        //データをアダプターにセットしています。これで表示されます。
                        mAdapter.setMyBookList(MyBooks);
                    }
                }, query);//
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_userpage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId ();
        if (id == R.id.nav_edit) {
            Intent intent = new Intent (this, UserEditActivity.class);
            startActivity (intent);
        }
        return super.onOptionsItemSelected (item);
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
        Intent intent = new Intent(UserpageActivity.this,BookSearchListActivity.class);
        QueryParamSet queryParamSet = new QueryParamSet();
        queryParamSet.addQueryParam(BookInfo.ISBN,this.search_Isbn);
        intent.putExtra( "SEARCH_PARAM", queryParamSet );
        startActivityForResult(intent, ACT_BOOK_SEARCH_LIST);
    }

    private void kickListSearchResult(Intent data){
        Bundle extras = data.getExtras();
        Book book = extras.getParcelable(Book.class.getSimpleName());
        Intent intent = new Intent(UserpageActivity.this,BookDetailActivity.class);
        intent.putExtra(Book.class.getSimpleName(), book);
        startActivityForResult(intent, ACT_BOOK_DETAIL_TO_ADD);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, BookMainActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_like) {
//            Intent intent = new Intent(this, LikesActivity.class);
//            startActivity(intent);
//一時的にコメントアウト

        } else if (id == R.id.nav_exchange) {
//            Intent intent = new Intent(this, SwapBookActivity.class);
//            startActivity(intent);
//一時的にコメントアウト

        } else if (id == R.id.nav_transaction) {
//            Intent intent = new Intent(this, RequestActivity.class);
//            startActivity(intent);
//一時的にコメントアウト

        } else if (id == R.id.nav_set) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_guide) {
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_mail) {
            Intent intent = new Intent(this, InquiryActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    void setProfileInMenu(View drawerView) {
//        tvUserName.setText(user.getName());
//        Picasso.with(this)
//                .load(user.getIconUrl())
//                .into(ivUserIcon);
        LinearLayout llUserContainer = (LinearLayout) drawerView.findViewById(R.id.ll_user_container);
        TextView tvUserName = (TextView) drawerView.findViewById(R.id.tv_user_name);
        ImageView ivUserIcon = (ImageView) drawerView.findViewById(R.id.iv_user_icon);

        llUserContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d(TAG, "onClick");
                Intent intent = new Intent(UserpageActivity.this, UserpageActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}

