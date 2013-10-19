package uk.org.baverstock.appghoulwidget;

import android.appwidget.AppWidgetManager;
import android.content.*;

/**
 * Re-scans widget intents for viability on package un/install.
 */

public class WidgetRescanner extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName provider = new ComponentName(context, AppGhoulWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(provider);
        WidgetUpdater.updateWidgetsFromPrefs(context, appWidgetManager, appWidgetIds);
    }
}
