package com.books.hondana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.books.hondana.R;

public class SelectedBooksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes_selected);

        Intent intent = getIntent();
        String selectedText = intent.getStringExtra("Text");
        int selectedPhoto = intent.getIntExtra("Photo", 0);

        TextView textView = (TextView)findViewById(R.id.selected_text);
        assert textView != null;
        textView.setText(selectedText);

        ImageView  imageView = (ImageView) findViewById(R.id.selected_photo);
        assert imageView != null;
        imageView.setImageResource(selectedPhoto);
    }

}