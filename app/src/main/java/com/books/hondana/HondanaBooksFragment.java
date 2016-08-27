package com.books.hondana;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.books.hondana.Connection.KiiCloudConnection;
import com.books.hondana.Connection.QueryParamSet;
import com.books.hondana.Model.KiiBook;
import com.books.hondana.Model.KiiCloudBucket;
import com.books.hondana.activity.BookInfoActivity;
import com.books.hondana.activity.SelectedBooksActivity;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiObjectCallBack;
import com.kii.cloud.storage.query.KiiQueryResult;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HondanaBooksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HondanaBooksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HondanaBooksFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_QUERYPARAMS = "ARG_QUERYPARAMS";
    public static final String DATA_LIST ="DATA_LIST";

    private int tabPage;
    private QueryParamSet queryParamSet;

    // define the UI elements
    private ProgressDialog mProgress;

    // define the list
    private ArrayList<KiiObject> dataLists = null;
    private GridView mGridView;
    private HondanaBookAdapter mListAdapter;

    private KiiCloudConnection kiiCloudConnection;
    private OnFragmentInteractionListener mListener;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public HondanaBooksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment KiiBooksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HondanaBooksFragment newInstance( int tabPage, QueryParamSet queryParamSet) {
        HondanaBooksFragment fragment = new HondanaBooksFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUERYPARAMS, queryParamSet);
        args.putInt(ARG_PAGE, tabPage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ( getArguments() != null) {
            queryParamSet = (QueryParamSet)getArguments().get(ARG_QUERYPARAMS);
            tabPage = getArguments().getInt(ARG_PAGE);
        }

        // create an empty object adapter
        mListAdapter = new HondanaBookAdapter( getActivity(), new ArrayList<KiiBook>());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(DATA_LIST,dataLists);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kii_books, container, false);

        if (savedInstanceState != null) {
            dataLists = savedInstanceState.getParcelableArrayList(DATA_LIST);
        }


        // SwipeRefreshLayoutの設定
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);

        // GridViewにデータをセットする
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(self, R.array.color, android.R.layout.simple_list_item_1);
        //GridView gridView = (GridView) view.findViewById(R.id.gridView);
        //gridView.setAdapter(adapter);

        return view;
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            // 2秒待機
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 1000);
        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

        // Inflate the layout for this fragment
        mGridView = (GridView) v.findViewById(R.id.gridView);
        mGridView.setAdapter(mListAdapter);
        mGridView.setOnItemClickListener((AdapterView.OnItemClickListener) this);

        // Retrieve an object.
        Uri objUri = Uri.parse("put existing object uri here");
        KiiObject object = KiiObject.createByUri(objUri);

        // Refresh and fetch the latest key-value pairs from Kii Cloud.
        object.refresh(new KiiObjectCallBack() {
            @Override
            public void onRefreshCompleted(int token, KiiObject object, Exception exception) {
                if (exception != null) {
                    // Error handling
                    return;
                }
                // Get key-value pairs.
                int score = object.getInt("score");
                String mode = object.getString("mode");
                boolean premiumUser = object.getBoolean("premiumUser");
            }
        });

        Intent intent = new Intent(this.getContext(), BookInfoActivity.class);
        // clickされたpositionのtextとphotoのID
        String selectedText = titles[position];
        int selectedPhoto = photos[position];
        // インテントにセット
        intent.putExtra("Text", selectedText);
        intent.putExtra("Photo", selectedPhoto);
        // Activity をスイッチする
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        kickLoadHondanaBooks();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    // ホンダナへの問い合わせ
    private void kickLoadHondanaBooks() {

        // default to an empty adapter
        mListAdapter.clear();

        kiiCloudConnection = new KiiCloudConnection( getActivity(), queryParamSet, KiiCloudBucket.BOOKS);

        // show a progress dialog to the user
        mProgress = ProgressDialog.show( getActivity(), "", "Loading...",  true);

        kiiCloudConnection.loadHondanaBooksByGenres(1, searchFinishListener );
    }

    // ホンダナへの問い合わせ完了時の処理
    KiiCloudConnection.SearchFinishListener searchFinishListener = new KiiCloudConnection.SearchFinishListener(){

        @Override
        public void didFinish(int token, KiiQueryResult<KiiObject> result, Exception e) {
            mProgress.dismiss();
            // check for an exception (successful request if e==null)
            if (e == null) {
                // add the objects to the adapter (adding to the listview)
               dataLists = (ArrayList)result.getResult();
                for (KiiObject kObj : dataLists) {
                    mListAdapter.add( new KiiBook(kObj) );
                }
                mListAdapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText( getActivity(), "エラーが発生しました",Toast.LENGTH_LONG).show();
                Log.w("HondanaBooksFragment", e);
            }
        }
    };
}
