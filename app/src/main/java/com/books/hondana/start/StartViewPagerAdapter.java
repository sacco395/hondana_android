package com.books.hondana.start;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class StartViewPagerAdapter extends FragmentPagerAdapter {

    public StartViewPagerAdapter(FragmentManager fm) {
        super (fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new StepOneFragment();
        } else if (position == 1) {
            return new StepTwoFragment();
        } else if (position == 2) {
            return new StepThreeFragment();
        } else return new StepFourFragment();

        }


    @Override
    public int getCount() {
        return 4;
    }
}
