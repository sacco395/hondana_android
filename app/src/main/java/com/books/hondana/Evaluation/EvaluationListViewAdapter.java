package com.books.hondana.Evaluation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.books.hondana.R;
import com.books.hondana.activity.SelectedBooksActivity;

public class EvaluationListViewAdapter extends BaseAdapter {

    static class ViewHolder {
        TextView textView;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        ImageView imageView;
    }

    private LayoutInflater inflater;
    private int itemLayoutId;
    private String[] titles;
    private String[] authors;
    private String[] comments;
    private String[] date;
    private int[] ids;

    public EvaluationListViewAdapter(Context context, int itemLayoutId, String[] scenes, String[] authors, String[] comments, String[] date, int[] photos) {
        super();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemLayoutId = itemLayoutId;
        this.titles = scenes;
        this.authors = authors;
        this.comments = comments;
        this.date = date;
        this.ids = photos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        // 最初だけ View を inflate して、それを再利用する
        if (convertView == null) {

            convertView = inflater.inflate(itemLayoutId, parent, false);
            // ViewHolder を生成
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.textView);
            holder.textView2 = (TextView) convertView.findViewById(R.id.textView2);
            holder.textView3 = (TextView) convertView.findViewById(R.id.textView3);
            holder.textView4 = (TextView) convertView.findViewById(R.id.textView4);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        }
        // holder を使って再利用
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        // holder の imageView にセット
        holder.imageView.setImageResource(ids[position]);
        // 現在の position にあるファイル名リストを holder の textView にセット
        holder.textView.setText(titles[position]);
        holder.textView2.setText(authors[position]);
        holder.textView3.setText(comments[position]);
        holder.textView4.setText(date[position]);

        return convertView;
    }

    @Override
    public int getCount() {
        // texts 配列の要素数
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class AllEvaluationFragment extends Fragment
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
        private static final String[] comments = {
                // Scenes of Isle of Wight
                "本日届きました。ありがとうございました。",
                "ほげ",
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
            adapter = new EvaluationListViewAdapter (this.getContext(), R.layout.part_book_list, scenes, authors, comments, date, photos);

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
}