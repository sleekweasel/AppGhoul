package uk.org.baverstock.appghoul;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.widget.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Displays a list of application launchers, click causes configuration.
 */

public class Configure extends Activity {
    private int widgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applist);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        final PackageManager packageManager = getPackageManager();
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = packageManager.queryIntentActivities(mainIntent, 0);

        List<GhoulInfo> ghoulAppsList = convertToGhoulFood(pkgAppsList, packageManager);
        Collections.sort(ghoulAppsList, new GhoulInfoComparator());

        final ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(new GhoulInfoArrayAdapter(this, widgetId, list, ghoulAppsList));
    }

    static List<GhoulInfo> convertToGhoulFood(List<ResolveInfo> pkgAppsList, PackageManager packageManager) {
        List<GhoulInfo> ghouls = new ArrayList<GhoulInfo>();
        for (ResolveInfo resolveInfo : pkgAppsList) {
            GhoulInfo ghoul = new GhoulInfo();
            ghoul.setResolveInfo(resolveInfo);
            ghoul.setDisplayTitle((String) resolveInfo.loadLabel(packageManager));
            ghoul.setPackage(resolveInfo.activityInfo.packageName);
            ghoul.setClassName(resolveInfo.activityInfo.name);
            ghouls.add(ghoul);
        }
        return ghouls;
    }

    private static class GhoulInfoComparator implements Comparator<GhoulInfo> {
        @Override
        public int compare(GhoulInfo resolveInfo, GhoulInfo resolveInfo2) {
            return resolveInfo.getDisplayTitle().compareToIgnoreCase(resolveInfo2.getDisplayTitle());
        }
    }
}
