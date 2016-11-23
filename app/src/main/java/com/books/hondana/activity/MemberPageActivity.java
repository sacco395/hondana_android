package com.books.hondana.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.books.hondana.MyBookListAdapter;
import com.books.hondana.R;
import com.books.hondana.arrived.HadArrivedBookActivity;
import com.books.hondana.connection.KiiBookConnection;
import com.books.hondana.connection.KiiMemberConnection;
import com.books.hondana.connection.KiiObjectCallback;
import com.books.hondana.connection.KiiObjectListCallback;
import com.books.hondana.connection.KiiRequestConnection;
import com.books.hondana.evaluation.EvaluationActivity;
import com.books.hondana.exhibited.ExhibitedBookActivity;
import com.books.hondana.guide.GuideActivity;
import com.books.hondana.like.LikesActivity;
import com.books.hondana.model.Book;
import com.books.hondana.model.Member;
import com.books.hondana.model.Request;
import com.books.hondana.setting.SettingActivity;
import com.books.hondana.start.StartActivity;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiUser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MemberPageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MemberPageActivity";

    private Member member;
    MyBookListAdapter mListAdapter;

    final ImageLoader imageLoader = ImageLoader.getInstance();

    public static Intent createIntent(Context context, Member member) {
        Intent intent = new Intent (context, MemberPageActivity.class);
        intent.putExtra (Member.class.getSimpleName (), member);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_memberpage);
        Toolbar toolbar = (Toolbar) findViewById (R.id.toolbar);

        //上記のcreateIntentでデータを受け取る
        member = getIntent ().getParcelableExtra (Member.class.getSimpleName ());
        //kiiBookがないのはおかしいのでcreateIntentを使うように怒る
        if (member == null) {
            throw new IllegalArgumentException ("createIntentを使ってください");
        }

        String member_name = member.getName ();
        toolbar.setTitle (member_name + "さん");
        setSupportActionBar (toolbar);

        mListAdapter = new MyBookListAdapter (new ArrayList<Book> (), new MyBookListAdapter.BookItemClickListener () {
            @Override
            public void onClick(Book book) {
                Intent intent = new Intent (getApplicationContext (), BookInfoActivity.class);
                intent.putExtra (Book.class.getSimpleName (), book);

                LogUtil.d (TAG, "onItemClick: " + book);
                startActivity (intent);
            }
        });

        ListView mListView = (ListView) findViewById (R.id.list_view);
        assert mListView != null;
        mListView.setAdapter (mListAdapter);


        final DrawerLayout drawer = (DrawerLayout) findViewById (R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle (
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened (drawerView);
                setProfileInMenu (drawerView);
                final ImageView userIcon = (ImageView) drawerView.findViewById(R.id.iv_user_icon);
                KiiUser kiiUser = KiiUser.getCurrentUser();
                LogUtil.d(TAG, "kiiUser: " + kiiUser);
                if (kiiUser != null) {
                    final String userId = kiiUser.getID();

                    KiiMemberConnection.fetch(userId, new KiiObjectCallback<Member> () {
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
        drawer.setDrawerListener (toggle);

        toggle.syncState ();


        TextView Username = (TextView) findViewById (R.id.user_name);
        TextView Username2 = (TextView) findViewById (R.id.user_name2);
        assert Username != null;
        Username.setText (member_name);
        assert Username2 != null;
        Username2.setText (member_name);

        NavigationView navigationView = (NavigationView) findViewById (R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener (this);


        View header = navigationView.getHeaderView (0);
        final ImageView navUserIcon = (ImageView) header.findViewById (R.id.iv_user_icon);
        header.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                LogUtil.d (TAG, "onClick: User click!");

                KiiUser kiiUser = KiiUser.getCurrentUser ();

                LogUtil.d (TAG, "kiiUser: " + kiiUser);

                if (kiiUser != null) {
                    Intent intent = new Intent (MemberPageActivity.this,
                            MemberPageActivity.class);
                    MemberPageActivity.this.startActivity (intent);
                } else {
                    Intent intent = new Intent (MemberPageActivity.this,
                            StartActivity.class);
                    MemberPageActivity.this.startActivity (intent);
                    showToast ("会員登録をお願いします！");
                }
            }

        });

        KiiUser user = KiiUser.getCurrentUser ();
        TextView userName = (TextView) header.findViewById (R.id.tv_user_name);
        userName.setText (user.getUsername ());


        LinearLayout Evaluation = (LinearLayout) findViewById (R.id.evaluation);


        Evaluation.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                LogUtil.d (TAG, "onClick");
                Intent intent = new Intent (MemberPageActivity.this, EvaluationActivity.class);
                startActivity (intent);
            }
        });


        final ImageView userIcon = (ImageView) findViewById (R.id.user_icon);
        final TextView userProfile = (TextView) findViewById (R.id.tv_user_profile);
        final TextView numberOfExcellent = (TextView) findViewById (R.id.numberOfExcellent);
        final TextView numberOfGood = (TextView) findViewById (R.id.numberOfGood);
        final TextView numberOfBad = (TextView) findViewById (R.id.numberOfBad);


        final String imageUrl = member.getImageUrl ();
        Log.d (TAG, "imageUrl: " + imageUrl);
        if (imageUrl.equals("")) {
        } else{
        Picasso.with (MemberPageActivity.this).load (imageUrl).into (navUserIcon);
        Picasso.with (MemberPageActivity.this).load (imageUrl).into (userIcon);
    }

        final String UserProfile = member.getProfile();
        Log.d(TAG, "profile: " + UserProfile);
        assert userProfile != null;
        userProfile.setText(UserProfile);

        final String userId = member.getId();
        Log.d(TAG, "userId: " + userId);

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
                mListAdapter.add(result);
                mListAdapter.notifyDataSetChanged();
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
        return true;
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
                Intent intent = new Intent(MemberPageActivity.this, UserpageActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}

