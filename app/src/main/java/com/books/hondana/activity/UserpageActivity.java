package com.books.hondana.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.books.hondana.MyBookListAdapter;
import com.books.hondana.R;
import com.books.hondana.connection.KiiBookConnection;
import com.books.hondana.connection.KiiMemberConnection;
import com.books.hondana.connection.KiiObjectCallback;
import com.books.hondana.connection.KiiObjectListCallback;
import com.books.hondana.connection.KiiRequestConnection;
import com.books.hondana.connection.QueryParamSet;
import com.books.hondana.evaluation.EvaluationActivity;
import com.books.hondana.arrived.HadArrivedBookActivity;
import com.books.hondana.exhibited.ExhibitedBookActivity;
import com.books.hondana.guide.GuideActivity;
import com.books.hondana.like.LikesActivity;
import com.books.hondana.model.Book;
import com.books.hondana.model.BookInfo;
import com.books.hondana.model.Member;
import com.books.hondana.model.Request;
import com.books.hondana.model.abst.KiiModel;
import com.books.hondana.setting.SettingActivity;
import com.books.hondana.start.StartActivity;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserpageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "UserpageActivity";

    MyBookListAdapter mListAdapter;

    // Intent Parameter
    private static final int ACT_READ_BARCODE = 1;
    private static final int ACT_BOOK_SEARCH_LIST = 2;
    private static final int ACT_BOOK_DETAIL_TO_ADD = 3;
    private static final int USER_EDIT = 4;

    ////////////////////////////////////////
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;
    private Member member;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final KiiUser user = KiiUser.getCurrentUser();
        assert toolbar != null;
        assert user != null;
        toolbar.setTitle(user.getUsername() + "さん");
        setSupportActionBar(toolbar);

        mListAdapter = new MyBookListAdapter(new ArrayList<Book>(), new MyBookListAdapter.BookItemClickListener() {
            @Override
            public void onClick(Book book) {
                Intent intent = new Intent(getApplicationContext(), BookEditActivity.class);
                intent.putExtra(Book.class.getSimpleName(), book);

                LogUtil.d(TAG, "onItemClick: " + book);
                startActivity (intent);
            }
        });


        ListView mListView = (ListView) findViewById(R.id.list_view);
        assert mListView != null;
        mListView.setAdapter(mListAdapter);

        //カメラボタン
        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fab);
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
        assert drawer != null;
        drawer.setDrawerListener(toggle);

        toggle.syncState();


        TextView Username = (TextView)findViewById(R.id.user_name);
        assert Username != null;
        Username.setText(user.getUsername());

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
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
        userName.setText(user.getUsername());

        LinearLayout UserEdit = (LinearLayout)findViewById(R.id.user_edit);


        LinearLayout Evaluation = (LinearLayout)findViewById(R.id.evaluation);

        assert UserEdit != null;
        UserEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d(TAG, "onClick");
                Intent intent = new Intent (UserpageActivity.this, UserEditActivity.class);
                startActivityForResult(intent,USER_EDIT);
            }
        });


        Evaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d(TAG, "onClick");
                Intent intent = new Intent(UserpageActivity.this, EvaluationActivity.class);
                startActivity(intent);
            }
        });


        final ImageView userIcon = (ImageView) findViewById(R.id.user_icon);
        final TextView userProfile = (TextView) findViewById(R.id.tv_user_profile);
        final TextView userPoint = (TextView) findViewById(R.id.user_point);
        final TextView numberOfBooks = (TextView) findViewById(R.id.numberOfBooks);
        final TextView numberOfExcellent = (TextView) findViewById(R.id.numberOfExcellent);
        final TextView numberOfGood = (TextView) findViewById(R.id.numberOfGood);
        final TextView numberOfBad = (TextView) findViewById(R.id.numberOfBad);

        KiiUser kiiUser = KiiUser.getCurrentUser();
        assert kiiUser != null;
        final String userId = kiiUser.getID();
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

                        final String UserProfile = member.getProfile();
                        Log.d(TAG, "profile: " + UserProfile);
                        assert userProfile != null;
                        userProfile.setText(UserProfile);

                        final int UserPoint = member.getPoint() + member.getPointsByBooks();
                        String point = Integer.toString(UserPoint);
                        Log.d(TAG, "point: " + point);
                        assert userPoint != null;
                        userPoint.setText(point);
                    }

                    @Override

                    public void failure(Exception e) {
                        Log.w(TAG, e);
                    }
                });

        KiiRequestConnection.fetchEvaluatedExcellent(userId, new KiiObjectListCallback<Request> () {
            @Override
            public void success(int token, List<Request> result) {
                LogUtil.d (TAG, "success: EvaluatedExcellent_size=" + result.size ());
                int num = result.size();
                String number = String.valueOf(num);
                numberOfExcellent.setText(number);
            }

            @Override
            public void failure(@Nullable Exception e) {
                LogUtil.w (TAG, e);
            }
        });

        KiiRequestConnection.fetchEvaluatedGood(userId, new KiiObjectListCallback<Request> () {
            @Override
            public void success(int token, List<Request> result) {
                LogUtil.d (TAG, "success: EvaluatedGood_size=" + result.size ());
                int num = result.size();
                String number = String.valueOf(num);
                numberOfGood.setText(number);
            }

            @Override
            public void failure(@Nullable Exception e) {
                LogUtil.w (TAG, e);
            }
        });

        KiiRequestConnection.fetchEvaluatedBad(userId, new KiiObjectListCallback<Request> () {
            @Override
            public void success(int token, List<Request> result) {
                LogUtil.d (TAG, "success: EvaluatedBad_size=" + result.size ());
                int num = result.size();
                String number = String.valueOf(num);
                numberOfBad.setText(number);
            }

            @Override
            public void failure(@Nullable Exception e) {
                LogUtil.w (TAG, e);
            }
        });


        KiiBookConnection.fetchPostedBooks(userId, new KiiObjectListCallback<Book>(){
            @Override
            public void success(int token, List<Book> result) {
                Log.d(TAG, "success: number_my_books=" + result.size());
                int num = result.size();
                String number = String.valueOf(num);
                assert numberOfBooks != null;
                numberOfBooks.setText(number);
                mListAdapter.add(result);
                mListAdapter.notifyDataSetChanged();

                int postedBook = result.size();
                final int pointsByBooks = postedBook;
//                final int pointsByBooks = postedBook / 10;
                Log.d(TAG, "pointsByBooks:" + pointsByBooks);

                int diff = 0;
                KiiMemberConnection.updatePoint(userId, diff, new KiiObjectCallback<Member>() {
                    @Override
                    public void success(int token, Member member) {
                        member.setPointsByBooks(pointsByBooks);
                        member.save(false, new KiiModel.KiiSaveCallback() {
                            @Override
                            public void success(int token, KiiObject object) {

                            }

                            @Override
                            public void failure(@Nullable Exception e) {
                            }
                        });
                    }
                    @Override
                    public void failure(Exception e) {
                    }
                });
            }
            @Override
            public void failure(@Nullable Exception e) {
                LogUtil.w(TAG, e);
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
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
            startActivityForResult(intent,USER_EDIT);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
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
                startActivity(getIntent());
            }
        }
        else if (requestCode == USER_EDIT) {
            if (resultCode == RESULT_OK) {
                LogUtil.d(TAG,"プロフィールが更新されたね");
                startActivity(getIntent());

            }
        }
    }

    private void kickBookSearch( Intent data ){

        Bundle extras = data.getExtras();
        String code = extras.getString("READ_CODE");
        String format = extras.getString("READ_FORMAT");
        Toast.makeText(this, "Contents = " + code +
                ", Format = " + format, Toast.LENGTH_SHORT).show();
        // 書籍情報を検索
        Intent intent = new Intent(UserpageActivity.this,BookSearchListActivity.class);
        QueryParamSet queryParamSet = new QueryParamSet();
        queryParamSet.addQueryParam(BookInfo.ISBN, code);
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
            Intent intent = new Intent(this, LikesActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_exchange) {
            Intent intent = new Intent(this, HadArrivedBookActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_transaction) {
            Intent intent = new Intent(this, ExhibitedBookActivity.class);
            startActivity(intent);

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
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    void setProfileInMenu(View drawerView) {
        LinearLayout llUserContainer = (LinearLayout) drawerView.findViewById(R.id.ll_user_container);

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

