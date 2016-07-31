package com.example.android.myargmenuplanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.style.BulletSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class FoodsActivity extends AppCompatActivity implements FoodsFragment.Callback{

    private boolean mTwoPane=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foods_detail);

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putParcelable(FoodsFragment.DETAIL_URI, getIntent().getData());
            FoodsFragment fragment = new FoodsFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.foods_detail_container, fragment)
                    .commit();
        }

    }

    @Override
    public void onItemSelected(Uri contentUriFood, Uri contentUriIngr, FoodsAdapter.FoodsAdapterViewHolder vh) {
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
            intent.putExtras(extras);
            startActivity(intent);
            //Toast.makeText(this,"Click on: "+contentUri.toString(), Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
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

