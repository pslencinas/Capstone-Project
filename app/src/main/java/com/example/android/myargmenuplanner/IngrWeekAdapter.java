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

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * {@link IngrWeekAdapter} exposes a list of weather forecasts
 * from a {@link Cursor} to a {@link RecyclerView}.
 */
public class IngrWeekAdapter extends RecyclerView.Adapter<IngrWeekAdapter.IngrWeekAdapterViewHolder> {


    // Flag to determine if we want to use a separate view for "today".
    private boolean mUseTodayLayout = true;

    private Cursor mCursor;




    /**
     * Cache of the children views for a forecast list item.
     */
    public class IngrWeekAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView mNameView;
        public final TextView mQtyView;
        public final TextView mUnitView;
        public final TextView mDateView;

        public IngrWeekAdapterViewHolder(View view) {
            super(view);
            mDateView = (TextView) view.findViewById(R.id.date_ingr);
            mNameView = (TextView) view.findViewById(R.id.list_ingr_name);
            mQtyView = (TextView) view.findViewById(R.id.list_ingr_qty);
            mUnitView = (TextView) view.findViewById(R.id.list_ingr_unit);

        }


    }



    @Override
    public IngrWeekAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_ingr_week, viewGroup, false);
        view.setFocusable(true);
        return new IngrWeekAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(IngrWeekAdapterViewHolder viewHolder, int position) {

        mCursor.moveToPosition(position);

        String date = mCursor.getString(IngredientsFragment.COL_DATE);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObj = null;
        try {
            dateObj = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat postFormater = new SimpleDateFormat("EEE dd, MMM");
        String newDateStr = postFormater.format(dateObj);


        String name = mCursor.getString(IngredientsFragment.COL_NAME);
        String qty = mCursor.getString(IngredientsFragment.COL_QTY);
        String unit = mCursor.getString(IngredientsFragment.COL_UNIT);

        viewHolder.mDateView.setText(newDateStr);
        viewHolder.mNameView.setText(name);
        viewHolder.mQtyView.setText(qty);
        viewHolder.mUnitView.setText(unit);
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


}