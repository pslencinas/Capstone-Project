package com.example.android.myargmenuplanner;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.android.myargmenuplanner.data.FoodContract;

public class SearchActivity extends AppCompatActivity implements SearchIngredientsFragment.Callback{

    private boolean mTwoPane=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ingr);

        if (savedInstanceState == null) {
            Bundle arguments;
            arguments = getIntent().getExtras();

            SearchIngredientsFragment fragment = new SearchIngredientsFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.ingr_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onItemSelected(String type, String mDate, Uri contentUriFood, Uri contentUriIngr, FoodsAdapter.FoodsAdapterViewHolder vh) {
        if (mTwoPane) {

//            Bundle args = new Bundle();
//            args.putParcelable(DetailFragment.DETAIL_URI, contentUri);
//
//            DetailFragment fragment = new DetailFragment();
//            fragment.setArguments(args);
//
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.weather_detail_container, fragment, DETAILFRAGMENT_TAG)
//                    .commit();
        } else {
            //Intent intent = new Intent(this, DetailFoodActivity.class).setData(contentUri);

                Intent intent = new Intent(this, DetailFoodActivity.class);
                Bundle extras = new Bundle();
                extras.putString("URI_FOOD", contentUriFood.toString());
                extras.putString("URI_INGR", contentUriIngr.toString());
                extras.putString("FROM_MENU", "NO");
                intent.putExtras(extras);
                startActivity(intent);
                //Toast.makeText(this,"Click on: "+contentUri.toString(), Toast.LENGTH_SHORT).show();

        }
    }




}
