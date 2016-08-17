package com.example.android.myargmenuplanner;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.myargmenuplanner.data.FoodContract;

public class FoodsActivity extends AppCompatActivity implements FoodsFragment.Callback{

    private boolean mTwoPane=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foods_detail);

        if (savedInstanceState == null) {
            Bundle arguments;
            arguments = getIntent().getExtras();

            FoodsFragment fragment = new FoodsFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.foods_detail_container, fragment)
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



                finish();

            }else {
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    public void onBackPressed() {
        Intent myIntent = new Intent(FoodsActivity.this, MainActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
        startActivity(myIntent);
        finish();
        return;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

