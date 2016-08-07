package com.books.hondana;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter2 extends FragmentPagerAdapter {

    public ViewPagerAdapter2(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position ==0) {
            return new AllEvaluationFragment();
        } else if (position == 1) {
            return new GoodEvaluationFragment();
        } else if (position == 1) {
            return new NeutralEvaluationFragment();
        } else return new BadEvaluationFragment();
    }

    @Override
    public int getCount() {
        return 4;
    }
}
