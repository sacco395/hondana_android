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

        String requested_date = request.getRequestedDate();
        String sent_date = request.getSentDate();
        if (!Objects.equals (sent_date, "")) {
            holder.tvDate.setText(sent_date + "に本が発送されました\n本が届いたら相手の評価をしましょう");
        } else {
            holder.tvDate.setText(requested_date + "に本にリクエストしました");
        }

        String requestBookId = request.getBookId();
        final View finalConvertView = convertView;
        KiiBookConnection.fetchByBookId (requestBookId, new KiiObjectCallback<Book> () {
            @Override
            public void success(int token, Book book) {
                BookInfo info = book.getInfo();
                String coverUrl = info.getImageUrl();

                Picasso.with(finalConvertView.getContext())
                        .load(coverUrl)
                        .into(holder.ivCover);
            }

            @Override
            public void failure(Exception e) {

            }
        });


//        // http://square.github.io/picasso/
//        Picasso.with(convertView.getContext())
//                .load(coverUrl)
//                .into(holder.ivCover);
//
//        holder.tvTitle.setText("「" + info.getTitle() + "」が発送されました");

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

    public interface SentRequestBookClickListener {
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