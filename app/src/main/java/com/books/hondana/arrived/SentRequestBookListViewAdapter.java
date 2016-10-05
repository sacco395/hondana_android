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
import java.util.Objects;

public class SentRequestBookListViewAdapter extends BaseAdapter {

    private static final String TAG = SentRequestBookListViewAdapter.class.getSimpleName();

    private ArrayList<Request> mRequests;
    private SentRequestBookClickListener mListener;

    public SentRequestBookListViewAdapter(ArrayList<Request> requests, SentRequestBookClickListener listener) {
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
            TextView tvDate = (TextView) itemLayout.findViewById(R.id.tv_date);
            itemLayout.setTag(new ViewHolder(ivCover, tvTitle, tvDate));

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

        String requested_date = request.getRequestedDate();
        String sent_date = request.getSentDate();
        if (!Objects.equals (sent_date, "")) {
            holder.tvDate.setText(sent_date + "に本が発送されました\n本が届いたら相手の評価をしましょう");
        } else {
            holder.tvDate.setText(requested_date);
        }

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

    public interface SentRequestBookClickListener {
        void onClick(Request request);
    }

    public static class ViewHolder {
        public ImageView ivCover;
        public TextView tvTitle;
        public TextView tvDate;

        public ViewHolder(ImageView ivCover, TextView tvTitle, TextView tvDate) {
            this.ivCover = ivCover;
            this.tvTitle = tvTitle;
            this.tvDate = tvDate;
        }
    }
}