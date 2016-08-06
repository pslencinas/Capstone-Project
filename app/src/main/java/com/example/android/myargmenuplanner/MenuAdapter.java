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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.myargmenuplanner.data.FoodContract;

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

        public MenuAdapterViewHolder(View view) {
            super(view);
            mDayView = (TextView) view.findViewById(R.id.tv_day);
            mLunchView = (TextView) view.findViewById(R.id.tv_lunch);
            mDinnerView = (TextView) view.findViewById(R.id.tv_dinner);

            mLunchView.setOnClickListener(this);
            mDinnerView.setOnClickListener(this);
            //view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int columnIndex=0;
            Log.i("onClick : ","id:"+v.getId());
            if(v.getId() == mLunchView.getId()){
                Log.i("onClick mLunchView: ","id:"+mLunchView.getId());
                columnIndex = mCursor.getColumnIndex(FoodContract.MenuEntry.COLUMN_LUNCH);
                mClickHandler.onClick(mCursor.getString(columnIndex), this);
            }
            if(v.getId() == mDinnerView.getId()){
                Log.i("onClick mDinnerView: ","id:"+mDinnerView.getId());
                columnIndex = mCursor.getColumnIndex(FoodContract.MenuEntry.COLUMN_DINNER);
                mClickHandler.onClick(mCursor.getString(columnIndex), this);
            }

            //Log.i("Dentro de MenuAdapter: ","Click on View: "+columnIndex+ "mCursor.getLong: "+mCursor.getLong(columnIndex));
            Toast.makeText(mContext,"Click on: "+mCursor.getString(columnIndex)+", date: "+mCursor.getString(1)
                    , Toast.LENGTH_SHORT).show();

        }
    }

    public static interface MenuAdapterOnClickHandler {
        void onClick(String id, MenuAdapterViewHolder vh);
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

        // Read date from cursor
        String date = mCursor.getString(TabFragmentTW.COL_DATE);
        String lunch = mCursor.getString(TabFragmentTW.COL_LUNCH);
        String dinner = mCursor.getString(TabFragmentTW.COL_DINNER);

        viewHolder.mDayView.setText(date);
        viewHolder.mLunchView.setText(lunch);
        viewHolder.mDinnerView.setText(dinner);

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