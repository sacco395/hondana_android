package com.books.hondana.todo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.books.hondana.arrived.SentRequestBookFragment;
import com.books.hondana.exhibited.ReceivedRequestBookFragment;

public class TodoTabAdapter extends FragmentPagerAdapter {

    public TodoTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position ==0) {
            return new ReceivedRequestBookFragment();
        } else return new SentRequestBookFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
