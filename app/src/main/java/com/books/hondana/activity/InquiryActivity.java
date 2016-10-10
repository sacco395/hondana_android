//お問い合わせ
package com.books.hondana.activity;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.books.hondana.R;
import com.books.hondana.connection.KiiMemberConnection;
import com.books.hondana.connection.KiiObjectCallback;
import com.books.hondana.arrived.HadArrivedBookActivity;
import com.books.hondana.exhibited.ExhibitedBookActivity;
import com.books.hondana.guide.GuideActivity;
import com.books.hondana.like.LikesActivity;
import com.books.hondana.model.Member;
import com.books.hondana.setting.SettingActivity;
import com.books.hondana.start.StartActivity;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiUser;
import com.squareup.picasso.Picasso;

public class InquiryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    private static final String TAG = InquiryActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle("お問い合わせ");
        setSupportActionBar(toolbar);

        findViewById(R.id.buttonMail).setOnClickListener(this);


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
                            Picasso.with(InquiryActivity.this)
                                    .load(imageUrl)
                                    .into(userIcon);
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

        //         this, binding.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // binding.drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        //navigationViewにアイコンここから
        View header = navigationView.getHeaderView(0);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KiiUser kiiUser = KiiUser.getCurrentUser();
                LogUtil.d(TAG, "kiiUser: " + kiiUser);

                if (kiiUser != null) {
                    Intent intent = new Intent(InquiryActivity.this,
                            UserpageActivity.class);
                    InquiryActivity.this.startActivity(intent);


                } else {
                    Intent intent = new Intent(InquiryActivity.this,
                            StartActivity.class);
                    InquiryActivity.this.startActivity(intent);
                    showToast("会員登録をお願いします！");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.buttonMail:
                    // クリック処理
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:info@spica-travel.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "ホンダナへのお問い合わせ");
                    intent.putExtra(Intent.EXTRA_TEXT, "お問い合わせ内容を記入してください");
                    //createChooserを使うと選択ダイアログのタイトルを変更する事ができます。
                    startActivity(Intent.createChooser(intent,"どちらで送信するか選んでください"));
                    //通常のブラウザ起動です。
                    //startActivity(intent);


                default:
                    break;
            }
        }
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
//        getMenuInflater().inflate(R.menu.inquiry, menu);
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
                Intent intent = new Intent(InquiryActivity.this, UserpageActivity.class);
                startActivity(intent);
            }
        });
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
