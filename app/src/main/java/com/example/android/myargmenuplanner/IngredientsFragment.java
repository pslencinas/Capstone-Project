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

    private IngrWeekAdapter mIngrWeekAdapter;
    private RecyclerView mRecyclerView;

    private ShareActionProvider mShareActionProvider;

    private Uri mUri;

    private static final int DETAIL_LOADER = 0;

    private static final String[] FOOD_COLUMNS = {

            FoodContract.FoodEntry.TABLE_NAME + "." + FoodContract.FoodEntry._ID,

            FoodContract.FoodEntry.COLUMN_ID,
            FoodContract.FoodEntry.COLUMN_TITLE,
            FoodContract.FoodEntry.COLUMN_IMAGE_ID,


    };

    private static final String[] INGR_COLUMNS = {

            FoodContract.IngrEntry.TABLE_NAME + "." + FoodContract.IngrEntry._ID,

            FoodContract.IngrEntry.COLUMN_ID_FOOD,
            FoodContract.IngrEntry.COLUMN_NAME,
            FoodContract.IngrEntry.COLUMN_QTY,
            FoodContract.IngrEntry.COLUMN_UNIT

    };

    private static final String[] MENU_COLUMNS = {

            FoodContract.MenuEntry.TABLE_NAME + "." + FoodContract.MenuEntry._ID,

            FoodContract.MenuEntry.COLUMN_DATE,
            FoodContract.MenuEntry.COLUMN_ID_LUNCH,
            FoodContract.MenuEntry.COLUMN_ID_DINNER


    };

    static final int COL_DATE = 1;
    static final int COL_ID_LUNCH = 2;
    static final int COL_ID_DINNER = 3;

    public static final int COL_NAME = 2;
    public static final int COL_QTY = 3;
    public static final int COL_UNIT = 4;



    private ImageView mImageView;
    private TextView mTitleView;
    private TextView mDescriptionView;

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


        Uri mUri = FoodContract.MenuEntry.CONTENT_URI;
        String sMenuByDate = FoodContract.MenuEntry.COLUMN_DATE + " BETWEEN ? AND ?";

        Cursor mCursor= getActivity().getContentResolver().query(
                mUri,
                MENU_COLUMNS,                           //Columnas a mostrar
                sMenuByDate,                           // selection = Condicion del WHERE
                new String []{TabFragmentTW.initDate, TabFragmentTW.endDate},        // selectionArgs = arg del WHERE
                null
        );

        while(mCursor.moveToNext()){
            Log.i(LOG_TAG, "ID_LUNCH: "+mCursor.getShort(COL_ID_LUNCH));
            Log.i(LOG_TAG, "ID_DINNER: "+mCursor.getShort(COL_ID_DINNER));

        }


        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    void onChanged( ) {

        //getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        mUri = FoodContract.IngrEntry.CONTENT_URI;

        return new CursorLoader(
                getActivity(),
                mUri,
                INGR_COLUMNS,
                null,
                null,
                null
        );


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        if (data != null) {

            mIngrWeekAdapter.swapCursor(data);


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }




}