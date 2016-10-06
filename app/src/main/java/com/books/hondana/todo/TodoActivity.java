package com.books.hondana.todo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.books.hondana.R;
import com.books.hondana.arrived.SentRequestBookFragment;
import com.books.hondana.exhibited.ReceivedRequestBookFragment;

import java.util.ArrayList;
import java.util.List;


public class TodoActivity extends AppCompatActivity {

    private static final String TAG = TodoActivity.class.getSimpleName();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_todo);

        // ツールバーをアクションバーとしてセット
        Toolbar toolbar = (Toolbar) findViewById (R.id.toolbar);
        toolbar.setTitle ("やることリスト");
        setSupportActionBar (toolbar);


        ActionBar actionBar = getSupportActionBar ();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled (true);
        }
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        TodoActivity.ToDoTabAdapter adapter = new TodoActivity.ToDoTabAdapter (getSupportFragmentManager());
        adapter.addFragment(new ReceivedRequestBookFragment(), "リクエストされた本");
        adapter.addFragment(new SentRequestBookFragment(), "リクエストした本");
        viewPager.setAdapter(adapter);
    }
    class ToDoTabAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<> ();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ToDoTabAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId ()) {
            case android.R.id.home:
                onBackPressed ();
                return true;
            default:
                return super.onOptionsItemSelected (item);
        }

    }


}