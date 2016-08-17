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

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class FoodProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FoodDbHelper mOpenHelper;

    static final int FOOD = 100;
    static final int FOOD_WITH_ID = 101;
    static final int INGR = 200;
    static final int INGR_WITH_ID = 201;
    static final int MENU = 300;
    static final int MENU_BTW_DATE = 301;

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FoodContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FoodContract.PATH_FOOD, FOOD);
        matcher.addURI(authority, FoodContract.PATH_FOOD + "/*", FOOD_WITH_ID);
        matcher.addURI(authority, FoodContract.PATH_INGR, INGR);
        matcher.addURI(authority, FoodContract.PATH_INGR + "/*", INGR_WITH_ID);
        matcher.addURI(authority, FoodContract.PATH_MENU, MENU);
        matcher.addURI(authority, FoodContract.PATH_MENU + "/*", MENU_BTW_DATE);

        return matcher;


    }


    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {

            case FOOD:
                return FoodContract.FoodEntry.CONTENT_TYPE;
            case FOOD_WITH_ID:
                return FoodContract.FoodEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new FoodDbHelper(getContext());
        return true;
    }

    //id = ?
    private static final String sFoodId = FoodContract.FoodEntry.COLUMN_ID + " = ? ";

    private Cursor getFoodById(Uri uri, String[] projection, String sortOrder) {

        String foodId = FoodContract.FoodEntry.getFoodIDbyUri(uri);

        return mOpenHelper.getReadableDatabase().query(
                FoodContract.FoodEntry.TABLE_NAME,
                projection,                                             // columnas a mostrar
                sFoodId,                                               // condicion WHERE
                new String[]{foodId},     // arg del WHERE
                null,
                null,
                sortOrder
        );
    }

    private static final String sIngrByFoodId = FoodContract.IngrEntry.COLUMN_ID_FOOD + " = ? ";

    private Cursor getIngrByFoodId(Uri uri, String[] projection, String sortOrder) {

        String foodId = FoodContract.IngrEntry.getIngredientsIDbyUri(uri);

        return mOpenHelper.getReadableDatabase().query(
                FoodContract.IngrEntry.TABLE_NAME,
                projection,                                             // columnas a mostrar
                sIngrByFoodId,                                          // condicion WHERE
                new String[]{foodId},                       // arg del WHERE
                null,
                null,
                sortOrder
        );
    }


    private static final String sMenuByDate = FoodContract.MenuEntry.COLUMN_DATE + " BETWEEN ? AND ?";

    private Cursor getMenuByDate(Uri uri, String[] projection, String[] arg, String sortOrder) {

        //String foodId = FoodContract.MenuEntry.getMenuIDbyUri(uri);

        return mOpenHelper.getReadableDatabase().query(
                FoodContract.MenuEntry.TABLE_NAME,                      //tabla
                projection,                                             // columnas a mostrar
                sMenuByDate,                                          // condicion WHERE
                arg,                               // arg del WHERE
                null,
                null,
                sortOrder
        );
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {

            // 'food'
            case FOOD: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FoodContract.FoodEntry.TABLE_NAME,      // tabla
                        projection,                             // columnas a mostrar
                        selection,                              // condicion del query
                        selectionArgs,                          // argumentos de la condicion
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            // "food/*"
            case FOOD_WITH_ID: {
                retCursor = getFoodById(uri, projection, sortOrder);
                break;
            }

            // 'ingr'
            case INGR: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FoodContract.IngrEntry.TABLE_NAME,      // tabla
                        projection,                             // columnas a mostrar
                        selection,                              // condicion del query
                        selectionArgs,                          // argumentos de la condicion
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            // "ingredients/*"
            case INGR_WITH_ID: {
                retCursor = getIngrByFoodId(uri, projection, sortOrder);
                break;
            }

            // 'menu'
            case MENU: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FoodContract.MenuEntry.TABLE_NAME,      // tabla
                        projection,                             // columnas a mostrar
                        selection,                              // condicion del query
                        selectionArgs,                          // argumentos de la condicion
                        null,
                        null,
                        sortOrder
                );
                break;
            }


            case MENU_BTW_DATE: {
                retCursor = getMenuByDate(uri, projection, selectionArgs, sortOrder);
                break;
            }


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FOOD: {

                long _id = db.insert(FoodContract.FoodEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FoodContract.FoodEntry.buildFoodUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case MENU: {

                long _id = db.insert(FoodContract.MenuEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FoodContract.FoodEntry.buildFoodUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case FOOD:
                rowsDeleted = db.delete(
                        FoodContract.FoodEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FOOD_WITH_ID:
                rowsDeleted = db.delete(
                        FoodContract.FoodEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case INGR:
                rowsDeleted = db.delete(
                        FoodContract.IngrEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MENU:
                rowsDeleted = db.delete(
                        FoodContract.MenuEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;

    }


    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case FOOD:

                rowsUpdated = db.update(FoodContract.FoodEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case FOOD_WITH_ID:
                rowsUpdated = db.update(FoodContract.FoodEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case MENU:
                rowsUpdated = db.update(FoodContract.MenuEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;

    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;
        switch (match) {
            case FOOD:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(FoodContract.FoodEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            case INGR:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(FoodContract.IngrEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            case MENU:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(FoodContract.MenuEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}