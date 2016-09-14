package com.books.hondana;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.books.hondana.Model.Genre;

/**
 * Created by Administrator on 2016/08/02.
 */
public class BookMainFragmentPagerAdapter
        extends FragmentPagerAdapter {

    private final Genre[] genres = new Genre[] {
            Genre.ALL,
            Genre.LITERATURE,
            Genre.BUSINESS,
            Genre.TECHNOLOGY,
            Genre.ART,
            Genre.POCKET_EDITION,
            Genre.PAPERBACK,
            Genre.COMIC,
            Genre.OTHERS
    };

    public BookMainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return genres[position].title();
    }

    @Override
    public Fragment getItem(int position) {
        return HondanaBooksFragment.newInstance(genres[position]);
    }

    @Override
    public int getCount() {
        return genres.length;
    }
}
