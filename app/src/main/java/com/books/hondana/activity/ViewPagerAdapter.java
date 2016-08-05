package com.books.hondana.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.books.hondana.PassedBooksFragment;
import com.books.hondana.ReceivedBooksFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
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