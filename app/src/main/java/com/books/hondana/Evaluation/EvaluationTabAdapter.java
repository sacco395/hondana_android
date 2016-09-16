package com.books.hondana.Evaluation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class EvaluationTabAdapter extends FragmentPagerAdapter {

    public EvaluationTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position ==0) {
            return new AllEvaluationFragment();
        } else if (position == 1) {
            return new GoodEvaluationFragment();
        } else if (position == 2) {
            return new NeutralEvaluationFragment();
        } else return new BadEvaluationFragment();
    }

    @Override
    public int getCount() {
        return 4;
    }
}
