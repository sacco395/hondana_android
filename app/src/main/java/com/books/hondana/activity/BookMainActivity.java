//トップページ
package com.books.hondana.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.books.hondana.BookMainFragmentPagerAdapter;
import com.books.hondana.Connection.KiiCloudConnection;
import com.books.hondana.Connection.QueryParamSet;
import com.books.hondana.Model.KiiBook;
import com.books.hondana.Model.KiiCloudBucket;
import com.books.hondana.Model.Member;
import com.books.hondana.R;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.query.KiiQueryResult;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class BookMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //private static final String TAG = "BookMainActivity";
    final static String TAG = BookMainActivity.class.getSimpleName();

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
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
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
                    final String userId = kiiUser.getID ();

                    final KiiCloudConnection membersConnection = new KiiCloudConnection(KiiCloudBucket.MEMBERS);
                    membersConnection.loadMember(userId, new KiiCloudConnection.SearchFinishListener() {
                        @Override
                        public void didFinish(int token, KiiQueryResult<KiiObject> result, Exception e) {
                            Log.d(TAG, "didFinish(result: " + result + ")");
                            if (result == null) {
                                Log.w(TAG, e);
                                return;
                            }

                            final List<KiiObject> kiiObjects = result.getResult();
                            Log.d(TAG, "members.size: " + kiiObjects.size());
                            if (kiiObjects != null && kiiObjects.size() > 0) {
                                final KiiObject kiiObject = kiiObjects.get(0);// ひとつしか来ていないはずなので0番目だけ使う
                                final Member member = new Member(kiiObject);

                                final String imageUrl = member.get(Member.IMAGE_URL);
                                Log.d(TAG, "imageUrl: " + imageUrl);
                                imageLoader.displayImage(imageUrl, userIcon);
                            }
                        }
                    });
                    TextView userName = (TextView) drawerView.findViewById(R.id.tv_user_name);
                    userName.setText(kiiUser.getUsername ().toString());
                }
            }
        };
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter( new BookMainFragmentPagerAdapter( getSupportFragmentManager()));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

            // if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //     binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_top, menu);
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
            case R.id.nav_search:{
                if (kiiUser != null) {
                    Intent intent = new Intent(this, SearchActivity.class);
                    startActivity(intent,
                            ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                } else {
                    Intent intent = new Intent(this, StartActivity.class);
                    startActivity(intent);
                    showToast("会員登録をお願いします！");
                }
                break;
            }


            case R.id.nav_notifications: {
                if (kiiUser != null) {
                    Intent intent = new Intent(this, InfoActivity.class);
                    startActivity(intent,
                            ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                } else {
                    Intent intent = new Intent(this, StartActivity.class);
                    startActivity(intent);
                    showToast("会員登録をお願いします！");
                }
                break;
            }

            case R.id.nav_todo:{
                if (kiiUser != null) {
                    Intent intent = new Intent(this, TodoActivity.class);
                    startActivity(intent,
                            ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                } else {
                    Intent intent = new Intent(this, StartActivity.class);
                    startActivity(intent);
                    showToast("会員登録をお願いします！");
                }

                break;
            }
        }

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    // the user can add items from the options menu.
    // create that menu here - from the res/menu/menu.xml file
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        //BookMainActivity.this.addItem(null);
//        return true;
//    }

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
                Intent intent = new Intent(this, SwapBookActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, StartActivity.class);
                startActivity(intent);
                showToast("会員登録をお願いします！");
            }


        } else if (id == R.id.nav_transaction) {
            if (kiiUser != null) {
                Intent intent = new Intent(this, RequestActivity.class);
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

        /*llUserContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d(TAG, "kiiUser: " + kiiUser);
                Intent intent = new Intent(BookMainActivity.this, UserpageActivity.class);
                startActivity(intent);
            }
        });*/
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
