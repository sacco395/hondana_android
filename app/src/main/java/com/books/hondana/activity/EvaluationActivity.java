package com.books.hondana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.books.hondana.AllEvaluationFragment;
import com.books.hondana.BadEvaluationFragment;
import com.books.hondana.GoodEvaluationFragment;
import com.books.hondana.NeutralEvaluationFragment;
import com.books.hondana.R;

import java.util.ArrayList;
import java.util.List;

public class EvaluationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("評価一覧");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //スキャン画面へ移動
                Intent intent = new Intent(EvaluationActivity.this, BarcodeScanActivity.class);
                startActivity(intent);
            }
        });


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

    }
    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("すべて");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("良い");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_good, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("普通");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_neutral, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFour.setText("悪い");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_bad, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter2 adapter2
                = new ViewPagerAdapter2(getSupportFragmentManager());
        adapter2.addFragment(new AllEvaluationFragment(), "すべて");
        adapter2.addFragment(new GoodEvaluationFragment(), "良い");
        adapter2.addFragment(new NeutralEvaluationFragment(), "普通");
        adapter2.addFragment(new BadEvaluationFragment(), "悪い");
        viewPager.setAdapter(adapter2);
    }

    class ViewPagerAdapter2 extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter2(FragmentManager manager) {
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

}
