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
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.myargmenuplanner.data.FoodContract;


/**
 * {@link IngredientsAdapter} exposes a list of weather forecasts
 * from a {@link Cursor} to a {@link RecyclerView}.
 */
public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.FoodsAdapterViewHolder> {


    // Flag to determine if we want to use a separate view for "today".
    private boolean mUseTodayLayout = true;

    private Cursor mCursor;




    /**
     * Cache of the children views for a forecast list item.
     */
    public class FoodsAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView mNameView;
        public final TextView mQtyView;
        public final TextView mUnitView;

        public FoodsAdapterViewHolder(View view) {
            super(view);
            mNameView = (TextView) view.findViewById(R.id.list_ingr_name);
            mQtyView = (TextView) view.findViewById(R.id.list_ingr_qty);
            mUnitView = (TextView) view.findViewById(R.id.list_ingr_unit);

        }


    }



    @Override
    public FoodsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_ingr, viewGroup, false);
        view.setFocusable(true);
        return new FoodsAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(FoodsAdapterViewHolder viewHolder, int position) {

        mCursor.moveToPosition(position);

        // Read date from cursor
        String name = mCursor.getString(DetailFoodFragment.COL_NAME);
        String qty = mCursor.getString(DetailFoodFragment.COL_QTY);
        String unit = mCursor.getString(DetailFoodFragment.COL_UNIT);

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