package com.example.android.myargmenuplanner;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        if (savedInstanceState == null) {

            AddMenuFragment fragment = new AddMenuFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.add_menu_container, fragment)
                    .commit();
        }


    }
}
