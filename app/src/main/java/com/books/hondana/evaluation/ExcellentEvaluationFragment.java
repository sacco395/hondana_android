package com.books.hondana.evaluation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.books.hondana.R;
import com.books.hondana.activity.UserpageActivity;
import com.books.hondana.connection.KiiObjectListCallback;
import com.books.hondana.connection.KiiRequestConnection;
import com.books.hondana.model.Request;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiUser;

import java.util.ArrayList;
import java.util.List;


public class ExcellentEvaluationFragment extends Fragment {

    private static final String TAG = ExcellentEvaluationFragment.class.getSimpleName ();

    EvaluationListViewAdapter mListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate (R.layout.fragment_passed_books, container, false);

        mListAdapter = new EvaluationListViewAdapter (new ArrayList<Request> (), new EvaluationListViewAdapter.EvaluationClickListener () {
            @Override
            public void onClick(Request request) {
                Intent intent = new Intent (getContext (), UserpageActivity.class);
                intent.putExtra (Request.class.getSimpleName (), request);

                LogUtil.d (TAG, "onItemClick: " + request);
                startActivity (intent);
            }
        });

        ListView mListView = (ListView) view.findViewById (R.id.list);
        assert mListView != null;
        mListView.setAdapter (mListAdapter);


        KiiUser kiiUser = KiiUser.getCurrentUser ();
        String userId = kiiUser.getID ();
        LogUtil.d (TAG, "userID = " + userId);

        KiiRequestConnection.fetchEvaluatedExcellent(userId, new KiiObjectListCallback<Request> () {
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
