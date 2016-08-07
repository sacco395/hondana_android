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

import com.books.hondana.activity.SelectedBooksActivity;

public class GoodEvaluationFragment extends Fragment
        implements AdapterView.OnItemClickListener {
    private BaseAdapter adapter;
    // Isle of Wight in U.K.
    private static final String[] scenes = {
            // Scenes of Isle of Wight
            "良い : 送った相手",
            "良い : 送ってもらった相手",
            "良い : 送った相手",
    };

    private static final String[] authors = {
            // Scenes of Isle of Wight
            "スピカ",
            "らっこ",
            "さきこ",
    };


    private static final String[] comments = {
            // Scenes of Isle of Wight
            "本日届きました。ありがとうございました。",
            "",
            "また機会がありましたらよろしくお願いします。",
    };

    private static final String[] date = {
            // Scenes of Isle of Wight
            "2016.00.00 00:00",
            "2016.00.00 00:00",
            "2016.00.00 00:00",
    };
    // ちょっと冗長的ですが分かり易くするために
    private static final int[] photos = {
            R.drawable.usericon,
            R.drawable.usericon,
            R.drawable.usericon,
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
        adapter = new ListViewAdapter2(this.getContext(), R.layout.part_evaluation_list, scenes, authors, comments, date, photos);

        // ListViewにadapterをセット
        listView.setAdapter(adapter);

        // 後で使います
        listView.setOnItemClickListener((AdapterView.OnItemClickListener) this);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Intent intent = new Intent(this.getContext(), SelectedBooksActivity.class);
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
