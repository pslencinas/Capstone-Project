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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.myargmenuplanner.data.FoodContract.FoodEntry;
import com.example.android.myargmenuplanner.data.FoodContract.IngrEntry;
import com.example.android.myargmenuplanner.data.FoodContract.MenuEntry;

public class FoodDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 10;

    static final String DATABASE_NAME = "menuplanner.db";

    public FoodDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

       final String SQL_CREATE_FOOD_TABLE = "CREATE TABLE " + FoodEntry.TABLE_NAME + " (" +

               FoodEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
               FoodEntry.COLUMN_ID + " INTEGER NOT NULL, " +
               FoodEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
               FoodEntry.COLUMN_IMAGE_ID + " TEXT NOT NULL, " +
               FoodEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
               FoodEntry.COLUMN_TIME + " TEXT NOT NULL " +

               ");";

        final String SQL_CREATE_INGR_TABLE = "CREATE TABLE " + IngrEntry.TABLE_NAME + " (" +

                IngrEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                IngrEntry.COLUMN_ID_FOOD + " INTEGER NOT NULL, " +
                IngrEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                IngrEntry.COLUMN_QTY + " INTEGER NOT NULL, " +
                IngrEntry.COLUMN_UNIT + " TEXT NOT NULL " +

                ");";

        final String SQL_CREATE_MENU_TABLE = "CREATE TABLE " + MenuEntry.TABLE_NAME + " (" +

                MenuEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MenuEntry.COLUMN_DATE + " DATE NOT NULL, " +
                MenuEntry.COLUMN_LUNCH + " TEXT NOT NULL, " +
                MenuEntry.COLUMN_ID_LUNCH + " INTEGER NOT NULL, " +
                MenuEntry.COLUMN_DINNER + " TEXT NOT NULL, " +
                MenuEntry.COLUMN_ID_DINNER + " INTEGER NOT NULL " +

                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_FOOD_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_INGR_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MENU_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FoodEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + IngrEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MenuEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
