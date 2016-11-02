//トップページ
package com.books.hondana.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.books.hondana.BookMainFragmentPagerAdapter;
import com.books.hondana.R;
import com.books.hondana.arrived.HadArrivedBookActivity;
import com.books.hondana.connection.KiiMemberConnection;
import com.books.hondana.connection.KiiObjectCallback;
import com.books.hondana.connection.KiiObjectListCallback;
import com.books.hondana.connection.KiiRequestConnection;
import com.books.hondana.connection.QueryParamSet;
import com.books.hondana.exhibited.ExhibitedBookActivity;
import com.books.hondana.guide.GuideActivity;
import com.books.hondana.like.LikesActivity;
import com.books.hondana.model.Book;
import com.books.hondana.model.BookInfo;
import com.books.hondana.model.Member;
import com.books.hondana.model.Request;
import com.books.hondana.setting.SettingActivity;
import com.books.hondana.start.StartActivity;
import com.books.hondana.todo.TodoActivity;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiUser;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import static com.books.hondana.R.id.nav_todo;

public class BookMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = BookMainActivity.class.getSimpleName();

    final ImageLoader imageLoader = ImageLoader.getInstance();

    // Intent Parameter
    private static final int ACT_READ_BARCODE = 1;
    private static final int ACT_BOOK_SEARCH_LIST = 2;
    private static final int ACT_BOOK_DETAIL_TO_ADD = 3;

    ////////////////////////////////////////
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setExitTransition(new Slide());
        }

        setContentView(R.layout.activity_book_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //カメラボタン
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                KiiUser kiiUser = KiiUser.getCurrentUser ();
                LogUtil.d (TAG, "kiiUser: " + kiiUser);

                if (kiiUser != null) {
                    launchSimpleActivity(v);

                } else {
                    Intent intent = new Intent(BookMainActivity.this,
                            StartActivity.class);
                    BookMainActivity.this.startActivity(intent);
                    showToast("会員登録をお願いします！");
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setProfileInMenu(drawerView);
                final ImageView userIcon = (ImageView) drawerView.findViewById(R.id.iv_user_icon);
                KiiUser kiiUser = KiiUser.getCurrentUser();
                LogUtil.d(TAG, "kiiUser: " + kiiUser);
                if (kiiUser != null) {
                    final String userId = kiiUser.getID();

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
                            Log.w(TAG, e);
                        }
                    });
                    TextView userName = (TextView) drawerView.findViewById(R.id.tv_user_name);
                    userName.setText(kiiUser.getUsername());
                }
            }
        };
        assert drawer != null;
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        //navigationViewにアイコンここから
        View header = navigationView.getHeaderView(0);
//        ImageView userIcon = (ImageView) header.findViewById(R.id.iv_user_icon);
//        Picasso.with(this).load("http://www.flamme.co.jp/common/profile/kasumi_arimura.jpg").into(userIcon);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KiiUser kiiUser = KiiUser.getCurrentUser();
                LogUtil.d(TAG, "kiiUser: " + kiiUser);

                if (kiiUser != null) {
                    Intent intent = new Intent(BookMainActivity.this,
                            UserpageActivity.class);
                    BookMainActivity.this.startActivity(intent);

                } else {
                    Intent intent = new Intent(BookMainActivity.this,
                            StartActivity.class);
                    BookMainActivity.this.startActivity(intent);
                    showToast("会員登録をお願いします！");
                }
            }
        });
        //navigationViewにアイコンここまで

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        assert viewPager != null;
        viewPager.setAdapter( new BookMainFragmentPagerAdapter( getSupportFragmentManager()));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

            // if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //     binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater ().inflate (R.menu.toolbar_top, menu);
        final MenuItem menu_bgm = menu.findItem (R.id.nav_todo);
        KiiUser kiiUser = KiiUser.getCurrentUser ();
        LogUtil.d (TAG, "kiiUser: " + kiiUser);
        if (kiiUser != null) {
            String userId = kiiUser.getID ();
            KiiRequestConnection.fetchTodoEvaluate (userId, new KiiObjectListCallback<Request> () {
                @Override
                public void success(int token, List<Request> result) {
                    Log.d (TAG, "リクエストしたやること" + result.size ());
                    if (result.size () == 0) {
                        menu_bgm.setIcon (R.drawable.ic_done_black_24dp);
                    } else {
                        menu_bgm.setIcon (R.drawable.ic_check_circle_black_24px);
                    }
                }

                @Override
                public void failure(@Nullable Exception e) {
                    LogUtil.e (TAG, "failure: ", e);
                }
            });
            KiiRequestConnection.fetchTodoSent (userId, new KiiObjectListCallback<Request> () {
                @Override
                public void success(int token, List<Request> result) {
                    Log.d (TAG, "リクエストされたやること" + result.size ());
                    if (result.size () == 0) {
                        menu_bgm.setIcon (R.drawable.ic_done_black_24dp);
                    } else {
                        menu_bgm.setIcon (R.drawable.ic_check_circle_black_24px);
                    }
                }

                @Override
                public void failure(@Nullable Exception e) {
                    LogUtil.e (TAG, "failure: ", e);
                }
            });
        }
        return true;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        LogUtil.d(TAG, "onClick");
        KiiUser kiiUser = KiiUser.getCurrentUser ();
        LogUtil.d (TAG, "kiiUser: " + kiiUser);

        switch (id) {
//            case R.id.nav_search:{
//                if (kiiUser != null) {
//                    Intent intent = new Intent(this, SearchActivity.class);
//                    startActivity(intent,
//                            ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
//                } else {
//                    Intent intent = new Intent(this, StartActivity.class);
//                    startActivity(intent);
//                    showToast("会員登録をお願いします！");
//                }
//                break;
//
//            }
//
//
//            case R.id.nav_notifications: {
//                if (kiiUser != null) {
//                    Intent intent = new Intent(this, InfoActivity.class);
//                    startActivity(intent,
//                            ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
//                } else {
//                    Intent intent = new Intent(this, StartActivity.class);
//                    startActivity(intent);
//                    showToast("会員登録をお願いします！");
//                }
//                break;
//            }

            case nav_todo:{
                if (kiiUser != null) {
                    Intent intent = new Intent(this, TodoActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, StartActivity.class);
                    startActivity(intent);
                    showToast("会員登録をお願いします！");
                }

                break;
            }
        }
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
        Intent intent = new Intent(BookMainActivity.this,BookSearchListActivity.class);
        QueryParamSet queryParamSet = new QueryParamSet();
        queryParamSet.addQueryParam(BookInfo.ISBN, code);
        intent.putExtra( "SEARCH_PARAM", queryParamSet );
        startActivityForResult(intent, ACT_BOOK_SEARCH_LIST);
    }

    private void kickListSearchResult(Intent data){
        Bundle extras = data.getExtras();
        //HashMap<String,String> bookInfo = (HashMap<String, String>) data.getSerializableExtra("Book");
        Book book = (Book) extras.get(Book.class.getSimpleName());
        Intent intent = new Intent(BookMainActivity.this,BookDetailActivity.class);
        intent.putExtra(Book.class.getSimpleName(), book);
        startActivityForResult(intent, ACT_BOOK_DETAIL_TO_ADD);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        KiiUser kiiUser = KiiUser.getCurrentUser ();
        LogUtil.d (TAG, "kiiUser: " + kiiUser);

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, BookMainActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_like) {
            if (kiiUser != null) {
                Intent intent = new Intent(this, LikesActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, StartActivity.class);
                startActivity(intent);
                showToast("会員登録をお願いします！");
            }

        } else if (id == R.id.nav_exchange) {
            if (kiiUser != null) {
                Intent intent = new Intent(this, HadArrivedBookActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, StartActivity.class);
                startActivity(intent);
                showToast("会員登録をお願いします！");
            }


        } else if (id == R.id.nav_transaction) {
            if (kiiUser != null) {
                Intent intent = new Intent(this, ExhibitedBookActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, StartActivity.class);
                startActivity(intent);
                showToast("会員登録をお願いします！");
            }


        } else if (id == R.id.nav_set) {
            if (kiiUser != null) {
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, StartActivity.class);
                startActivity(intent);
                showToast("会員登録をお願いします！");
            }


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
                Intent intent = new Intent(BookMainActivity.this, UserpageActivity.class);
                startActivity(intent);
            }
        });
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
