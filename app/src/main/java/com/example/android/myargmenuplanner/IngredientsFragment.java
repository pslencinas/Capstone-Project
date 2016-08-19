package com.example.android.myargmenuplanner;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.myargmenuplanner.data.FoodContract;
import com.squareup.picasso.Picasso;



public class IngredientsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = FoodsFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";
    static String TYPE_OF_MEAL;
    static String DATE_OF_MEAL;
    public static String mDate;
    private static int mLunch;
    private static int mDinner;
    private IngrWeekAdapter mIngrWeekAdapter;
    private RecyclerView mRecyclerView;

    private ShareActionProvider mShareActionProvider;

    private Uri mUri;

    private static final int DETAIL_LOADER = 0;


    private static final String[] LIST_INGR_COLUMNS = {

            FoodContract.MenuEntry.TABLE_NAME + "." + FoodContract.MenuEntry.COLUMN_DATE,
            FoodContract.IngrEntry.TABLE_NAME + "." + FoodContract.IngrEntry.COLUMN_NAME,
            FoodContract.IngrEntry.TABLE_NAME + "." + FoodContract.IngrEntry.COLUMN_QTY,
            FoodContract.IngrEntry.TABLE_NAME + "." + FoodContract.IngrEntry.COLUMN_UNIT




    };


    public static final int COL_DATE = 0;
    public static final int COL_NAME = 1;
    public static final int COL_QTY = 2;
    public static final int COL_UNIT = 3;



    private TextView mEmptyList;


    public static ListView mListView1;

    public static String title;
    public static String description;
    public static String food_id;
    public static String image_id;

    public IngredientsFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        TYPE_OF_MEAL = "";
        Bundle arguments = getArguments();
        if (arguments != null) {
            //mUri = arguments.getParcelable(DetailFoodFragment.DETAIL_URI);
            TYPE_OF_MEAL = arguments.getString("TYPE");
            DATE_OF_MEAL = arguments.getString("DATE");

        }


        View rootView = inflater.inflate(R.layout.fragment_ingr_list, container, false);
        mEmptyList = (TextView) rootView.findViewById(R.id.empty_list);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_ingr);
        // Set the layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mIngrWeekAdapter = new IngrWeekAdapter();

        // specify an adapter (see also next example)
        mRecyclerView.setAdapter(mIngrWeekAdapter);

        return rootView;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        getLoaderManager().initLoader(DETAIL_LOADER, null, this);

        super.onActivityCreated(savedInstanceState);
    }

    void onChanged( ) {

        //getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        Uri mUri = FoodContract.MenuEntry.buildIngrByWeekUri();
        Log.i(LOG_TAG, "URI de IngredientsFragmnet: "+mUri);

        String sMenuByDate = FoodContract.MenuEntry.COLUMN_DATE + " BETWEEN ? AND ?";


        return new CursorLoader(
                getActivity(),
                mUri,
                LIST_INGR_COLUMNS,
                sMenuByDate,
                new String []{TabFragmentTW.initDate, TabFragmentTW.endDate},
                null
        );


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        if (data != null && data.moveToFirst()) {
            mEmptyList.setText("");
            mIngrWeekAdapter.swapCursor(data);


        }else{
            mEmptyList.setText(R.string.empty_list);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }




}