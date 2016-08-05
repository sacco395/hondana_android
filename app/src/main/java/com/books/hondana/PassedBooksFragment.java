package com.books.hondana;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.books.hondana.activity.LikesSelectedActivity;

public class PassedBooksFragment extends Fragment
        implements AdapterView.OnItemClickListener {
    private BaseAdapter adapter;
    // Isle of Wight in U.K.
    private static final String[] scenes = {
            // Scenes of Isle of Wight
            "デザイン思考は世界を変える",
            "十月の旅人",
            "無印良品は仕組みが９割",
    };

    private static final String[] authors = {
            // Scenes of Isle of Wight
            "ティム・ブラウン",
            "レイ・ブラッドベリ",
            "松井忠三",
    };

    // ちょっと冗長的ですが分かり易くするために
    private static final int[] photos = {
            R.drawable.changedesign,
            R.drawable.october,
            R.drawable.muji,
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_passed_books, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = (ListView) view.findViewById(R.id.list);
        adapter = new ListViewAdapter(this.getContext(), R.layout.part_book_list, scenes, authors, photos);

        // ListViewにadapterをセット
        listView.setAdapter(adapter);

        // 後で使います
        listView.setOnItemClickListener((AdapterView.OnItemClickListener) this);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Intent intent = new Intent(this.getContext(), LikesSelectedActivity.class);
        // clickされたpositionのtextとphotoのID
        String selectedText = scenes[position];
        int selectedPhoto = photos[position];
        // インテントにセット
        intent.putExtra("Text", selectedText);
        intent.putExtra("Photo", selectedPhoto);
        // Activity をスイッチする
        startActivity(intent);
    }
}

