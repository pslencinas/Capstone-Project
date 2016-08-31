package com.example.android.myargmenuplanner;


import android.app.Activity;
import android.content.Context;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.R.attr.data;
import static android.R.attr.end;
import static android.R.attr.id;

public class TabFragmentTW extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = TabFragmentTW.class.getSimpleName();
    static final String DETAIL_URI = "URI";
    private Uri mUri;

    public static final String ACTION_DATA_UPDATED =
            "com.example.android.myargmenuplanner.ACTION_DATA_UPDATED";

    public static String initDate, endDate, dateNow;
    private MenuAdapter mMenuAdapter;
    private FoodsAdapter mFoodsAdapter;
    private RecyclerView mRecyclerView;
    private static final int DETAIL_LOADER = 0;
    private AdView mAdView;

    private static final String[] MENU_COLUMNS = {

            MenuEntry.TABLE_NAME + "." + MenuEntry._ID,

            MenuEntry.COLUMN_DATE,
            MenuEntry.COLUMN_LUNCH,
            MenuEntry.COLUMN_DINNER


    };

    static final int COL_DATE = 1;
    static final int COL_LUNCH = 2;
    static final int COL_DINNER = 3;


    public TabFragmentTW() {
        // Required empty public constructor
    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelectedMenu(String type, String id, String date, MenuAdapter.MenuAdapterViewHolder vh);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View rootView = inflater.inflate(R.layout.fragment_rv_menu, container, false);

        mAdView = (AdView) rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_menu);
        // Set the layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mMenuAdapter = new MenuAdapter(getActivity(), new MenuAdapter.MenuAdapterOnClickHandler() {
            @Override
            public void onClick(String type, String id, String date, MenuAdapter.MenuAdapterViewHolder vh) {

                Log.i(LOG_TAG, "ClickOn TabFragmentTW");

                ((TabFragmentTW.Callback) getActivity())
                        .onItemSelectedMenu(type, id, date, vh );

                //Toast.makeText(getActivity(),"Click on: "+id, Toast.LENGTH_SHORT).show();
            }
        });



        mRecyclerView.setAdapter(mMenuAdapter);

        return rootView;
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        dateNow = df.format(cal.getTime());
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        initDate = "";
        endDate = "";

        if(dayofweek == 1){
            initDate = dateNow;
            cal.add(Calendar.DATE, 6);
            endDate = df.format(cal.getTime());
        }else
            if(dayofweek == 7){
                endDate = dateNow;
                cal.add(Calendar.DATE, -6);
                initDate = df.format(cal.getTime());
            }else{
                int shift = 7 - dayofweek;
                cal.add(Calendar.DATE, shift);
                endDate = df.format(cal.getTime());
                cal.add(Calendar.DATE, -6);
                initDate = df.format(cal.getTime());
            }

        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void updateWidgets() {
        Context context = getActivity();
        // Setting the package ensures that only components in our app will receive the broadcast
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(context.getPackageName());
        context.sendBroadcast(dataUpdatedIntent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

//        Log.i(LOG_TAG, "Dentro de onCreateLoader");

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
        updateWidgets();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
