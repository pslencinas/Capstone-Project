/*
 * Copyright (C) 2014 The Android Open Source Project
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
package com.example.android.myargmenuplanner.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.util.Log;

import com.example.android.myargmenuplanner.data.FoodContract.FoodEntry;
import com.example.android.myargmenuplanner.data.FoodContract.IngrEntry;
import com.example.android.myargmenuplanner.data.FoodContract.MenuEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import static android.R.attr.name;
import static com.example.android.myargmenuplanner.R.string.date;

public class LoadMenu extends AsyncTask<String, Void, String[]> {

    private final String LOG_TAG = LoadMenu.class.getSimpleName();

    private String orderBy;
    public Context mContext;

    private static final String[] MENU_COLUMNS = {

            MenuEntry.TABLE_NAME + "." + MenuEntry._ID,
            MenuEntry.COLUMN_DATE

    };

    public LoadMenu(Context context) {
        mContext = context;
    }


    @Override
    protected String[] doInBackground(String... params) {

        Uri mUri = MenuEntry.CONTENT_URI;

        Cursor mCursor= mContext.getContentResolver().query(
                mUri,
                null,
                null,
                null,
                null
        );


        if(mCursor.getCount() == 0) {


            int shift = 0;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            String sDate = df.format(cal.getTime());
            int dayofweek = cal.get(Calendar.DAY_OF_WEEK);

            Log.i(LOG_TAG, "Init Date: " + date);
            Vector<ContentValues> cVVector = new Vector<ContentValues>(14 - dayofweek);
            String sWeek = "thisweek";

            for (int i = dayofweek; i <= 14; i++) {

                if (i > 7) {
                    sWeek = "nextweek";
                }
                ContentValues values = new ContentValues();

                values.put(MenuEntry.COLUMN_DATE, sDate);
                values.put(MenuEntry.COLUMN_LUNCH, "Empty");
                values.put(MenuEntry.COLUMN_ID_LUNCH, 0);
                values.put(MenuEntry.COLUMN_DINNER, "Empty");
                values.put(MenuEntry.COLUMN_ID_DINNER, 0);

                cVVector.add(values);

                cal.add(Calendar.DATE, 1);
                sDate = df.format(cal.getTime());
                //            Log.i(LOG_TAG, "Day of the week: "+cal.get(Calendar.DAY_OF_WEEK));
                //            Log.i(LOG_TAG, "Date: "+date);

            }

            int inserted = 0;


            //            // add to database
            Log.i(LOG_TAG, "Creando registros en base de datos. Tabla Menu ");

            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);

                inserted = mContext.getContentResolver().bulkInsert(MenuEntry.CONTENT_URI, cvArray);
                Log.i(LOG_TAG, "Registros nuevos creados en Tabla Menu: " + inserted);
            }


        }else{  //ya tengo registros, tengo que fijarme las fechas

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            String dateNow = df.format(cal.getTime());
            int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
            String date="";
            String week="";
            while(mCursor.moveToNext()){

                date = mCursor.getString(1);
                week = mCursor.getString(2);

                if(dateNow.equals(date)){

                }
            }



        }





        return null;
    }


}