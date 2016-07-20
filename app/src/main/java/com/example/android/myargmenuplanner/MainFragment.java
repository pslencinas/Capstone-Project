package com.example.android.myargmenuplanner;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.myargmenuplanner.data.FetchJsonDataTask;


public class MainFragment extends Fragment{

    private final String LOG_TAG = MainFragment.class.getSimpleName();
    public static ListView listView;
    private int qtyMovies;
    private int mPosition = ListView.INVALID_POSITION;
    public static String orderBy = "popular";

    public Context mContext;

    public MainFragment() {

    }

    public interface Callback {
        /**
         *  for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //mMovieAdapter = new MovieAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.activity_main_nd, container, false);
        listView = (ListView) rootView.findViewById(R.id.listview_foods);
        //listView.setAdapter(mMovieAdapter);



        FetchJsonDataTask fetch = new FetchJsonDataTask(getActivity());
        fetch.execute();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {

//                    ((Callback) getActivity()).onItemSelected(MovieContract.MovieEntry.
//                            buildMovieUriByMovie(cursor.getLong(COL_ID)));

                }
                mPosition = position;
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        super.onActivityCreated(savedInstanceState);
    }


//
//    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//
//        Uri orderUri;
//
//        if(orderBy.equals("favorites")){
//            orderUri = MovieContract.FavoriteEntry.CONTENT_URI;
//
//            return new CursorLoader(getActivity(),  //context
//                    orderUri,                       //URI
//                    FAVORITE_COLUMNS,               //Projection = columnas a devolver
//                    null,                           //condicion del query
//                    null,                           //argumentos
//                    null);                          //orden
//
//        }else{
//            orderUri = MovieContract.MovieEntry.CONTENT_URI;
//
//            return new CursorLoader(getActivity(),  //context
//                    orderUri,                       //URI
//                    MOVIE_COLUMNS,                  //Projection = columnas a devolver
//                    null,                           //condicion del query
//                    null,                           //argumentos
//                    null);                          //orden
//
//        }
//
//
//
//    }
//
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//
//        mMovieAdapter.swapCursor(data);
//
//    }
//
//    public void onLoaderReset(Loader<Cursor> loader) {
//        mMovieAdapter.swapCursor(null);
//    }


}




