package com.books.hondana;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.books.hondana.model.Book;
import com.books.hondana.model.BookCondition;
import com.books.hondana.model.BookInfo;
import com.books.hondana.model.Member;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class HondanaBookAdapter extends BaseAdapter {
    private static final String TAG = HondanaBookAdapter.class.getSimpleName();
    private ArrayList<Book> mBooks;
    private BookItemClickListener mListener;

    private Map<String, Member> mCache;
    public HondanaBookAdapter(ArrayList<Book> books, BookItemClickListener listener) {
        this.mBooks = books;
        this.mListener = listener;
        mCache = new HashMap<>();
    }
    public void clear() {
        mBooks.clear();
    }
    public void add(List<Book> books) {
        mBooks.addAll(books);
    }
    public void add(Book book) {
        mBooks.add(book);
    }
    @Nullable
    public Book getLastItem() {
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
    public View getView(int position, View convertView, final ViewGroup parent) {
        // ViewHolder パターンというのがあって、これによってパフォーマンスの向上が見込めます
        // http://outofmem.hatenablog.com/entry/2014/10/29/040510
        if (convertView == null) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemLayout = inflater.inflate(R.layout.item_book, null);
            // ID の命名もう少し考えた方がいいです。
            // 辞書引いてでも、しっくりくる名前を考えるべきです。Title って聞いたら、
            // 文字列を連想して、TextView かと思ってしまいます。
            // ここでは表紙の画像なので、imgCovert とかがいいかと。
            ImageView ivCover = (ImageView) itemLayout.findViewById(R.id.imgCovert);
            ImageView ivCondition = (ImageView) itemLayout.findViewById(R.id.MainBookConditionIcon);
            TextView tvTitle = (TextView) itemLayout.findViewById(R.id.rowTextTitle);
            TextView tvAuthor = (TextView) itemLayout.findViewById(R.id.rowTextAuthor);
            TextView tvOwnerName = (TextView) itemLayout.findViewById(R.id.book_owner);
            TextView tvBookState = (TextView) itemLayout.findViewById(R.id.book_state);
            itemLayout.setTag(new ViewHolder(ivCover, ivCondition, tvTitle, tvAuthor, tvOwnerName, tvBookState));
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

        BookCondition condition = book.getCondition();
        int resId = condition.getIconDrawableResId();
        if (resId != 0) {
            Context context = convertView.getContext();
            Drawable conditionDrawable = ResourcesCompat.getDrawable(context.getResources(), resId, null);
            holder.ivConditionIcon.setImageDrawable(conditionDrawable);
        }
        holder.tvTitle.setText(info.getTitle());
        holder.tvAuthor.setText(info.getAuthor());
        holder.tvOwnerName.setText(book.getOwnerName());
        holder.tvBookState.setText(book.getStateText());

        return convertView;
    }

    public interface BookItemClickListener {
        void onClick(Book book);
    }
    private static class ViewHolder {
        public ImageView ivCover;
        public ImageView ivConditionIcon;
        public TextView tvTitle;
        public TextView tvAuthor;
        public TextView tvOwnerName;
        public TextView tvBookState;
        public ViewHolder(ImageView ivCover, ImageView ivConditionIcon, TextView tvTitle, TextView tvAuthor, TextView tvOwnerName,TextView tvBookState) {
            this.ivCover = ivCover;
            this.ivConditionIcon = ivConditionIcon;
            this.tvTitle = tvTitle;
            this.tvAuthor = tvAuthor;
            this.tvOwnerName = tvOwnerName;
            this.tvBookState = tvBookState;
        }
    }
}