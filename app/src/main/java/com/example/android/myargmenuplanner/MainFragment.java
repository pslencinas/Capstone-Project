package com.example.android.myargmenuplanner;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.myargmenuplanner.data.FetchJsonDataTask;
import com.example.android.myargmenuplanner.data.FoodContract;
import com.example.android.myargmenuplanner.data.LoadMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static android.R.attr.id;
import static com.example.android.myargmenuplanner.FoodsFragment.COL_ID;
import static com.example.android.myargmenuplanner.R.id.toolbar;


public class MainFragment extends Fragment{

    private final String LOG_TAG = MainFragment.class.getSimpleName();
    private static RecyclerView mRecView;
    private MenuAdapter mMenuAdapter;

    private int mPosition = ListView.INVALID_POSITION;

    public Toolbar toolbar;

    public Context mContext;

    public MainFragment() {

    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri foodUri, Uri ingrUri, MenuAdapter.MenuAdapterViewHolder vh);
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


        //Log.i(LOG_TAG, "Dentro de onCreateView Main Fragment");
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.containerView,new TabFragment()).commit();


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        FetchJsonDataTask fetch = new FetchJsonDataTask(getActivity());
        fetch.execute();
        LoadMenu menu = new LoadMenu(getActivity());
        menu.execute();

        super.onActivityCreated(savedInstanceState);
    }



}




