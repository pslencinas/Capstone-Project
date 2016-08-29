package com.example.android.myargmenuplanner;

import android.app.Activity;
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

public class MainActivity extends AppCompatActivity implements TabFragmentTW.Callback{

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private boolean mTwoPane=false;

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

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();

/**
 * Setup click events on the Navigation View Items.
 */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                if (menuItem.getItemId() == R.id.nav_ingredients) {

                    Intent intent = new Intent(MainActivity.this, IngredientsActivity.class);
                    startActivity(intent);

                }

                if (menuItem.getItemId() == R.id.nav_foods) {

                    Intent intent = new Intent(MainActivity.this, FoodsActivity.class);
                    startActivity(intent);

                }

                if (menuItem.getItemId() == R.id.nav_search) {

                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intent);

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


        FetchJsonDataTask fetch = new FetchJsonDataTask(this);
        fetch.execute();
        LoadMenu menu = new LoadMenu(this);
        menu.execute();



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
}
