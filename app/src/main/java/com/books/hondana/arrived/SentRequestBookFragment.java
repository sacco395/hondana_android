package com.books.hondana.arrived;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.books.hondana.R;
import com.books.hondana.activity.ReceivedBookActivity;
import com.books.hondana.connection.KiiObjectListCallback;
import com.books.hondana.connection.KiiRequestConnection;
import com.books.hondana.model.Request;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiUser;

import java.util.ArrayList;
import java.util.List;

public class SentRequestBookFragment extends Fragment {

    private static final String TAG = SentRequestBookFragment.class.getSimpleName();

    SentRequestBookListViewAdapter mListAdapter;

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
        final String clientUserId = user.getID ();
        LogUtil.d (TAG, "clientUserId = " + clientUserId);


        mListAdapter = new SentRequestBookListViewAdapter (new ArrayList<Request> (), new SentRequestBookListViewAdapter.SentRequestBookClickListener() {
            @Override
            public void onClick(Request request) {
                LogUtil.d (TAG, "onItemClick: " + request);
                startActivity(ReceivedBookActivity.createIntent(getContext(), request));
            }
        });

        ListView mListView = (ListView) view.findViewById (R.id.list_view);
        assert mListView != null;
        mListView.setAdapter (mListAdapter);


        KiiRequestConnection.fetchSentRequest (clientUserId, new KiiObjectListCallback<Request> () {
                    @Override
                    public void success(int token, List<Request> result) {
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