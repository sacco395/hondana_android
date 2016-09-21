package com.books.hondana;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.books.hondana.activity.ReceivedBookActivity;
import com.squareup.picasso.Picasso;

import java.util.List;


//<MessageRecord>はデータクラスMessageRecordのArrayAdapterであることを示している。このアダプターで管理したいデータクラスを記述されば良い。
public class BookRequestListAdapter extends ArrayAdapter<MyBookList> {


    //アダプターを作成する関数。コンストラクター。クラス名と同じです。
    public BookRequestListAdapter(Context context) {

        super(context, R.layout.part_book_list);
    }

    //表示するViewを返します。これがListVewの１つのセルとして表示されます。表示されるたびに実行されます。
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //convertViewをチェックし、Viewがないときは新しくViewを作成します。convertViewがセットされている時は未使用なのでそのまま再利用します。メモリーに優しい。
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.part_my_book_list, parent, false);
        }

        //レイアウトにある画像と文字のViewを所得します。
        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_BookImg);
        TextView title = (TextView) convertView.findViewById(R.id.tv_BookTitle);
        TextView author = (TextView) convertView.findViewById(R.id.tv_BookAuthor);


        convertView.setOnClickListener(new View.OnClickListener() {
            //クリックした時
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ReceivedBookActivity.class);

                // 渡したいデータとキーを指定する。urlという名前でリンクの文字列を渡しています。
                MyBookList MyBookList = getItem(position);
                String id = MyBookList.getId();
                intent.putExtra("_id", id);

                // 遷移先の画面を呼び出す
                view.getContext().startActivity(intent);
            }
        });


        //表示するセルの位置からデータをMessageRecordのデータを取得します
        MyBookList imageRecord = getItem(position);

        //mImageLoaderを使って画像をダウンロードし、Viewにセットします。
        String image_url = imageRecord.getImageUrl();
        Picasso.with(convertView.getContext()).load(image_url).into(imageView);

        //Viewに文字をセットします。
        title.setText(imageRecord.getTitle());
        author.setText(imageRecord.getAuthor());

        //1つのセルのViewを返します。
        return convertView;
    }

    //データをセットしなおす関数
    public void setMyBookList(List<MyBookList> objects) {
        //ArrayAdapterを空にする。
        clear();
        //テータの数だけMessageRecordを追加します。
        for (MyBookList object : objects) {
            add(object);
        }
        //データの変更を通知します。
        notifyDataSetChanged();
    }
}
