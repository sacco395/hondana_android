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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.books.hondana.BookRequestListAdapter;
import com.books.hondana.Connection.KiiMemberConnection;
import com.books.hondana.Connection.KiiObjectCallback;
import com.books.hondana.Connection.QueryParamSet;
import com.books.hondana.Model.Book;
import com.books.hondana.Model.BookInfo;
import com.books.hondana.Model.Member;
import com.books.hondana.MyBookList;
import com.books.hondana.MyBookListAdapter;
import com.books.hondana.R;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiQueryCallBack;
import com.kii.cloud.storage.query.KiiClause;
import com.kii.cloud.storage.query.KiiQuery;
import com.kii.cloud.storage.query.KiiQueryResult;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class UserpageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "UserpageActivity";

    private BookRequestListAdapter mAdapter;
    private MyBookListAdapter mAdapter2;

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

        //         this, binding.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // binding.drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        // ログインしてる名前を表示する
        TextView Username = (TextView)findViewById(R.id.user_name);
        Username.setText(user.getUsername ().toString());
        //

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //navigationViewにアイコンここから
        View header = navigationView.getHeaderView(0);
        final ImageView userIcon = (ImageView) header.findViewById(R.id.iv_user_icon);
//        Picasso.with(this).load("http://www.flamme.co.jp/common/profile/kasumi_arimura.jpg").into(userIcon);
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
        //navigationViewにアイコンここまで

        LinearLayout UserEdit = (LinearLayout)findViewById(R.id.user_edit);

        //評価機能がついたら復活
//        LinearLayout Evaluation = (LinearLayout)findViewById(R.id.evaluation);
//
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

//        ImageView userIcon2 = (ImageView)findViewById(R.id.user_icon);
//        Picasso.with(this).load("http://www.flamme.co.jp/common/profile/kasumi_arimura.jpg").into(userIcon2);
//        // binding.navView.setNavigationItemSelectedListener(this);

        final ImageView userIcon2 = (ImageView) findViewById(R.id.user_icon);
        final TextView tv_userProfile = (TextView) findViewById(R.id.tv_user_profile);

        final String userId = user.getID();
        KiiMemberConnection.fetch(userId, new KiiObjectCallback<Member>() {
            @Override
            public void success(int token, Member member) {
                final String userProfile = member.getProfile();
                tv_userProfile.setText(userProfile);
                if (!member.hasValidImageUrl()) {
                    return;
                }
                final String imageUrl = member.getImageUrl();
                LogUtil.d(TAG, "imageUrl: " + imageUrl);
                imageLoader.displayImage(imageUrl, userIcon);
                imageLoader.displayImage(imageUrl, userIcon2);
//ここからポイント表示
                int user_point = member.getPoint();
                String point = Integer.toString(user_point);
                TextView tv_userPoint = (TextView) findViewById(R.id.user_point);
                tv_userPoint.setText(point);
//ここまでポイント表示
            }

            @Override
            public void failure(Exception e) {
                LogUtil.w(TAG, e);
            }
        });

//アダプターを作成します。newでクラスをインスタンス化しています。
        mAdapter = new BookRequestListAdapter(this);
        mAdapter2 = new MyBookListAdapter(this);

        //ListViewのViewを取得
        ListView listView = (ListView) findViewById(R.id.list_view);
        //GridViewにアダプターをセット。
        listView.setAdapter(mAdapter);

        //一覧のデータを作成して表示します。
        fetch();

        //ListViewのViewを取得
        ListView listView2 = (ListView) findViewById(R.id.list_view02);
        //GridViewにアダプターをセット。
        listView2.setAdapter(mAdapter2);

        //一覧のデータを作成して表示します。
        fetch02();
    }

    //KiiCLoud対応のfetchです。
    //自分で作った関数です。一覧のデータを作成して表示します。
    KiiUser kiiUser = KiiUser.getCurrentUser();
    String userId = kiiUser.getID();

    private void fetch() {
        //KiiCloudの検索条件を作成。検索条件は未設定。なので全件。
        KiiQuery query = new KiiQuery(KiiClause.and(
                KiiClause.equals("request_userId", userId)));//request_userId(自分が本に申請した場合)
//                KiiClause.notEquals("send_date", "")));//送った日付が存在する場合)
        // Define query conditions

        //ソート条件を設定。日付の降順
        query.sortByDesc("_created");
        //バケットappbooksを検索する。最大200件
        Kii.bucket("appbooks")
                .query(new KiiQueryCallBack<KiiObject>() {
                    //検索が完了した時
                    @Override
                    public void onQueryCompleted(int token, KiiQueryResult<KiiObject> result, Exception exception) {
                        if (exception != null) {
                            //エラー処理を書く
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
                            String url = obj.getString("image_url", "");
                            String title = obj.getString("title", "");
                            String author = obj.getString("author", "");


                            //MyBookListを新しく作ります。
                            MyBookList list = new MyBookList(id, url, title, author);
                            //MyBookListの配列に追加します。
                            MyBooks.add(list);
                        }
                        //データをアダプターにセットしています。これで表示されます。
                        mAdapter.setMyBookList(MyBooks);
                    }
                }, query);//
    }

    private void fetch02() {
        //KiiCloudの検索条件を作成。検索条件は未設定。なので全件。
        KiiQuery query = new KiiQuery(KiiClause.and(
                KiiClause.equals("user_id", userId),//request_userId(自分が本に申請した場合)
                KiiClause.notEquals("request_date", "")));//送った日付が存在する場合)
        // Define query conditions

        //ソート条件を設定。日付の降順
        query.sortByDesc("_created");
        //バケットappbooksを検索する。最大200件
        Kii.bucket("appbooks")
                .query(new KiiQueryCallBack<KiiObject>() {
                    //検索が完了した時
                    @Override
                    public void onQueryCompleted(int token, KiiQueryResult<KiiObject> result, Exception exception) {
                        if (exception != null) {
                            //エラー処理を書く
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
                            String url = obj.getString("image_url", "");
                            String title = obj.getString("title", "");
                            String author = obj.getString("author", "");


                            //MyBookListを新しく作ります。
                            MyBookList list = new MyBookList(id, url, title, author);
                            //MyBookListの配列に追加します。
                            MyBooks.add(list);
                        }
                        //データをアダプターにセットしています。これで表示されます。
                        mAdapter2.setMyBookList(MyBooks);
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

