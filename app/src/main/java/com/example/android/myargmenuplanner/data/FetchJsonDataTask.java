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
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.myargmenuplanner.BuildConfig;
import com.example.android.myargmenuplanner.data.FoodContract.FoodEntry;
import com.example.android.myargmenuplanner.data.FoodContract.IngrEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import static android.R.attr.id;
import static com.example.android.myargmenuplanner.FoodsFragment.image_id;

public class FetchJsonDataTask extends AsyncTask<String, Void, String[]> {

    private final String LOG_TAG = FetchJsonDataTask.class.getSimpleName();

    private String orderBy;
    public Context mContext;

    public FetchJsonDataTask(Context context) {
        mContext = context;
    }


    private void getDataFoodFromJson(String JsonStr)
            throws JSONException {

        final String JSON_LIST = "foods";
        final String JSON_TITLE = "title";
        final String JSON_IMAGE_ID = "image_id";
        final String JSON_DESCRIPTION = "description";
        final String JSON_TIME = "time";
        final String JSON_ID = "id";


        try {
            JSONObject dataJson = new JSONObject(JsonStr);
            JSONArray moviesArray = dataJson.getJSONArray(JSON_LIST);

            Vector<ContentValues> cVVector = new Vector<ContentValues>(moviesArray.length());

            for (int i = 0; i < moviesArray.length(); i++) {

                JSONObject movie = moviesArray.getJSONObject(i);
                String title = movie.getString(JSON_TITLE);
                String image_id = movie.getString(JSON_IMAGE_ID);
                String description = movie.getString(JSON_DESCRIPTION);
                String time = movie.getString(JSON_TIME);
                String id = movie.getString(JSON_ID);



                ContentValues foodsValues = new ContentValues();

                foodsValues.put(FoodEntry.COLUMN_ID, id);
                foodsValues.put(FoodEntry.COLUMN_TITLE, title);
                foodsValues.put(FoodEntry.COLUMN_IMAGE_ID, image_id);
                foodsValues.put(FoodEntry.COLUMN_DESCRIPTION, description);
                foodsValues.put(FoodEntry.COLUMN_TIME, time);
                cVVector.add(foodsValues);

            }
                int inserted = 0;

//            delete database
            int rowdeleted= mContext.getContentResolver().delete(FoodEntry.CONTENT_URI, null, null);

//            // add to database
            Log.i(LOG_TAG, "Creando registros en base de datos. Tabla Food: ");
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);

                inserted = mContext.getContentResolver().bulkInsert(FoodEntry.CONTENT_URI, cvArray);
                Log.i(LOG_TAG, "Registros nuevos creados en tabla Food: "+inserted);
            }




        }
        catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

    }


    private void getDataIngrFromJson(String JsonStr) throws JSONException {

        final String JSON_LIST = "ingredients";
        final String JSON_NAME = "name";
        final String JSON_QTY = "qty";
        final String JSON_UNIT = "unit";
        final String JSON_ID_FOOD = "id_food";


        try {
            JSONObject dataJson = new JSONObject(JsonStr);
            JSONArray ingrArray = dataJson.getJSONArray(JSON_LIST);

            Vector<ContentValues> cVVector = new Vector<ContentValues>(ingrArray.length());

            for (int i = 0; i < ingrArray.length(); i++) {

                JSONObject ingr = ingrArray.getJSONObject(i);
                String id_food = ingr.getString(JSON_ID_FOOD);
                String name = ingr.getString(JSON_NAME);
                String qty = ingr.getString(JSON_QTY);
                String unit = ingr.getString(JSON_UNIT);

                ContentValues values = new ContentValues();

                values.put(IngrEntry.COLUMN_ID_FOOD, id_food);
                values.put(IngrEntry.COLUMN_NAME, name);
                values.put(IngrEntry.COLUMN_QTY, qty);
                values.put(IngrEntry.COLUMN_UNIT, unit);

                cVVector.add(values);

            }
            int inserted = 0;

//            delete database
            int rowdeleted= mContext.getContentResolver().delete(IngrEntry.CONTENT_URI, null, null);

//            // add to database
            Log.i(LOG_TAG, "Creando registros en base de datos. Tabla Ingredientes ");
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);

                inserted = mContext.getContentResolver().bulkInsert(IngrEntry.CONTENT_URI, cvArray);
                Log.i(LOG_TAG, "Registros nuevos creados en Tabla Ingredientes: "+inserted);
            }




        }
        catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

    }


    @Override
    protected String[] doInBackground(String... params) {


        BufferedReader reader = null;

        String JsonStrFood = null;
        String JsonStrIngr = null;

        try {


            InputStream inputStream = mContext.getAssets().open("jsonfooddb.json");
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            JsonStrFood = buffer.toString();

            //Log.i(LOG_TAG, "Buffer " + JsonStr);

            getDataFoodFromJson(JsonStrFood);

            Log.i(LOG_TAG, "Leyendo registros Ingredientes!!!!!!! ");
            InputStream inputStreamIngr = mContext.getAssets().open("ingredientsdb.json");
            StringBuffer bufferIngr = new StringBuffer();
            if (inputStreamIngr == null) {
                return null;
            }

            reader = null;
            reader = new BufferedReader(new InputStreamReader(inputStreamIngr));

            String  lineIngr;
            while ((lineIngr = reader.readLine()) != null) {
                bufferIngr.append(lineIngr + "\n");
            }

            if (bufferIngr.length() == 0) {

                return null;
            }

            JsonStrIngr = bufferIngr.toString();

            //Log.i(LOG_TAG, "Buffer Ingredients " + JsonStrIngr);

            getDataIngrFromJson(JsonStrIngr);



        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error ", e);
        } finally {

            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }


        return null;
    }


}