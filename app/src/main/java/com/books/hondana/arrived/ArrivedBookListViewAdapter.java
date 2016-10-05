package com.books.hondana.arrived;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.books.hondana.R;
import com.books.hondana.connection.KiiBookConnection;
import com.books.hondana.connection.KiiObjectCallback;
import com.books.hondana.model.Book;
import com.books.hondana.model.BookInfo;
import com.books.hondana.model.Request;
import com.books.hondana.util.LogUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ArrivedBookListViewAdapter extends BaseAdapter {

    private static final String TAG = ArrivedBookListViewAdapter.class.getSimpleName();

    private ArrayList<Request> mRequests;
    private ArrivedBookClickListener mListener;

    public ArrivedBookListViewAdapter(ArrayList<Request> requests, ArrivedBookClickListener listener) {
        this.mRequests = requests;
        this.mListener = listener;
    }

    public void add(List<Request>requests) {
        mRequests.addAll(requests);
    }

    public void add(Request request) {
        mRequests.add(request);
    }


    @Override
    public int getCount() {
        return mRequests.size();
    }

    @Override
    public Object getItem(int position) {
        return mRequests.get(position);
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

            View itemLayout = inflater.inflate(R.layout.part_todo_evaluate_list, null);

            ImageView ivCover = (ImageView) itemLayout.findViewById(R.id.iv_bookImg);
            TextView tvTitle = (TextView) itemLayout.findViewById(R.id.tv_bookTitle);
            TextView tvMessage = (TextView) itemLayout.findViewById(R.id.tv_message);
            TextView tvDate = (TextView) itemLayout.findViewById(R.id.tv_date);
            itemLayout.setTag(new ViewHolder(ivCover, tvTitle, tvMessage, tvDate));

            convertView = itemLayout;
        }

        final ViewHolder holder = (ViewHolder) convertView.getTag();

        final Request request = mRequests.get(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(request);
            }
        });

        String received_date = request.getReceivedDate();
        holder.tvDate.setText(received_date + "に本を受け取りました");

        String requestBookId = request.getBookId();
        LogUtil.d(TAG,"requestBookId:"+ requestBookId);

        final View finalConvertView = convertView;
        KiiBookConnection.fetchByBookId (requestBookId, new KiiObjectCallback<Book> () {
            @Override
            public void success(int token, Book book) {
                BookInfo info = book.getInfo();
//                String book_title = info.getTitle();
//                holder.tvTitle.setText(book_title + "にリクエストしました");

                String coverUrl = info.getImageUrl();

                Picasso.with(finalConvertView.getContext())
                        .load(coverUrl)
                        .into(holder.ivCover);
            }

            @Override
            public void failure(Exception e) {

            }
        });
        return convertView;
    }

    public interface ArrivedBookClickListener {
        void onClick(Request request);
    }

    public static class ViewHolder {
        public ImageView ivCover;
        public TextView tvTitle;
        public TextView tvMessage;
        public TextView tvDate;

        public ViewHolder(ImageView ivCover, TextView tvTitle, TextView tvMessage, TextView tvDate) {
//public ViewHolder(ImageView ivCover, TextView tvTitle) {
            this.ivCover = ivCover;
            this.tvTitle = tvTitle;
            this.tvMessage = tvMessage;
            this.tvDate = tvDate;
        }
    }
}