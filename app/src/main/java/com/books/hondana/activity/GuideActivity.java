//ガイド
package com.books.hondana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.books.hondana.Connection.KiiCloudConnection;
import com.books.hondana.Model.KiiCloudBucket;
import com.books.hondana.Model.Member;
import com.books.hondana.R;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.query.KiiQueryResult;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class GuideActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "GuideActivity";
    final ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("ガイド");
        setSupportActionBar(toolbar);


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
                            LogUtil.d(TAG, "didFinish(result: " + result + ")");
                            if (result == null) {
                                Log.w(TAG, e);
                                return;
                            }

                            final List<KiiObject> kiiObjects = result.getResult();
                            LogUtil.d(TAG, "members.size: " + kiiObjects.size());
                            if (kiiObjects != null && kiiObjects.size() > 0) {
                                final KiiObject kiiObject = kiiObjects.get(0);// ひとつしか来ていないはずなので0番目だけ使う
                                final Member member = new Member(kiiObject);

                                final String imageUrl = member.get(Member.IMAGE_URL);
                                LogUtil.d(TAG, "imageUrl: " + imageUrl);
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

        //         this, binding.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // binding.drawerLayout.setDrawerListener(toggle);
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
                    Intent intent = new Intent(GuideActivity.this,
                            UserpageActivity.class);
                    GuideActivity.this.startActivity(intent);

                } else {
                    Intent intent = new Intent(GuideActivity.this,
                            StartActivity.class);
                    GuideActivity.this.startActivity(intent);
                    showToast("会員登録をお願いします！");
                }
            }
        });
        //navigationViewにアイコンここまで

        // binding.navView.setNavigationItemSelectedListener(this);

//        ガイドページの項目のリストビュー
        ListView list = (ListView) findViewById(R.id.list_view);
        String[] item01 = getResources().getStringArray(R.array.array01);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, item01);
        list.setAdapter(adapter);

        // リスト項目がクリックされた時の処理
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                String strData = adapter.getItem(position);

                Intent intent = new Intent();

                switch (position) {
                    case 0:
                        intent.setClass(GuideActivity.this, GuideFirstActivity.class);
                        break;
                    case 1:
                        intent.setClass(GuideActivity.this, GuidePassActivity.class);
                        break;
                    case 2:
                        intent.setClass(GuideActivity.this, GuideRegisterActivity.class);
                        break;
                    case 3:
                        intent.setClass(GuideActivity.this, GuideBukuActivity.class);
                        break;
                    case 4:
                        intent.setClass(GuideActivity.this, GuideSendingActivity.class);
                        break;
                }
                intent.putExtra("SELECTED_DATA", strData);
                startActivity(intent);
            }
        });
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
//        getMenuInflater().inflate(R.menu.guide, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
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
                LogUtil.d(TAG, "onClick");
                Intent intent = new Intent(GuideActivity.this, UserpageActivity.class);
                startActivity(intent);
            }
        });*/
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
