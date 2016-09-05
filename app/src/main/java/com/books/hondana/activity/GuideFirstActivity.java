package com.books.hondana.activity;

import android.app.Activity;
import android.os.Bundle;

import com.books.hondana.R;

public class GuideFirstActivity extends Activity {
    private static final String TAG = "GuideFirstActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_first);
//        LogUtil.e(TAG, "error", e);
    }
}