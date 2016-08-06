package com.example.android.myargmenuplanner;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.myargmenuplanner.data.FoodContract;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;
import static com.example.android.myargmenuplanner.R.id.spinner_dinner;
import static com.example.android.myargmenuplanner.R.id.spinner_lunch;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddMenuFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private Spinner spinner_lunch;
    private Spinner spinner_dinner;

    private Button btnSubmit;
    private static final int DETAIL_LOADER = 0;
    static final String DETAIL_URI = "URI";
    private ArrayList mArrayList;
    private ArrayAdapter mDataAdapter;
    private static final String LOG_TAG = AddMenuFragment.class.getSimpleName();
    private Uri mUri;

    private Context mContext;
    private static final String[] FOOD_COLUMNS = {

            FoodContract.FoodEntry.TABLE_NAME + "." + FoodContract.FoodEntry._ID,

            FoodContract.FoodEntry.COLUMN_ID,
            FoodContract.FoodEntry.COLUMN_TITLE,


    };

    static final int COL_ID = 1;
    static final int COL_TITLE = 2;

    public AddMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View myView =  inflater.inflate(R.layout.fragment_add_menu, container, false);

        mContext = getActivity();
        spinner_lunch = (Spinner) myView.findViewById(R.id.spinner_lunch);
        spinner_dinner = (Spinner) myView.findViewById(R.id.spinner_dinner);

        btnSubmit = (Button) myView.findViewById(R.id.btnSubmit);

        mArrayList = new ArrayList<String>();
        mArrayList.add("empty");
        mDataAdapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, mArrayList);
        mDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



//        spinner_l1.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(mContext,
                        "OnClickListener : " +
                                "\nSpinner 1 : "+ String.valueOf(spinner_lunch.getSelectedItem())+
                                "\nSpinner 2 : "+ String.valueOf(spinner_dinner.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();
            }

        });


        return myView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    private class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//            Toast.makeText(parent.getContext(),
//                    "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
//                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.i(LOG_TAG, "Dentro de onCreateLoader");

        mUri = FoodContract.FoodEntry.CONTENT_URI;

        return new CursorLoader(
                getActivity(),
                mUri,
                FOOD_COLUMNS,
                null,
                null,
                null
        );


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        if (data != null && data.moveToFirst()) {

            mArrayList.clear();
            while(!data.isAfterLast()) {
                mArrayList.add(data.getString(COL_TITLE)); //add the item
                data.moveToNext();
            }

            spinner_lunch.setAdapter(mDataAdapter);
            spinner_dinner.setAdapter(mDataAdapter);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
