package com.books.hondana;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.books.hondana.Model.Genre;
import com.books.hondana.Model.KiiBook;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/8/16.
 */
public class KiiBooksAdapter extends BaseAdapter {

    private Genre mGenre;

    private BookItemClickListener mListener;

    private List<KiiBook> mBooks;

    public KiiBooksAdapter(Genre genre, BookItemClickListener listener) {
        mGenre = genre;
        mListener = listener;
        mBooks = new ArrayList<>();
    }

    public void load() {

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

        // ViewHolder pattern (http://outofmem.hatenablog.com/entry/2014/10/29/040510)
        if (convertView == null) {
            Context context = parent.getContext();

            // row というレイアウトの名前は、何も伝えていません。
            // また、あたかもリスト表示の1アイテムであるかのような印象を与えます。(実際にはGrid)
            // item_book とかいいかと思います。
            View layout = View.inflate(context, R.layout.row, parent);

            // ID の命名もう少し考えた方がいいです。imgCovert とか。
            // 辞書引いてでも、しっくりくる名前を考えるべきです。
            ImageView ivCover = (ImageView) layout.findViewById(R.id.imgTitle);

            TextView tvTitle = (TextView) layout.findViewById(R.id.rowTextTitle);
            TextView tvAuthor = (TextView) layout.findViewById(R.id.rowTextAuthor);
            layout.setTag(new ViewHolder(ivCover, tvTitle, tvAuthor));

            convertView = layout;
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
