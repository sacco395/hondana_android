package com.books.hondana;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Tab3ViewPagerAdapter extends FragmentPagerAdapter {

    public Tab3ViewPagerAdapter(FragmentManager fm) {
        super (fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new StepOneFragment ();
        } else if (position == 1) {
            return new StepTwoFragment ();
        } else return new StepThreeFragment ();
        }


    @Override
    public int getCount() {
        return 3;
    }
}
