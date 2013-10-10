package uk.org.baverstock.appghoul;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Arrays;

/**
 * When widgets appear, disappear, or need refreshing.
 */

public class AppGhoulWidgetProvider extends AppWidgetProvider {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.e("AppGhoulWidgetProvider", "onReceive...");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.e("AppGhoulWidgetProvider", "onDeleted..." + Arrays.toString(appWidgetIds));
    }

    @Override
    public void onEnabled(Context context) {
        Log.e("AppGhoulWidgetProvider", "onEnabled...");
    }

    @Override
    public void onDisabled(Context context) {
        Log.e("AppGhoulWidgetProvider", "onDisabled...");
    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        Log.e("AppGhoulWidgetProvider", "onPeekservice...");
        return super.peekService(myContext, service);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.w("AppGhoulWidgetProvider", "onUpdate..." + appWidgetIds.length);

        WidgetUpdater.updateWidgetsFromPrefs(context, appWidgetManager, appWidgetIds);
    }
}
