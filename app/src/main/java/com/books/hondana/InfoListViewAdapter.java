package com.books.hondana;

import android.content.Context;
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

public class InfoListViewAdapter extends BaseAdapter {

    static class ViewHolder {
        TextView textView;
        TextView textView2;
        ImageView imageView;
    }

    private LayoutInflater inflater;
    private int itemLayoutId;
    private String[] info;
    private String[] date;
    private int[] ids;

    public InfoListViewAdapter(Context context, int itemLayoutId, String[] info, String[] date, int[] photos) {
        super();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemLayoutId = itemLayoutId;
        this.info = info;
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
        holder.textView.setText(info[position]);
        holder.textView2.setText(date[position]);

        return convertView;
    }

    @Override
    public int getCount() {
        // texts 配列の要素数
        return info.length;
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
        private static final String[] info = {
                // Scenes of Isle of Wight
                "有効期限が近づいているブクがあります",
                "事務局から個別メッセージ1ブクをプレゼントしました",
                "ホンダナで交換申請されるための秘訣教えます!",
        };

        private static final String[] date = {
                // Scenes of Isle of Wight
                "3日前",
                "4日前",
                "5日前",
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
            return inflater.inflate(R.layout.fragment_request_books, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            ListView listView = (ListView) view.findViewById(R.id.info_list_view);
            adapter = new InfoListViewAdapter (this.getContext(), R.layout.part_info_list, info, date, photos);

            // ListViewにadapterをセット
            listView.setAdapter(adapter);

            // 後で使います
            listView.setOnItemClickListener((AdapterView.OnItemClickListener) this);

        }


        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//            Intent intent = new Intent(this.getContext(), SelectedBooksActivity.class);
//            // clickされたpositionのtextとphotoのID
//            String selectedText = info[position];
//            int selectedPhoto = photos[position];
//            // インテントにセット
//            intent.putExtra("Text", selectedText);
//            intent.putExtra("Photo", selectedPhoto);
//            // Activity をスイッチする
//            startActivity(intent);
        }
    }
}