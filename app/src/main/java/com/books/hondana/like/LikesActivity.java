package com.books.hondana.like;

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

import com.books.hondana.R;
import com.books.hondana.activity.BookDetailActivity;
import com.books.hondana.activity.BookInfoActivity;
import com.books.hondana.activity.BookMainActivity;
import com.books.hondana.activity.BookSearchListActivity;
import com.books.hondana.activity.InquiryActivity;
import com.books.hondana.activity.SimpleScannerActivity;
import com.books.hondana.activity.UserpageActivity;
import com.books.hondana.arrived.HadArrivedBookActivity;
import com.books.hondana.connection.KiiBookConnection;
import com.books.hondana.connection.KiiLikeConnection;
import com.books.hondana.connection.KiiMemberConnection;
import com.books.hondana.connection.KiiObjectCallback;
import com.books.hondana.connection.KiiObjectListCallback;
import com.books.hondana.connection.QueryParamSet;
import com.books.hondana.exhibited.ExhibitedBookActivity;
import com.books.hondana.guide.GuideActivity;
import com.books.hondana.model.Book;
import com.books.hondana.model.BookInfo;
import com.books.hondana.model.Like;
import com.books.hondana.model.Member;
import com.books.hondana.setting.SettingActivity;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiUser;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class LikesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = LikesActivity.class.getSimpleName();

    final ImageLoader imageLoader = ImageLoader.getInstance();

    // Intent Parameter
    private static final int ACT_READ_BARCODE = 1;
    private static final int ACT_BOOK_SEARCH_LIST = 2;
    private static final int ACT_BOOK_DETAIL_TO_ADD = 3;

    ////////////////////////////////////////
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;
    LikeBookListAdapter mListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final KiiUser user = KiiUser.getCurrentUser();
        assert toolbar != null;
        toolbar.setTitle("気になる本一覧");
        setSupportActionBar(toolbar);

        mListAdapter = new LikeBookListAdapter(new ArrayList<Like>(), new LikeBookListAdapter.LikeItemClickListener() {
            @Override
            public void onClick(Like like) {
                final String bookId = like.getBookId();
                LogUtil.d(TAG, "bookId: " + bookId);
                KiiBookConnection.fetchByBookId(bookId, new KiiObjectCallback<Book>() {
                    @Override
                    public void success(int token, Book book) {
                        Intent intent = new Intent(getApplicationContext(), BookInfoActivity.class);
                        intent.putExtra(Book.class.getSimpleName(), book);

                        LogUtil.d(TAG, "onItemClick: " + book);
                        startActivity(intent);
                    }

                    @Override
                    public void failure(Exception e) {
                        LogUtil.e(TAG, "failure: ", e);
                    }
                });
            }
        });

        ListView mListView = (ListView) findViewById(R.id.list_view);
        assert mListView != null;
        mListView.setAdapter(mListAdapter);


        //カメラボタン
        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fab);
        assert mFab != null;
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSimpleActivity(v);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);


        View header = navigationView.getHeaderView(0);
        final ImageView userIcon = (ImageView) header.findViewById(R.id.iv_user_icon);
        assert user != null;
        final String userId = user.getID();
        KiiMemberConnection.fetch(userId, new KiiObjectCallback<Member>() {
            @Override
            public void success(int token, Member member) {
                if (!member.hasValidImageUrl()) {
                    return;
                }
                final String imageUrl = member.getImageUrl();
                LogUtil.d(TAG, "imageUrl: " + imageUrl);
                imageLoader.displayImage(imageUrl, userIcon);
            }

            @Override
            public void failure(Exception e) {
                LogUtil.w(TAG, e);
            }
        });
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d(TAG, "onClick: User click!");
            }
        });

        TextView userName = (TextView) header.findViewById(R.id.tv_user_name);
        assert user != null;
        userName.setText(user.getUsername());

        fetchStared();
    }


    private void fetchStared(){
        KiiUser kiiUser = KiiUser.getCurrentUser();
        assert kiiUser != null;
        final String userId = kiiUser.getID();
        KiiLikeConnection.fetchStared(userId, new KiiObjectListCallback<Like>() {
            @Override
            public void success(int token, List<Like> result) {
                Log.d(TAG, "success: size=" + result.size());

                mListAdapter.add(result);
                mListAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(@Nullable Exception e) {


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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
        // 書籍情報を検索
        Intent intent = new Intent(LikesActivity.this,BookSearchListActivity.class);
        QueryParamSet queryParamSet = new QueryParamSet();
        queryParamSet.addQueryParam(BookInfo.ISBN, code);
        intent.putExtra( "SEARCH_PARAM", queryParamSet );
        startActivityForResult(intent, ACT_BOOK_SEARCH_LIST);
    }

    private void kickListSearchResult(Intent data){
        Bundle extras = data.getExtras();
        Book book = extras.getParcelable(Book.class.getSimpleName());
        Intent intent = new Intent(LikesActivity.this,BookDetailActivity.class);
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
                Intent intent = new Intent(LikesActivity.this, UserpageActivity.class);
                startActivity(intent);
            }
        });
    }
}