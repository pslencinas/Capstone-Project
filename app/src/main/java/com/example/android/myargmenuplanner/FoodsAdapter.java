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
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.myargmenuplanner.data.FoodContract;

import static android.R.attr.choiceMode;


/**
 * {@link FoodsAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.support.v7.widget.RecyclerView}.
 */
public class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.FoodsAdapterViewHolder> {

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    // Flag to determine if we want to use a separate view for "today".
    private boolean mUseTodayLayout = true;
    final private FoodsAdapterOnClickHandler mClickHandler;
    private Cursor mCursor;
    final private Context mContext;



    /**
     * Cache of the children views for a forecast list item.
     */
    public class FoodsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mTitleView;
        public final TextView mDescriptionView;

        public FoodsAdapterViewHolder(View view) {
            super(view);
            mTitleView = (TextView) view.findViewById(R.id.list_item_title);
            mDescriptionView = (TextView) view.findViewById(R.id.list_item_description);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int columnIndex = mCursor.getColumnIndex(FoodContract.FoodEntry.COLUMN_ID);
            mClickHandler.onClick(mCursor.getLong(columnIndex), this);



        }
    }

    public static interface FoodsAdapterOnClickHandler {
        void onClick(Long id, FoodsAdapterViewHolder vh);
    }

    public FoodsAdapter(Context context, FoodsAdapterOnClickHandler dh) {
        mContext = context;
        mClickHandler = dh;

    }


    @Override
    public FoodsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_foods, viewGroup, false);
        view.setFocusable(true);
        return new FoodsAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(FoodsAdapterViewHolder viewHolder, int position) {

        mCursor.moveToPosition(position);

        // Read date from cursor
        String title = mCursor.getString(FoodsFragment.COL_TITLE);
        String description = mCursor.getString(FoodsFragment.COL_DESCRIPTION);


        viewHolder.mTitleView.setText(title);
        viewHolder.mDescriptionView.setText(description);

    }



    @Override
    public int getItemViewType(int position) {
        return (position == 0 && mUseTodayLayout) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
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
        if ( viewHolder instanceof FoodsAdapterViewHolder ) {
            FoodsAdapterViewHolder vfh = (FoodsAdapterViewHolder)viewHolder;
            vfh.onClick(vfh.itemView);
        }
    }
}