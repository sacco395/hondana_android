package com.books.hondana;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.books.hondana.activity.SendBookActivity;
import com.books.hondana.connection.KiiObjectListCallback;
import com.books.hondana.connection.KiiRequestConnection;
import com.books.hondana.model.Book;
import com.books.hondana.model.Request;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiUser;

import java.util.ArrayList;
import java.util.List;

public class ReceivedRequestFragment extends Fragment {

    private static final String TAG = ReceivedRequestFragment.class.getSimpleName();

    PRBookListViewAdapter mListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_books, container, false);


        mListAdapter = new PRBookListViewAdapter(new ArrayList<Book>(), new PRBookListViewAdapter.BookItemClickListener() {
            @Override
            public void onClick(Book book) {
                Intent intent = new Intent(getContext(), SendBookActivity.class);
                intent.putExtra(Book.class.getSimpleName(), book);

                LogUtil.d(TAG, "onItemClick: " + book);
                startActivity(intent);
            }
        });

        ListView mListView = (ListView) view.findViewById(R.id.list_view);
        mListView.setAdapter(mListAdapter);


        KiiUser kiiUser = KiiUser.getCurrentUser();
        String userId = kiiUser.getID();
        LogUtil.d(TAG, "userID = " + userId);

        KiiRequestConnection.fetchRequestsByOthers(userId, new KiiObjectListCallback<Request>() {
            @Override
            public void success(int token, List<Request> result) {
                Log.d(TAG, "success: size=" + result.size());
            }

            @Override
            public void failure(@Nullable Exception e) {
                LogUtil.w(TAG, e);
            }
        });
        return view;
    }

}


