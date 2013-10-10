package uk.org.baverstock.appghoul;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.widget.*;

import java.util.Comparator;
import java.util.List;

/**
 * Displays a list of application launchers, click causes configuration.
 */

public class Configure extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applist);

        int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        final ListView list = (ListView) findViewById(R.id.list);

        GhoulInfoArrayAdapter adapter = new GhoulInfoArrayAdapter(this, widgetId, list);
        collectLaunchers(adapter, list);
        list.setEnabled(false);
        list.setAdapter(adapter);
    }

    private void collectLaunchers(final GhoulInfoArrayAdapter adapter, final ListView list) {
        Runnable packageGatherer = new Runnable() {
            @Override
            public void run() {
                final PackageManager packageManager = getPackageManager();
                final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                List<ResolveInfo> pkgAppsList = packageManager.queryIntentActivities(mainIntent, 0);
                for (ResolveInfo resolveInfo : pkgAppsList) {
                    final GhoulInfo ghoul = convertToGhoulFood(resolveInfo, packageManager);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.add(ghoul);
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.sort(new Comparator<GhoulInfo>() {
                            @Override
                            public int compare(GhoulInfo resolveInfo, GhoulInfo resolveInfo2) {
                                return resolveInfo.getDisplayTitle().compareToIgnoreCase(resolveInfo2.getDisplayTitle());
                            }
                        });
                        adapter.indexFastThumb();
                        list.setFastScrollEnabled(true);
                        list.setEnabled(true);
                        Toast.makeText(Configure.this, "Long press to retitle a launcher", Toast.LENGTH_LONG).show();
                    }
                });
            }
        };
        new Thread(packageGatherer, "packageGatherer").start();
    }

    static GhoulInfo convertToGhoulFood(ResolveInfo resolveInfo, PackageManager packageManager) {
        GhoulInfo ghoul = new GhoulInfo();
        ghoul.setResolveInfo(resolveInfo);
        ghoul.setDisplayTitle((String) resolveInfo.loadLabel(packageManager));

        Intent launcherIntent = new Intent(Intent.ACTION_MAIN, null);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        launcherIntent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);

        ghoul.setIntent(launcherIntent);
        return ghoul;
    }

}
