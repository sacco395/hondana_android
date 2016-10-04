package com.books.hondana.exhibited;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.books.hondana.R;
import com.books.hondana.activity.SendBookActivity;
import com.books.hondana.connection.KiiBookConnection;
import com.books.hondana.connection.KiiObjectCallback;
import com.books.hondana.connection.KiiObjectListCallback;
import com.books.hondana.connection.KiiRequestConnection;
import com.books.hondana.model.Book;
import com.books.hondana.model.Request;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiUser;

import java.util.ArrayList;
import java.util.List;

public class ReceivedRequestBookFragment extends Fragment {

    private static final String TAG = ReceivedRequestBookFragment.class.getSimpleName();

    ReceivedRequestBookListViewAdapter mListAdapter;

//    private Book mBook;
//
//    private KiiUserBookConnection connection;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mBook = (Book) getArguments().getSerializable(Book.class.getSimpleName());
//        }
//
//        connection = new KiiUserBookConnection(mBook);
//
//        mListAdapter = new ExhibitedBookListViewAdapter (new ArrayList<Book>(), new ExhibitedBookListViewAdapter.ExhibitedBookClickListener() {
//            @Override
//            public void onClick(Book book) {
//                Intent intent = new Intent(getContext(), BookInfoActivity.class);
//                intent.putExtra(Book.class.getSimpleName(), book);
//
//                LogUtil.d(TAG, "onItemClick: " + book);
//                startActivity(intent);
//            }
//        });
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate (R.layout.fragment_request_books, container, false);

        final KiiUser user = KiiUser.getCurrentUser();
        assert user != null;
        final String serverUserId = user.getID ();
        LogUtil.d (TAG, "serverUserId = " + serverUserId);


        mListAdapter = new ReceivedRequestBookListViewAdapter (new ArrayList<Book> (), new ReceivedRequestBookListViewAdapter.ReceivedRequestBookClickListener () {
            @Override
            public void onClick(Book book) {
                LogUtil.d (TAG, "onItemClick: " + book);
                String bookId = book.getId ();
                KiiRequestConnection.fetchByBookId (bookId, serverUserId, new KiiObjectCallback<Request> () {
                    @Override
                    public void success(int token, Request request) {
                        startActivity(SendBookActivity.createIntent(getContext (), request));
                    }

                    @Override
                    public void failure(Exception e) {
                        LogUtil.w (TAG, e);
                    }
                });
            }
        });

        ListView mListView = (ListView) view.findViewById (R.id.list_view);
        assert mListView != null;
        mListView.setAdapter (mListAdapter);


        KiiBookConnection.fetchReceivedRequestBooks(serverUserId, new KiiObjectListCallback<Book> () {
            @Override
            public void success(int token, List<Book> result) {
                LogUtil.d (TAG, "success: size=" + result.size ());
                mListAdapter.add(result);
                mListAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(@Nullable Exception e) {
                LogUtil.w (TAG, e);
            }
        });
        return view;
    }
}