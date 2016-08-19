package com.example.android.myargmenuplanner.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.example.android.myargmenuplanner.MainActivity;
import com.example.android.myargmenuplanner.R;
import com.example.android.myargmenuplanner.TabFragmentTW;


/**
 * Provider for a scrollable weather detail widget
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)

public class WidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, TodayWidgetIntentService.class));
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        context.startService(new Intent(context, TodayWidgetIntentService.class));
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        if (TabFragmentTW.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            context.startService(new Intent(context, TodayWidgetIntentService.class));
        }
    }
}
