package com.books.hondana;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.books.hondana.Model.KiiBook;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/07/13.
 */
public class HondanaBookAdapter extends BaseAdapter {

    private static final String TAG = HondanaBookAdapter.class.getSimpleName();

    private ArrayList<KiiBook> mBooks;
    private BookItemClickListener mListener;

    public HondanaBookAdapter(ArrayList<KiiBook> books, BookItemClickListener listener) {
        this.mBooks = books;
        this.mListener = listener;
    }

    public void clear() {
        mBooks.clear();
    }

    public void add(KiiBook book) {
        mBooks.add(book);
        Log.d(TAG, "add: " + mBooks.size());
    }

    @Nullable
    public KiiBook getLastItem() {
        if (mBooks.isEmpty()) {
            return null;
        }
        return mBooks.get(getCount() - 1);
    }

    @Override
    public int getCount() {
        return mBooks.size();
    }

    @Override
    public Object getItem(int position) {
        return mBooks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // ViewHolder パターンというのがあって、これによってパフォーマンスの向上が見込めます
        // http://outofmem.hatenablog.com/entry/2014/10/29/040510
        if (convertView == null) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // row というレイアウトの名前は、何も伝えていません。
            // また、あたかもリスト表示の1アイテムであるかのような印象を与えます。(実際にはGrid)
            // item_book とかいいかと思います。
            View itemLayout = inflater.inflate(R.layout.row, null);

            // ID の命名もう少し考えた方がいいです。
            // 辞書引いてでも、しっくりくる名前を考えるべきです。Title って聞いたら、
            // 文字列を連想して、TextView かと思ってしまいます。
            // ここでは表紙の画像なので、imgCovert とかがいいかと。
            ImageView ivCover = (ImageView) itemLayout.findViewById(R.id.imgTitle);

            TextView tvTitle = (TextView) itemLayout.findViewById(R.id.rowTextTitle);
            TextView tvAuthor = (TextView) itemLayout.findViewById(R.id.rowTextAuthor);
            itemLayout.setTag(new ViewHolder(ivCover, tvTitle, tvAuthor));

            convertView = itemLayout;
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();

        final KiiBook book = mBooks.get(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(book);
            }
        });

        String coverUrl = book.get(KiiBook.IMAGE_URL);

        // http://square.github.io/picasso/
        Picasso.with(convertView.getContext())
                .load(coverUrl)
                .into(holder.ivCover);

        String title = book.get(KiiBook.TITLE);
        String author = book.get(KiiBook.AUTHOR);

        holder.tvTitle.setText(title);
        holder.tvAuthor.setText(author);

        return convertView;
    }

    public interface BookItemClickListener {
        void onClick(KiiBook book);
    }

    private static class ViewHolder {
        public ImageView ivCover;
        public TextView tvTitle;
        public TextView tvAuthor;

        public ViewHolder(ImageView ivCover, TextView tvTitle, TextView tvAuthor) {
            this.ivCover = ivCover;
            this.tvTitle = tvTitle;
            this.tvAuthor = tvAuthor;
        }
    }
}
