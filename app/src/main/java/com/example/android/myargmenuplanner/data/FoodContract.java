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

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static android.R.attr.id;

public class FoodContract {


    public static final String CONTENT_AUTHORITY = "com.example.android.myargmenuplanner";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
            //  content://com.example.android.myargmenuplanner

    public static final String PATH_FOOD = "foods";
    public static final String PATH_INGR = "ingredients";
    public static final String PATH_MENU = "menu";

    public static final class FoodEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FOOD).build();
                //  content://com.example.android.myargmenuplanner/foods

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FOOD;


        // Table name
        public static final String TABLE_NAME = "food";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE_ID = "image_id";
        public static final String COLUMN_DESCRIPTION = "description";

        // content://CONTENT_AUTHORITY/foods
        public static Uri buildFoodUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getFoodIDbyUri(Uri uri) {

            return uri.getPathSegments().get(1);
        }


    }

    public static final class IngrEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGR).build();
        //  content://com.example.android.myargmenuplanner/ingr

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INGR;


        // Table name
        public static final String TABLE_NAME = "ingredients";
        public static final String COLUMN_ID_FOOD = "id_food";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_QTY = "qty";
        public static final String COLUMN_UNIT = "unit";

        // content://CONTENT_AUTHORITY/foods
        public static Uri buildIngrByFoodUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getIngredientsIDbyUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }


    }

    public static final class MenuEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MENU).build();
        //  CONTENT_URI = content://com.example.android.myargmenuplanner/menu

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MENU;


        // Table name
        public static final String TABLE_NAME = "menu";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_LUNCH = "lunch";
        public static final String COLUMN_DINNER = "dinner";
        public static final String COLUMN_WEEK = "week";

        // content://CONTENT_AUTHORITY/menu
        public static Uri buildMenuByWeekUri(String week) {

            return CONTENT_URI.buildUpon().appendPath(week).build();
        }

        public static String getMenuIDbyUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }


    }







}