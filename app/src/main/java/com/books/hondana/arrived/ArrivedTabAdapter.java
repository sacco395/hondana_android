package com.books.hondana.arrived;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.books.hondana.exhibited.ExhibitedBookFragment;
import com.books.hondana.exhibited.HadSendBookFragment;
import com.books.hondana.exhibited.ReceivedRequestBookFragment;

public class ArrivedTabAdapter extends FragmentPagerAdapter {

    public ArrivedTabAdapter(FragmentManager fm) {
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
