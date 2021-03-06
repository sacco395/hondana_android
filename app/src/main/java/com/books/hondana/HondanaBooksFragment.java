package com.books.hondana;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.Toast;

import com.books.hondana.connection.KiiBookConnection;
import com.books.hondana.connection.KiiGenreConnection;
import com.books.hondana.connection.KiiObjectListCallback;
import com.books.hondana.model.Book;
import com.books.hondana.model.Genre;
import com.books.hondana.activity.BookInfoActivity;
import com.books.hondana.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class HondanaBooksFragment extends Fragment {

    private static final String TAG = HondanaBooksFragment.class.getSimpleName();

    private static final int LOAD_BOOKS_COUNT_LIMIT = 20;

    private Genre mGenre;

    private HondanaBookAdapter mGridAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private KiiGenreConnection connection;

    // ロード中を示すフラグ。無限ロードを防ぐため。
    private boolean mIsLoading = false;

    public HondanaBooksFragment() {}

    public static HondanaBooksFragment newInstance(Genre genre) {
        HondanaBooksFragment fragment = new HondanaBooksFragment();
        Bundle args = new Bundle();
        args.putSerializable(Genre.class.getSimpleName(), genre);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGenre = (Genre) getArguments().getSerializable(Genre.class.getSimpleName());
        }

        connection = new KiiGenreConnection(mGenre);

        mGridAdapter = new HondanaBookAdapter(new ArrayList<Book>(), new HondanaBookAdapter.BookItemClickListener() {
            @Override
            public void onClick(Book book) {
                Intent intent = new Intent(getContext(), BookInfoActivity.class);
                intent.putExtra(Book.class.getSimpleName(), book);

                LogUtil.d(TAG, "onItemClick: " + book);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kii_books, container, false);

        // Inflate the layout for this fragment
        GridView mGridView = (GridView) view.findViewById(R.id.gridView);
        mGridView.setAdapter(mGridAdapter);

        // SwipeRefreshLayoutの設定
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);

        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mIsLoading) {
                    Log.d(TAG, "onScroll: isLoading");
                    return;
                }

                if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    mIsLoading = true;
                    kickLoadHondanaBooks(false);
                }
            }
        });
        return view;
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refresh();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        kickLoadHondanaBooks(true);
        mGridAdapter.clear();
    }

    /**
     * ホンダナへの問い合わせ
     * @param from ページネーションのための、作成日時の UNIX 時間 (ミリ秒)
     *             この時間以前に作成された KiiBook を取ってくる
     *             0 を指定したら、リフレッシュ
     */
    private void kickLoadHondanaBooks(boolean refresh) {
        mIsLoading = true;

        connection.fetch(0, refresh, new KiiObjectListCallback<Book>() {
            @Override
            public void success(int token, List<Book> result) {
                Log.d(TAG, "success: size=" + result.size());
                finishLoadingView();
                mGridAdapter.add(result);
                mGridAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(@Nullable Exception e) {
                finishLoadingView();
                Toast.makeText(getActivity(), "エラーが発生しました", Toast.LENGTH_LONG).show();
                LogUtil.w(TAG, e);
            }
        });
    }

    private void finishLoadingView() {
        mSwipeRefreshLayout.setRefreshing(false);
        mIsLoading = false;
    }
}
