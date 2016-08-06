package com.example.android.myargmenuplanner;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.myargmenuplanner.data.FoodContract;
import com.example.android.myargmenuplanner.data.FoodContract.MenuEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.R.attr.id;

public class TabFragmentNW extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = TabFragmentTW.class.getSimpleName();
    static final String DETAIL_URI = "URI";
    private Uri mUri;

    private String initDate, endDate;
    private MenuAdapter mMenuAdapter;
    private RecyclerView mRecyclerView;
    private static final int DETAIL_LOADER = 0;

    private static final String[] MENU_COLUMNS = {

            MenuEntry.TABLE_NAME + "." + MenuEntry._ID,

            MenuEntry.COLUMN_DATE,
            MenuEntry.COLUMN_LUNCH,
            MenuEntry.COLUMN_DINNER,
            MenuEntry.COLUMN_WEEK,

    };

    static final int COL_DATE = 1;
    static final int COL_LUNCH = 2;
    static final int COL_DINNER = 3;


    public TabFragmentNW() {
        // Required empty public constructor
    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri foodUri, Uri menuUri, MenuAdapter.MenuAdapterViewHolder vh);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View rootView = inflater.inflate(R.layout.fragment_rv_menu, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_menu);
        // Set the layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mMenuAdapter = new MenuAdapter(getActivity(), new MenuAdapter.MenuAdapterOnClickHandler() {
            @Override
            public void onClick(String id, MenuAdapter.MenuAdapterViewHolder vh) {

//                ((TabFragmentTW.Callback) getActivity())
//                        .onItemSelected(FoodContract.FoodEntry.buildFoodUri(id)
//                                ,FoodContract.IngrEntry.buildIngrByFoodUri(id), vh );

                //Toast.makeText(getActivity(),"Click on: "+id, Toast.LENGTH_SHORT).show();
            }
        });
        // specify an adapter (see also next example)
        mRecyclerView.setAdapter(mMenuAdapter);

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);

        int shift = 7 - dayofweek + 1;
        cal.add(Calendar.DATE, shift);
        initDate = df.format(cal.getTime());
        cal.add(Calendar.DATE, 6);
        endDate = df.format(cal.getTime());

        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.i(LOG_TAG, "Dentro de onCreateLoader");

        mUri = MenuEntry.CONTENT_URI;
        String sMenuByDate = FoodContract.MenuEntry.COLUMN_DATE + " BETWEEN ? AND ?";

        Log.i(LOG_TAG, "mURI: "+mUri);

        return new CursorLoader(
                getActivity(),
                mUri,                                   // URI
                MENU_COLUMNS,                           // projection = Columnas a mostrar
                sMenuByDate,                           // selection = Condicion del WHERE
                new String []{initDate, endDate},        // selectionArgs = arg del WHERE
                null
        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        if (data != null) {

            mMenuAdapter.swapCursor(data);


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
