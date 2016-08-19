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
package com.example.android.myargmenuplanner.widget;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.RemoteViews;

import com.example.android.myargmenuplanner.MainActivity;
import com.example.android.myargmenuplanner.R;
import com.example.android.myargmenuplanner.data.FoodContract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * IntentService which handles updating all Today widgets with the latest data
 */
public class TodayWidgetIntentService extends IntentService {


    private static final String[] MENU_COLUMNS = {

            FoodContract.MenuEntry.TABLE_NAME + "." + FoodContract.MenuEntry._ID,

            FoodContract.MenuEntry.COLUMN_DATE,
            FoodContract.MenuEntry.COLUMN_LUNCH,
            FoodContract.MenuEntry.COLUMN_DINNER

    };

    static final int COL_DATE = 1;
    static final int COL_LUNCH = 2;
    static final int COL_DINNER = 3;

    public TodayWidgetIntentService() {
        super("TodayWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Retrieve all of the Today widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                WidgetProvider.class));


        Uri mUri = FoodContract.MenuEntry.CONTENT_URI;
        String sMenuByDate = FoodContract.MenuEntry.COLUMN_DATE + " = ?";

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String dateNow = df.format(cal.getTime());

        Cursor data = getContentResolver().query(
                mUri,                                   // tabla
                MENU_COLUMNS,                                   // columnas a mostrar
                sMenuByDate,                            // selection = Condicion del WHERE
                new String []{dateNow},                 // selectionArgs = arg del WHERE
                null
        );


        if (data == null) {
            return;
        }
        if (!data.moveToFirst()) {
            data.close();
            return;
        }

        // Extract data from the Cursor

        String mDate = data.getString(COL_DATE);
        String mLunch = data.getString(COL_LUNCH);
        String mDinner = data.getString(COL_DINNER);

        data.close();

        // Perform this loop procedure for each Today widget
        for (int appWidgetId : appWidgetIds) {
            // Find the correct layout based on the widget's width
            int widgetWidth = getWidgetWidth(appWidgetManager, appWidgetId);
            int defaultWidth = getResources().getDimensionPixelSize(R.dimen.widget_today_default_width);
            int largeWidth = getResources().getDimensionPixelSize(R.dimen.widget_today_large_width);
            int layoutId;
            if (widgetWidth >= largeWidth) {
                layoutId = R.layout.widget_layout;
            } else if (widgetWidth >= defaultWidth) {
                layoutId = R.layout.widget_layout;
            } else {
                layoutId = R.layout.widget_layout;
            }
            RemoteViews views = new RemoteViews(getPackageName(), layoutId);



            views.setTextViewText(R.id.today, "Today");
            views.setTextViewText(R.id.w_lunch, mLunch);
            views.setTextViewText(R.id.w_dinner, mDinner);

            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private int getWidgetWidth(AppWidgetManager appWidgetManager, int appWidgetId) {
        // Prior to Jelly Bean, widgets were always their default size
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return getResources().getDimensionPixelSize(R.dimen.widget_today_default_width);
        }
        // For Jelly Bean and higher devices, widgets can be resized - the current size can be
        // retrieved from the newly added App Widget Options
        return getWidgetWidthFromOptions(appWidgetManager, appWidgetId);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private int getWidgetWidthFromOptions(AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        if (options.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)) {
            int minWidthDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
            // The width returned is in dp, but we'll convert it to pixels to match the other widths
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minWidthDp,
                    displayMetrics);
        }
        return  getResources().getDimensionPixelSize(R.dimen.widget_today_default_width);
    }

}
