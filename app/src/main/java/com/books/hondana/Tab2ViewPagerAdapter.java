package com.books.hondana;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Tab2ViewPagerAdapter extends FragmentPagerAdapter {

    public Tab2ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position ==0) {
            return new PassedBooksFragment();
        } else return new ReceivedBooksFragment();
        }

    @Override
    public int getCount() {
        return 2;
    }
}