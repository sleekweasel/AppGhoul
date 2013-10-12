package uk.org.baverstock.appghoul;

import android.appwidget.AppWidgetManager;
import android.content.*;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.test.mock.MockPackageManager;

import java.util.List;

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
