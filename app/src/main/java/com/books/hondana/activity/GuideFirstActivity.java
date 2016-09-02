package com.books.hondana.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.books.hondana.R;

public class GuideFirstActivity extends Activity {
    private static final String TAG = "GuideFirstActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_first);
        Log.e(TAG,"error", new Throwable());
    }
}