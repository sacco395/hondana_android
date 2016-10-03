package com.books.hondana.exhibited;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ExhibitedTabAdapter extends FragmentPagerAdapter {

    public ExhibitedTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position ==0) {
            return new ExhibitedBookFragment ();
        } else if (position == 1) {
            return new ReceivedRequestBookFragment ();
        } else return new HadSendBookFragment ();
    }

    @Override
    public int getCount() {
        return 3;
    }
}
