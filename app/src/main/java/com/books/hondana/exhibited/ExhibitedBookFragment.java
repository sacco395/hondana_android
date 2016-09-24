package com.books.hondana.exhibited;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.books.hondana.PRBookListViewAdapter;
import com.books.hondana.R;
import com.books.hondana.activity.SendBookActivity;
import com.books.hondana.model.Book;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiUser;

import java.util.ArrayList;

public class ExhibitedBookFragment extends Fragment {

    private static final String TAG = ExhibitedBookFragment.class.getSimpleName();
    private static final String KEY_BOOK_LIST = "bookList";

    PRBookListViewAdapter mListAdapter;

    private ArrayList<Book> mBooks;

    public static ExhibitedBookFragment newInstance(ArrayList<Book> bookList) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_BOOK_LIST, bookList);
        ExhibitedBookFragment fragment = new ExhibitedBookFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBooks = getArguments().getParcelableArrayList(KEY_BOOK_LIST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_books, container, false);


        mListAdapter = new PRBookListViewAdapter(mBooks, new PRBookListViewAdapter.BookItemClickListener() {
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

//        KiiBookConnection.fetchPostedBooks(userId, new KiiObjectListCallback<Book>(){
//            @Override
//            public void success(int token, List<Book> result) {
//                Log.d(TAG, "success: size=" + result.size());
//                mListAdapter.add(result);
//                mListAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void failure(@Nullable Exception e) {
//                LogUtil.w(TAG, e);
//            }
//        });
        return view;
    }

}


