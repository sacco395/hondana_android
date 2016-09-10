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

import com.books.hondana.Connection.KiiBookConnection;
import com.books.hondana.Connection.KiiMemberConnection;
import com.books.hondana.Connection.KiiObjectCallback;
import com.books.hondana.Model.book.Book;
import com.books.hondana.Model.Genre;
import com.books.hondana.Model.kii.KiiBook;
import com.books.hondana.Model.kii.KiiMember;
import com.books.hondana.activity.BookInfoActivity;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;

import java.util.ArrayList;
import java.util.List;

public class HondanaBooksFragment extends Fragment {

    private static final String TAG = HondanaBooksFragment.class.getSimpleName();

    private static final int LOAD_BOOKS_COUNT_LIMIT = 20;

    private GridView mGridView;
    private HondanaBookAdapter mGridAdapter;

    private KiiBookConnection mConnection;

    private SwipeRefreshLayout mSwipeRefreshLayout;

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
            Genre genre = (Genre) getArguments().getSerializable(Genre.class.getSimpleName());
            mConnection = new KiiBookConnection(genre);
        }

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
        mGridView = (GridView) view.findViewById(R.id.gridView);
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
                    Book last = mGridAdapter.getLastItem();
                    if (last == null) {
                        kickLoadHondanaBooks(0);
                        return;
                    }
                    // FIXME: 9/9/16 Kii これかなりイケてないのでなんかいい感じにしたい。
                    if (last.createdAt <= 1470909532550L) {
                        // サーバにこれ以上本がない
                        return;
                    }
                    kickLoadHondanaBooks(last.createdAt);
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
        kickLoadHondanaBooks(0);
        mGridAdapter.clear();
    }

    /**
     * ホンダナへの問い合わせ
     * @param from ページネーションのための、作成日時の UNIX 時間 (ミリ秒)
     *             この時間以前に作成された KiiBook を取ってくる
     *             0 を指定したら、リフレッシュ
     */
    private void kickLoadHondanaBooks(long from) {
        mIsLoading = true;

        if (mConnection == null) {
            mConnection = new KiiBookConnection(Genre.ALL);
        }

        mConnection.fetch(from, LOAD_BOOKS_COUNT_LIMIT, new KiiObjectCallback() {
            @Override
            public void success(int token, List<KiiObject> result) {
                finishLoadingView();
                for (KiiObject kiiObject : result) {
                    mGridAdapter.add(new Book(kiiObject));
                }
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
