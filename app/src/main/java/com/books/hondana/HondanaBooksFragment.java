package com.books.hondana;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.books.hondana.Connection.KiiCloudConnection;
import com.books.hondana.Connection.QueryParamSet;
import com.books.hondana.Model.KiiBook;
import com.books.hondana.Model.KiiCloudBucket;
import com.kii.cloud.storage.KiiObject;
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
    public void onResume() {
        super.onResume();



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

        kickLoadHondanaBooks();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(DATA_LIST,dataLists);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            dataLists = savedInstanceState.getParcelableArrayList(DATA_LIST);
        }
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_kii_books, container, false);
        mGridView = (GridView) v.findViewById(R.id.gridView);
        mGridView.setAdapter(mListAdapter);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
            }
        }
    };
}
