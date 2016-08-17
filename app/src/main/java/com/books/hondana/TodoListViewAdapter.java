package com.books.hondana;

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

import com.books.hondana.activity.SelectedBooksActivity;

public class TodoListViewAdapter extends BaseAdapter {

    static class ViewHolder {
        TextView textView;
        TextView textView2;
        ImageView imageView;
    }

    private LayoutInflater inflater;
    private int itemLayoutId;
    private String[] date;
    private String[] todo;
    private int[] ids;

    public TodoListViewAdapter(Context context, int itemLayoutId, String[] date, String[] todo, int[] photos) {
        super();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemLayoutId = itemLayoutId;
        this.date = date;
        this.todo = todo;
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
        holder.textView.setText(date[position]);
        holder.textView2.setText(todo[position]);


        return convertView;
    }

    @Override
    public int getCount() {
        // texts 配列の要素数
        return todo.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class PassedRequestFragment extends Fragment
            implements AdapterView.OnItemClickListener {
        private BaseAdapter adapter;
        // Isle of Wight in U.K.
        private static final String[] todo = {
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
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            ListView listView = (ListView) view.findViewById(R.id.todo_list_view);
            adapter = new TodoListViewAdapter (this.getContext(), R.layout.part_todo_list, date, todo, photos);

            // ListViewにadapterをセット
            listView.setAdapter(adapter);

            // 後で使います
            listView.setOnItemClickListener((AdapterView.OnItemClickListener) this);

        }


        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            Intent intent = new Intent(this.getContext(), SelectedBooksActivity.class);
            // clickされたpositionのtextとphotoのID
            String selectedText = todo[position];
            int selectedPhoto = photos[position];
            // インテントにセット
            intent.putExtra("Text", selectedText);
            intent.putExtra("Photo", selectedPhoto);
            // Activity をスイッチする
            startActivity(intent);
        }
    }
}