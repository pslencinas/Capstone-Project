package com.example.android.myargmenuplanner;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabFragmentTW extends Fragment {


    public TabFragmentTW() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View myView =  inflater.inflate(R.layout.tab_fragment_tw, container, false);

        FloatingActionButton myFab = (FloatingActionButton) myView.findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Snackbar.make(v, "Click on FAB", Snackbar.LENGTH_LONG)
                        .show();

                Intent i = new Intent(getActivity(), AddMenuActivity.class);
                startActivity(i);
            }
        });


        return myView;
    }


}
