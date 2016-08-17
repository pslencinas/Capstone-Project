/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.myargmenuplanner;

import android.content.ContentValues;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

//import com.bumptech.glide.Glide;
import com.example.android.myargmenuplanner.data.FoodContract;
import com.example.android.myargmenuplanner.data.FoodContract.IngrEntry;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFoodFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailFoodFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";

    private String mForecast;
    private Uri mUri;
    private Uri mUri_ingr;
    private String mFromMenu;
    private String mDate;
    private String mTypeOfMeal;
    private boolean mTransitionAnimation;
    private IngredientsAdapter mIngrAdapter;
    private RecyclerView mIngrRecyclerView;

    private static final int DETAIL_LOADER_FOOD = 0;
    private static final int DETAIL_LOADER_INGR = 1;

    private static final String[] FOOD_COLUMNS = {

            FoodContract.FoodEntry.TABLE_NAME + "." + FoodContract.FoodEntry._ID,

            FoodContract.FoodEntry.COLUMN_ID,
            FoodContract.FoodEntry.COLUMN_TITLE,
            FoodContract.FoodEntry.COLUMN_IMAGE_ID,
            FoodContract.FoodEntry.COLUMN_DESCRIPTION

    };

    private static final String[] INGR_COLUMNS = {

            IngrEntry.TABLE_NAME + "." + IngrEntry._ID,

            IngrEntry.COLUMN_ID_FOOD,
            IngrEntry.COLUMN_NAME,
            IngrEntry.COLUMN_QTY,
            IngrEntry.COLUMN_UNIT

    };


    public static final int COL_FOOD_ID = 1;
    public static final int COL_TITLE = 2;
    public static final int COL_IMAGE_ID = 3;
    public static final int COL_DESCRIPTION = 4;

    public static final int COL_ID_FOOD = 1;
    public static final int COL_NAME = 2;
    public static final int COL_QTY = 3;
    public static final int COL_UNIT = 4;

    private ImageView mImageView;
    private TextView mTitleView;
    private TextView mDescriptionView;
    private ListView mListView;
    private FloatingActionButton mFab;


    public DetailFoodFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            //mUri = arguments.getParcelable(DetailFoodFragment.DETAIL_URI);
            mUri = Uri.parse(arguments.getString("URI_FOOD"));
            mUri_ingr = Uri.parse(arguments.getString("URI_INGR"));
            mFromMenu = arguments.getString("FROM_MENU");
            mDate = arguments.getString("DATE");
            mTypeOfMeal = arguments.getString("TYPE_OF_MEAL");
        }


        View rootView = inflater.inflate(R.layout.fragment_foods_detail, container, false);

        mImageView = (ImageView) rootView.findViewById(R.id.imageView);
        mTitleView = (TextView) rootView.findViewById(R.id.title_text);
        mDescriptionView = (TextView) rootView.findViewById(R.id.description_text);
        mIngrRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_ingr);
        mFab = (FloatingActionButton) rootView.findViewById(R.id.fab_del);

        if(mFromMenu.equals("NO")) {
            mFab.setVisibility(View.INVISIBLE);
        }else{
            mFab.setVisibility(View.VISIBLE);

            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String mSelectionClause = FoodContract.MenuEntry.COLUMN_DATE +  " = ?";
                    String[] mSelectionArgs = {mDate};


                    ContentValues foodValues = new ContentValues();

                    if(mTypeOfMeal.equals("lunch")){
                        foodValues.put(FoodContract.MenuEntry.COLUMN_LUNCH, "Empty");
                    }else{
                        foodValues.put(FoodContract.MenuEntry.COLUMN_DINNER, "Empty");
                    }


                    int rowUpdated = getActivity().getContentResolver().
                            update(FoodContract.MenuEntry.CONTENT_URI, foodValues, mSelectionClause, mSelectionArgs);

                    getActivity().finish();

                }
            });

        }

        mIngrRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mIngrAdapter = new IngredientsAdapter();
        mIngrRecyclerView.setAdapter(mIngrAdapter);

        return rootView;
    }

    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if ( getActivity() instanceof DetailFoodActivity ){
            // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.main, menu);

        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER_FOOD, null, this);
        getLoaderManager().initLoader(DETAIL_LOADER_INGR, null, this);
        super.onActivityCreated(savedInstanceState);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            if (id == DETAIL_LOADER_FOOD) {
                return new CursorLoader(
                        getActivity(),
                        mUri,
                        FOOD_COLUMNS,
                        null,
                        null,
                        null
                );
            }
        }
        if ( null != mUri_ingr ) {
            if(id == DETAIL_LOADER_INGR) {
                return new CursorLoader(
                        getActivity(),
                        mUri_ingr,
                        INGR_COLUMNS,
                        null,
                        null,
                        null
                );
            }
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(loader.getId() == DETAIL_LOADER_FOOD){
            if (data != null && data.moveToFirst()) {

                String title = data.getString(COL_TITLE);
                String description = data.getString(COL_DESCRIPTION);
                String image_id = data.getString(COL_IMAGE_ID);

                String url = "https://dl.dropboxusercontent.com/u/96819748/"+image_id;
                Log.i(LOG_TAG, "URL: "+url);

                mTitleView.setText(title);
                mDescriptionView.setText(description);

               Picasso.with(getActivity())
                       .load(url)
                       .into(mImageView);



            }
        }

        if(loader.getId() == DETAIL_LOADER_INGR){

            if (data != null ) {

                mIngrAdapter.swapCursor(data);


            }
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}