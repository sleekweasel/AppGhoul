package uk.org.baverstock.appghoul;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.widget.Toast;

import java.net.URISyntaxException;

/**
 * Holds ghoulish information: Title, class, drawable, etc.
 */

public class GhoulInfo {
    private static final String INTENT = "intent.";
    private static final String TITLE = "title.";

    private ResolveInfo resolveInfo;
    private String displayTitle;
    private Intent intent;

    public static void ghoulToPrefs(GhoulInfo info, int widgetId, SharedPreferences sharedPreferences) {
        SharedPreferences.Editor prefs = sharedPreferences.edit();
        prefs.putString(INTENT + widgetId, info.getIntent().toURI().toString());
        prefs.putString(TITLE + widgetId, info.getDisplayTitle().toString());
        prefs.commit();
    }

    public static GhoulInfo ghoulFromPrefs(SharedPreferences prefs, int appWidgetId) {
        GhoulInfo ghoul = new GhoulInfo();
        String title = prefs.getString(TITLE + appWidgetId, "UndeadApp " + appWidgetId);
        String intentUri = prefs.getString(INTENT + appWidgetId, "about:");
        Intent intent;
        try {
            intent = Intent.getIntent(intentUri);
        } catch (URISyntaxException e) {
            intent = new Intent();
            intent.putExtra("badIntent", intentUri);
        }
        ghoul.setDisplayTitle(title);
        ghoul.setIntent(intent);
        return ghoul;
    }

    public static void setWidgetAppIdExtra(Intent intent, int widgetId) {
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
    }

    public static int getWidgetIdFromExtras(Activity activity) {
        Bundle extras = activity.getIntent().getExtras();
        int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
        if (extras != null) {
            widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        } else {
            return widgetId;
        }
        return widgetId;
    }

    public void setResolveInfo(ResolveInfo resolveInfo) {
        this.resolveInfo = resolveInfo;
    }

    public ResolveInfo getResolveInfo() {
        return resolveInfo;
    }


    public void setDisplayTitle(String displayTitle) {
        this.displayTitle = displayTitle;
    }

    public String getDisplayTitle() {
        return displayTitle;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}
