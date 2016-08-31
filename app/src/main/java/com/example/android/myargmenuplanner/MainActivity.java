package com.example.android.myargmenuplanner;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.myargmenuplanner.data.FetchJsonDataTask;
import com.example.android.myargmenuplanner.data.FoodContract;
import com.example.android.myargmenuplanner.data.LoadMenu;

import static android.R.attr.id;
import static android.R.attr.name;

public class MainActivity extends AppCompatActivity implements TabFragmentTW.Callback, FoodsFragment.Callback{

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    public boolean mTwoPane=false;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Setup the DrawerLayout and NavigationView
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view) ;

        if (findViewById(R.id.food_detail_container) != null) {
            mTwoPane = true;
            Log.d(LOG_TAG, "TABLET!!!!!");

            if (savedInstanceState == null) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.food_detail_container, new FoodsFragment(), DETAILFRAGMENT_TAG)
//                        .commit();
            }
        } else {
            Log.d(LOG_TAG, "Dentro del ELSE de onCreate MainActivity!!!!!");
            mTwoPane = false;

        }





        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                if (menuItem.getItemId() == R.id.nav_ingredients) {

                    if(mTwoPane){
                        Bundle extras = new Bundle();
                        extras.putString("TYPE", "SHOW");

                        IngredientsFragment fragment = new IngredientsFragment();
                        fragment.setArguments(extras);

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.food_detail_container, fragment, DETAILFRAGMENT_TAG)
                                .commit();

                    }else {
                        Intent intent = new Intent(MainActivity.this, IngredientsActivity.class);
                        startActivity(intent);
                    }
                }

                if (menuItem.getItemId() == R.id.nav_foods) {


                    if(mTwoPane){
                        Bundle extras = new Bundle();
                        extras.putString("TYPE", "SHOW");

                        FoodsFragment fragment = new FoodsFragment();
                        fragment.setArguments(extras);

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.food_detail_container, fragment, DETAILFRAGMENT_TAG)
                                .commit();

                    }else {

                        Intent intent = new Intent(MainActivity.this, FoodsActivity.class);
                        startActivity(intent);
                    }


                }

                if (menuItem.getItemId() == R.id.nav_search) {

                    if(mTwoPane){
                        Bundle extras = new Bundle();
                        extras.putString("TYPE", "SHOW");

                        SearchIngredientsFragment fragment = new SearchIngredientsFragment();
                        fragment.setArguments(extras);

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.food_detail_container, fragment, DETAILFRAGMENT_TAG)
                                .commit();

                    }else {
                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(intent);
                    }
                }




                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();








    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                Log.i(LOG_TAG, "Dentro de MainActivity: Result= "+result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onItemSelectedMenu(String type_of_meal, String meal, String date, MenuAdapter.MenuAdapterViewHolder vh) {
        if (mTwoPane) {

            Bundle extras = new Bundle();
            extras.putString("TYPE", type_of_meal);
            extras.putString("DATE", date);

            FoodsFragment fragment = new FoodsFragment();
            fragment.setArguments(extras);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.food_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();

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


            if(meal.equals("Empty")){
                Intent intent = new Intent(this, FoodsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("TYPE", type_of_meal);
                extras.putString("DATE", date);
                intent.putExtras(extras);
                startActivityForResult(intent, 1);
                //startActivity(intent);

                Log.i(LOG_TAG, "Dentro de onItemSelectedMenu - id=Empty");
            }else{
                long id=0;
                String mSelection = FoodContract.FoodEntry.COLUMN_TITLE +  " = ?";  //WHERE
                String[] mSelArgs = {meal};                                         // parametros del WHERE
                String[] mProj = {FoodContract.FoodEntry.COLUMN_ID};                //Columnas a mostrar

                Cursor mCursor = this.getContentResolver().
                        query(FoodContract.FoodEntry.CONTENT_URI,mProj , mSelection, mSelArgs,null);

                mCursor.moveToFirst();
                int nameColumnIndex = mCursor.getColumnIndex(FoodContract.FoodEntry.COLUMN_ID);
                id = mCursor.getLong(nameColumnIndex);

                Intent intent = new Intent(this, DetailFoodActivity.class);
                Bundle extras = new Bundle();
                extras.putString("URI_FOOD", FoodContract.FoodEntry.buildFoodUri(id).toString());
                extras.putString("URI_INGR", FoodContract.IngrEntry.buildIngrByFoodUri(id).toString());
                extras.putString("FROM_MENU", "YES");
                extras.putString("DATE", date);
                extras.putString("TYPE_OF_MEAL", type_of_meal);
                intent.putExtras(extras);
                startActivity(intent);
            }

            //Toast.makeText(this,"Click on: "+contentUri.toString(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onItemSelected(String type, String mDate, Uri contentUriFood, Uri contentUriIngr, FoodsAdapter.FoodsAdapterViewHolder vh) {

        //Intent intent = new Intent(this, DetailFoodActivity.class).setData(contentUri);
        if(type.equals("lunch") || type.equals("dinner")){
//                Log.i("FoodsActivity", "Dentro de onItemSelected: TYPE_OF_MEAL: "+type);

            //contentUriFood contiene el URI de la comida seleccionada  ->  content://BASE/foods/id

            String mSelection = FoodContract.FoodEntry.COLUMN_ID +  " = ?";
            String[] mSelArgs = {FoodContract.FoodEntry.getFoodIDbyUri(contentUriFood)};
            String[] mProj = {FoodContract.FoodEntry.COLUMN_TITLE};

            Cursor mCursor = this.getContentResolver().
                    query(FoodContract.FoodEntry.CONTENT_URI,mProj , mSelection, mSelArgs,null);

            mCursor.moveToFirst();
            int nameColumnIndex = mCursor.getColumnIndex(FoodContract.FoodEntry.COLUMN_TITLE);

            String mSelectionClause = FoodContract.MenuEntry.COLUMN_DATE +  " = ?";
            String[] mSelectionArgs = {mDate};


            ContentValues foodValues = new ContentValues();
            if(type.equals("lunch")){
                foodValues.put(FoodContract.MenuEntry.COLUMN_LUNCH, mCursor.getString(nameColumnIndex));
                foodValues.put(FoodContract.MenuEntry.COLUMN_ID_LUNCH, FoodContract.FoodEntry.getFoodIDbyUri(contentUriFood));
            }else{
                foodValues.put(FoodContract.MenuEntry.COLUMN_DINNER, mCursor.getString(nameColumnIndex));
                foodValues.put(FoodContract.MenuEntry.COLUMN_ID_DINNER, FoodContract.FoodEntry.getFoodIDbyUri(contentUriFood));
            }


            int rowUpdated = this.getContentResolver().
                    update(FoodContract.MenuEntry.CONTENT_URI, foodValues, mSelectionClause, mSelectionArgs);


        }else {
            Bundle extras = new Bundle();
            extras.putString("URI_FOOD", contentUriFood.toString());
            extras.putString("URI_INGR", contentUriIngr.toString());
            extras.putString("FROM_MENU", "NO");

            DetailFoodFragment fragment = new DetailFoodFragment();
            fragment.setArguments(extras);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.food_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();

        }

    }

}
