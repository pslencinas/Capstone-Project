package com.example.android.myargmenuplanner;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.android.myargmenuplanner.R;

public class IngredientsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingr);

        if (savedInstanceState == null) {
            Bundle arguments;
            arguments = getIntent().getExtras();

            IngredientsFragment fragment = new IngredientsFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.ingr_container, fragment)
                    .commit();
        }
    }

}
