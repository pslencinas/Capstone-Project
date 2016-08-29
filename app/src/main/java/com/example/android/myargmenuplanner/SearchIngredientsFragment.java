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
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.example.android.myargmenuplanner.data.FoodContract;


public class SearchIngredientsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = SearchIngredientsFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";
    static String TYPE_OF_MEAL;
    static String DATE_OF_MEAL;
    public static String mDate;
    private static int mLunch;
    private static int mDinner;
    private IngrWeekAdapter mIngrWeekAdapter;
    private RecyclerView mRecyclerView;
    private MultiAutoCompleteTextView mAutoComp;
    private FoodsAdapter mFoodsAdapter;
    public String[] listOfIngr;
    public String[] listFoodId;
    private ShareActionProvider mShareActionProvider;

    private Uri mUri;

    private static final int DETAIL_LOADER = 0;


    private static final String[] FOOD_COLUMNS = {

            FoodContract.FoodEntry.TABLE_NAME + "." + FoodContract.FoodEntry._ID,

            FoodContract.FoodEntry.COLUMN_ID,
            FoodContract.FoodEntry.COLUMN_TITLE,
            FoodContract.FoodEntry.COLUMN_IMAGE_ID,
            FoodContract.FoodEntry.COLUMN_TIME,

    };

    static final int COL_ID = 1;
    static final int COL_TITLE = 2;
    static final int COL_IMG_ID = 3;

    private static final String[] INGR_COLUMNS = {

            FoodContract.IngrEntry.TABLE_NAME + "." + FoodContract.IngrEntry._ID,

            FoodContract.IngrEntry.COLUMN_ID_FOOD,
            FoodContract.IngrEntry.COLUMN_NAME,
            FoodContract.IngrEntry.COLUMN_UNIT,
            FoodContract.IngrEntry.COLUMN_QTY


    };

    static final int COL_ID_FOOD = 1;
    static final int COL_NAME = 2;
    static final int COL_UNIT = 3;
    static final int COL_QTY = 4;


    private TextView mEmptyList;
    private Button mButton;

    public static ListView mListView1;

    public static String title;
    public static String description;
    public static String food_id;
    public static String image_id;

    public SearchIngredientsFragment() {
        setHasOptionsMenu(true);
    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String type, String mDate, Uri foodUri, Uri ingrUri, FoodsAdapter.FoodsAdapterViewHolder vh);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


       Bundle arguments = getArguments();
        if (arguments != null) {
            //mUri = arguments.getParcelable(DetailFoodFragment.DETAIL_URI);
            TYPE_OF_MEAL = arguments.getString("TYPE");
            DATE_OF_MEAL = arguments.getString("DATE");

        }

        loadIngredients();

        View rootView = inflater.inflate(R.layout.fragment_search_ingr, container, false);

        mEmptyList = (TextView) rootView.findViewById(R.id.empty_list);
        mButton = (Button) rootView.findViewById(R.id.button);

        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                if (mAutoComp.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "You have to select at least one ingredient!!!",
                            Toast.LENGTH_LONG).show();
                }else {
                    onClickButton();

                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
            }

        });


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        // Set the layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFoodsAdapter = new FoodsAdapter(getActivity(), new FoodsAdapter.FoodsAdapterOnClickHandler() {
            @Override
            public void onClick(Long id, FoodsAdapter.FoodsAdapterViewHolder vh) {


                ((SearchIngredientsFragment.Callback) getActivity())
                        .onItemSelected(TYPE_OF_MEAL, DATE_OF_MEAL
                                ,FoodContract.FoodEntry.buildFoodUri(id)
                                ,FoodContract.IngrEntry.buildIngrByFoodUri(id), vh );
            }
        });




        // specify an adapter (see also next example)
        mRecyclerView.setAdapter(mFoodsAdapter);


        mAutoComp=(MultiAutoCompleteTextView)rootView.findViewById(R.id.multiAutoCompleteTextView);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,listOfIngr);


        mAutoComp.setAdapter(adapter);
        mAutoComp.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        return rootView;
    }


    public void onClickButton() {
        String ingr = mAutoComp.getText().toString();
        ingr = ingr.substring(0, ingr.length()-2);
        String[] ingrArr = ingr.split(",");

        String sMenuByDate = "";
        String foodId = "";

        if(ingrArr.length == 1){
            sMenuByDate = FoodContract.IngrEntry.COLUMN_NAME + " =  ? ";
        }else{
            sMenuByDate = FoodContract.IngrEntry.COLUMN_NAME + " =  ? ";
            for(int i=1; i< ingrArr.length; i++){
                ingrArr[i] = ingrArr[i].substring(1);
                sMenuByDate = sMenuByDate + " OR "+FoodContract.IngrEntry.COLUMN_NAME + " =  ? ";

            }

        }

        Uri mUri = FoodContract.IngrEntry.CONTENT_URI;

        Cursor mCursor = getActivity().getContentResolver().query(
                mUri,
                INGR_COLUMNS,
                sMenuByDate,                           // selection = Condicion del WHERE
                ingrArr,                                // selectionArgs = arg del WHERE
                null
        );
        int i=0;
        if (mCursor != null && mCursor.moveToFirst()){
            listFoodId = new String[mCursor.getCount()];
            do{
                foodId = mCursor.getString(COL_ID_FOOD);
                Log.i(LOG_TAG, "FoodId: " + foodId);
                listFoodId[i] = foodId;
                i++;

            }while (mCursor.moveToNext());

        }

        getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        //getLoaderManager().initLoader(DETAIL_LOADER, null, this);

        super.onActivityCreated(savedInstanceState);
    }

    void loadIngredients(){


        Uri mUri = FoodContract.IngrEntry.CONTENT_URI;
        String nameIngr = "";

        int i=0;
        String sortOrder = FoodContract.IngrEntry.COLUMN_NAME + " ASC";
        String selection = FoodContract.IngrEntry.COLUMN_NAME + " NOT NULL GROUP BY " + FoodContract.IngrEntry.COLUMN_NAME;

        Cursor mCursor = getActivity().getContentResolver().query(
                mUri,
                INGR_COLUMNS,
                selection,                               // selection = Condicion del WHERE
                null,                                // selectionArgs = arg del WHERE
                sortOrder
        );


        if (mCursor != null && mCursor.moveToFirst()){
            listOfIngr = new String[mCursor.getCount()];
            do{
                nameIngr = mCursor.getString(COL_NAME);
                //Log.i(LOG_TAG, "Ingredients: " + nameIngr);
                listOfIngr[i] = nameIngr;
                i++;

            }while (mCursor.moveToNext());

        }


    }
    void onChanged( ) {

        getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        mUri = FoodContract.FoodEntry.CONTENT_URI;
        String selection;
        if(listFoodId.length == 1){
            selection = FoodContract.FoodEntry.COLUMN_ID + " =  ? ";
        }else{
            selection = FoodContract.FoodEntry.COLUMN_ID + " =  ? ";
            for(int i=1; i< listFoodId.length; i++){

                selection = selection + " OR "+FoodContract.FoodEntry.COLUMN_ID + " =  ? ";

            }
        }

        Log.i(LOG_TAG, "selection: " + selection);


        return new CursorLoader(
                getActivity(),
                mUri,
                FOOD_COLUMNS,           //columnas
                selection,                   //selection
                listFoodId,                   //selectionArg
                null                    //sortOrder
        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        if (data != null) {

            mFoodsAdapter.swapCursor(data);


        }else {
            Toast.makeText(getActivity(), "There is not food for ingredient you have selected!!!",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFoodsAdapter.swapCursor(null);
    }




}