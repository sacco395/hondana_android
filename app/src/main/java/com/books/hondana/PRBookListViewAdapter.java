package com.books.hondana;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.books.hondana.model.Book;
import com.books.hondana.model.BookInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PRBookListViewAdapter extends BaseAdapter {

    private static final String TAG = PRBookListViewAdapter.class.getSimpleName();

    private ArrayList<Book> mBooks;
    private BookItemClickListener mListener;

    public PRBookListViewAdapter(ArrayList<Book> books, BookItemClickListener listener) {
        this.mBooks = books;
        this.mListener = listener;
    }

    public void add(List<Book> books) {
        mBooks.addAll(books);
    }

    public void add(Book book) {
        mBooks.add(book);
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
    public View getView(int position, View convertView, final ViewGroup parent) {

        // ViewHolder パターンというのがあって、これによってパフォーマンスの向上が見込めます
        // http://outofmem.hatenablog.com/entry/2014/10/29/040510
        if (convertView == null) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // item_book というレイアウトの名前は、何も伝えていません。
            // また、あたかもリスト表示の1アイテムであるかのような印象を与えます。(実際にはGrid)
            // item_book とかいいかと思います。
            View itemLayout = inflater.inflate(R.layout.part_my_book_list, null);

            // ID の命名もう少し考えた方がいいです。
            // 辞書引いてでも、しっくりくる名前を考えるべきです。Title って聞いたら、
            // 文字列を連想して、TextView かと思ってしまいます。
            // ここでは表紙の画像なので、imgCovert とかがいいかと。
            ImageView ivCover = (ImageView) itemLayout.findViewById(R.id.iv_BookImg);
            TextView tvTitle = (TextView) itemLayout.findViewById(R.id.tv_BookTitle);
            TextView tvAuthor = (TextView) itemLayout.findViewById(R.id.tv_BookAuthor);
            itemLayout.setTag(new ViewHolder(ivCover, tvTitle, tvAuthor));

            convertView = itemLayout;
        }

        final ViewHolder holder = (ViewHolder) convertView.getTag();

        final Book book = mBooks.get(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(book);
            }
        });

        BookInfo info = book.getInfo();
        String coverUrl = info.getImageUrl();

        // http://square.github.io/picasso/
        Picasso.with(convertView.getContext())
                .load(coverUrl)
                .into(holder.ivCover);

        holder.tvTitle.setText(info.getTitle());
        holder.tvAuthor.setText(info.getAuthor());


        return convertView;
    }

    public interface BookItemClickListener {
        void onClick(Book book);
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

