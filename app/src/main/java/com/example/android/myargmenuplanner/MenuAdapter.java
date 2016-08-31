/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.myargmenuplanner;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.myargmenuplanner.data.FoodContract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.id;


/**
 * {@link MenuAdapter} exposes a list of weather forecasts
 * from a {@link Cursor} to a {@link RecyclerView}.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuAdapterViewHolder> {

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    // Flag to determine if we want to use a separate view for "today".
    private boolean mUseTodayLayout = true;
    final private MenuAdapterOnClickHandler mClickHandler;
    private Cursor mCursor;
    final private Context mContext;



    /**
     * Cache of the children views for a forecast list item.
     */
    public class MenuAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mDayView;
        public final TextView mLunchView;
        public final TextView mDinnerView;
        public final LinearLayout mLinearLayout;

        public MenuAdapterViewHolder(View view) {
            super(view);
            mDayView = (TextView) view.findViewById(R.id.tv_day);
            mLunchView = (TextView) view.findViewById(R.id.tv_lunch);
            mDinnerView = (TextView) view.findViewById(R.id.tv_dinner);
            mLinearLayout=(LinearLayout)view.findViewById(R.id.layout_menu);

            mLunchView.setOnClickListener(this);
            mDinnerView.setOnClickListener(this);
            //view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int columnLunchIndex=0;
            int columnDinnerIndex=0;
            int columnDateIndex=0;


            if(v.getId() == mLunchView.getId()){
                columnLunchIndex = mCursor.getColumnIndex(FoodContract.MenuEntry.COLUMN_LUNCH);
                columnDateIndex = mCursor.getColumnIndex(FoodContract.MenuEntry.COLUMN_DATE);
                mClickHandler.onClick("lunch", mCursor.getString(columnLunchIndex), mCursor.getString(columnDateIndex), this);
            }
            if(v.getId() == mDinnerView.getId()){
                columnDinnerIndex = mCursor.getColumnIndex(FoodContract.MenuEntry.COLUMN_DINNER);
                columnDateIndex = mCursor.getColumnIndex(FoodContract.MenuEntry.COLUMN_DATE);
                mClickHandler.onClick("dinner", mCursor.getString(columnDinnerIndex), mCursor.getString(columnDateIndex),this);
            }



        }
    }

    public static interface MenuAdapterOnClickHandler {
        void onClick(String type, String id, String date, MenuAdapterViewHolder vh);
    }

    public MenuAdapter(Context context, MenuAdapterOnClickHandler dh) {
        mContext = context;
        mClickHandler = dh;

    }


    @Override
    public MenuAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_menu, viewGroup, false);
        view.setFocusable(true);
        return new MenuAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MenuAdapterViewHolder viewHolder, int position) {

        mCursor.moveToPosition(position);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        // Read date from cursor
        String date = mCursor.getString(TabFragmentTW.COL_DATE);
        String lunch = mCursor.getString(TabFragmentTW.COL_LUNCH);
        String dinner = mCursor.getString(TabFragmentTW.COL_DINNER);

        viewHolder.mLunchView.setText(lunch);
        viewHolder.mDinnerView.setText(dinner);

        if(!lunch.equals("Empty")){
            viewHolder.mLunchView.setTextColor(Color.parseColor("#7986cb"));

        }
        if(!dinner.equals("Empty")){
            viewHolder.mDinnerView.setTextColor(Color.parseColor("#7986cb"));

        }
        Date dateObj = null;
        try {
            dateObj = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat postFormater = new SimpleDateFormat("EEE dd, MMM");
        String newDateStr = postFormater.format(dateObj);

        viewHolder.mDayView.setText(newDateStr);

    }


    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();

    }

    public Cursor getCursor() {
        return mCursor;
    }

    public void selectView(RecyclerView.ViewHolder viewHolder) {
        if ( viewHolder instanceof MenuAdapter.MenuAdapterViewHolder) {
            MenuAdapter.MenuAdapterViewHolder vfh = (MenuAdapter.MenuAdapterViewHolder)viewHolder;
            vfh.onClick(vfh.itemView);
        }
    }
}