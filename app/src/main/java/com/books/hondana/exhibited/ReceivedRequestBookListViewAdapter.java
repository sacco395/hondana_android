package com.books.hondana.exhibited;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.books.hondana.R;
import com.books.hondana.model.Book;
import com.books.hondana.model.BookInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ReceivedRequestBookListViewAdapter extends BaseAdapter {

    private static final String TAG = ReceivedRequestBookListViewAdapter.class.getSimpleName();

    private ArrayList<Book> mBooks;
    private ReceivedRequestBookClickListener mListener;

    public ReceivedRequestBookListViewAdapter(ArrayList<Book> books, ReceivedRequestBookClickListener listener) {
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
        if (convertView == null) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View itemLayout = inflater.inflate(R.layout.part_todo_list, null);

            ImageView ivCover = (ImageView) itemLayout.findViewById(R.id.iv_BookImg);
            TextView tvTitle = (TextView) itemLayout.findViewById(R.id.tv_BookTitle);
//            TextView tvMessage = (TextView) itemLayout.findViewById(R.id.tv_message);
//            TextView tvDate = (TextView) itemLayout.findViewById(R.id.tv_date);
//            itemLayout.setTag(new ViewHolder(ivCover, tvTitle, tvMessage, tvDate));
            itemLayout.setTag(new ViewHolder(ivCover, tvTitle));

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

        holder.tvTitle.setText("「" + info.getTitle() + "」の発送をしましょう");

//        String bookId = book.getId ();
//        LogUtil.d(TAG, "bookId: " + bookId);
//        KiiRequestConnection.fetchRequestsByOthers(bookId, new KiiObjectCallback<Request> () {
//            @Override
//            public void success(int token, Request request) {
//                final String requestDate = request.getRequestedDate ();
//                LogUtil.d (TAG, "requestDate: " + requestDate);
//                holder.tvDate.setText (requestDate);
//            }
//
//            @Override
//            public void failure(Exception e) {
//                LogUtil.e(TAG, "failure: ", e);
//            }
//        });
//
//        KiiMemberConnection.fetch(clientId, new KiiObjectCallback<Member> () {
//            @Override
//            public void success(int token, Member member) {
//                final String name = member.getName();
//                LogUtil.d(TAG, "name: " + name);
//                holder.tvMessage.setText(name + "さんがあなたにリクエストをしました。");
//            }
//
//            @Override
//            public void failure(Exception e) {
//                LogUtil.e(TAG, "failure: ", e);
//            }
//        });

        return convertView;
    }

    public interface ReceivedRequestBookClickListener {
        void onClick(Book book);
    }

    public static class ViewHolder {
        public ImageView ivCover;
        public TextView tvTitle;
//        public TextView tvMessage;
//        public TextView tvDate;

//        public ViewHolder(ImageView ivCover, TextView tvTitle, TextView tvMessage, TextView tvDate) {
public ViewHolder(ImageView ivCover, TextView tvTitle) {
            this.ivCover = ivCover;
            this.tvTitle = tvTitle;
//            this.tvMessage = tvMessage;
//            this.tvDate = tvDate;
        }
    }
}