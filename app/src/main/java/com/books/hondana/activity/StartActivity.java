package com.books.hondana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.books.hondana.R;
import com.books.hondana.StepFourFragment;
import com.books.hondana.StepOneFragment;
import com.books.hondana.StepThreeFragment;
import com.books.hondana.StepTwoFragment;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity
        implements View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_start);
            findViewById(R.id.buttonLogin).setOnClickListener(this);
            findViewById(R.id.buttonRegistration).setOnClickListener(this);
            findViewById(R.id.skip).setOnClickListener(this);

            viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(viewPager);

            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
    }


    private void setupViewPager(ViewPager viewPager) {
        Tab3ViewPagerAdapter adapter = new Tab3ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new StepOneFragment (), "Step1");
        adapter.addFrag(new StepTwoFragment (), "Step2");
        adapter.addFrag(new StepThreeFragment (), "Step3");
        adapter.addFrag(new StepFourFragment (), "Step4");
        viewPager.setAdapter(adapter);
    }
    class Tab3ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Tab3ViewPagerAdapter(FragmentManager manager) {
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

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.buttonLogin:
                    // クリック処理
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    break;

                case R.id.buttonRegistration:
                    // クリック処理
                    intent = new Intent (this, RegisterActivity.class);
                    startActivity(intent);
                    break;

                case R.id.skip:
                    // クリック処理
                    intent = new Intent (this, BookMainActivity.class);
                    startActivity(intent);
                    break;

                default:
                    break;
            }
        }
    }
}
