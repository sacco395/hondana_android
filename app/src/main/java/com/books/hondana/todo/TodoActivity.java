package com.books.hondana.todo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.books.hondana.R;
import com.books.hondana.arrived.SentRequestBookFragment;
import com.books.hondana.exhibited.ReceivedRequestBookFragment;

import java.util.ArrayList;
import java.util.List;

public class TodoActivity extends AppCompatActivity {

    private static final String TAG = TodoActivity.class.getSimpleName();

    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        TodoTabAdapter adapter
                = new TodoTabAdapter(getSupportFragmentManager());
        adapter.addFragment(new ReceivedRequestBookFragment(), "リクエスト受信中");
        adapter.addFragment(new SentRequestBookFragment(), "リクエスト送信中");
        viewPager.setAdapter(adapter);
    }

    class TodoTabAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public TodoTabAdapter(FragmentManager manager) {
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