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
        public final TextView mLaunch1View;
        public final TextView mLaunch2View;
        public final TextView mDinner1View;
        public final TextView mDinner2View;

        public MenuAdapterViewHolder(View view) {
            super(view);
            mDayView = (TextView) view.findViewById(R.id.tv_day);
            mLaunch1View = (TextView) view.findViewById(R.id.tv_launch_1);
            mLaunch2View = (TextView) view.findViewById(R.id.tv_launch_2);
            mDinner1View = (TextView) view.findViewById(R.id.tv_dinner_1);
            mDinner2View = (TextView) view.findViewById(R.id.tv_dinner_2);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);

//            int columnIndex = mCursor.getColumnIndex(FoodContract.FoodEntry.COLUMN_ID);
//            mClickHandler.onClick(mCursor.getLong(columnIndex), this);



        }
    }

    public static interface MenuAdapterOnClickHandler {
        void onClick(Long id, MenuAdapterViewHolder vh);
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

//        // Read date from cursor
//        String title = mCursor.getString(FoodsFragment.COL_TITLE);
//        String description = mCursor.getString(FoodsFragment.COL_DESCRIPTION);
//
//
//        viewHolder.mTitleView.setText(title);
//        viewHolder.mDescriptionView.setText(description);

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