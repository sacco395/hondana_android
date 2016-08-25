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


public class BookInfoListViewAdapter extends BaseAdapter {

    static class ViewHolder {
        TextView textView;
        TextView textView2;
        ImageView imageView;
    }

    private LayoutInflater inflater;
    private int itemLayoutId;
    private String[] username;
    private String[] evaluation;

    public BookInfoListViewAdapter(Context context, int itemLayoutId, String[] username, String[] evaluation) {
        super();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemLayoutId = itemLayoutId;
        this.username = username;
        this.evaluation = evaluation;
    }



//ScrollViewでListViewが一行しか表示できない場合に以下を書く

//    public static void setListViewHeightBasedOnChildren(ListView listViewBookOwner) {
//        ListAdapter BookDetailListViewAdapter = listViewBookOwner.getAdapter();
//        if (BookDetailListViewAdapter == null) {
//            return;
//        }
//        int totalHeight = listViewBookOwner.getPaddingTop() + listViewBookOwner.getPaddingBottom();
//
//        for (int i = 0; i < BookDetailListViewAdapter.getCount(); i++) {
//            View listItem = BookDetailListViewAdapter.getView(i, null, listViewBookOwner);
//            if (listItem instanceof ViewGroup) {
//                listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT));
//            }
//            listItem.measure(0, 0);
//            totalHeight += listItem.getMeasuredHeight();
//        }
//
//        ViewGroup.LayoutParams params = listViewBookOwner.getLayoutParams();
//        params.height = totalHeight + (listViewBookOwner.getDividerHeight() * (BookDetailListViewAdapter.getCount() - 1));
//        listViewBookOwner.setLayoutParams(params);
//    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        // 最初だけ View を inflate して、それを再利用する
        if (convertView == null) {

            convertView = inflater.inflate(itemLayoutId, parent, false);
            // ViewHolder を生成
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.textBookOwnerListName);
            holder.textView2 = (TextView) convertView.findViewById(R.id.textBookOwnerListCondition);
            holder.imageView = (ImageView) convertView.findViewById(R.id.usericon);

            convertView.setTag(holder);
        }
        // holder を使って再利用
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        // holder の imageView にセット
        // 現在の position にあるファイル名リストを holder の textView にセット
        holder.textView.setText(username[position]);
        holder.textView2.setText(evaluation[position]);


        return convertView;
    }

    @Override
    public int getCount() {
        // texts 配列の要素数
        return evaluation.length;
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
        private static final String[] evaluation = {
                // Scenes of Isle of Wight
                "本日届きました。ありがとうございました。",
                "ほげ",
                "また機会がありましたらよろしくお願いします。",
        };

        private static final String[] username = {
                // Scenes of Isle of Wight
                "2016.00.00 00:00",
                "2016.00.00 00:00",
                "2016.00.00 00:00",
        };




        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            ListView listView = (ListView) view.findViewById(R.id.listViewBookOwner);
            adapter = new BookInfoListViewAdapter (this.getContext(), R.layout.part_book_owner, username, evaluation);

            // ListViewにadapterをセット
            listView.setAdapter(adapter);

            // 後で使います
            listView.setOnItemClickListener((AdapterView.OnItemClickListener) this);

        }


        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//            Intent intent = new Intent(this.getContext(), SelectedBooksActivity.class);
//            // clickされたpositionのtextとphotoのID
//            String selectedText = evaluation[position];
//            // インテントにセット
//            intent.putExtra("Text", selectedText);
//            // Activity をスイッチする
//            startActivity(intent);
        }
    }
}