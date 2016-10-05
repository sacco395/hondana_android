package com.books.hondana.arrived;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.books.hondana.exhibited.ExhibitedBookFragment;
import com.books.hondana.exhibited.HadSendBookFragment;

public class ArrivedTabAdapter extends FragmentPagerAdapter {

    public ArrivedTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position ==0) {
            return new SentRequestBookFragment();
        } else return new HadArrivedBookFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
