package uk.org.baverstock.appghoul;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Does widget updating
 */

public class WidgetUpdater {
    static public void updateWidgetsFromPrefs(Context context, AppWidgetManager appWidgetManager, int... appWidgetIds) {
        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

        PackageManager packageManager = context.getPackageManager();

        for (int appWidgetId : appWidgetIds) {
            try {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

                setBitmapAndLaunchIntent(context, prefs, packageManager, appWidgetId, views);

                setTitle(prefs, appWidgetId, views);

                setControlIntent(context, appWidgetId, views);

                appWidgetManager.updateAppWidget(appWidgetId, views);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setBitmapAndLaunchIntent(
            Context context, SharedPreferences prefs, PackageManager packageManager, int appWidgetId,
            RemoteViews views
    ) throws URISyntaxException
    {
        String intentUri = prefs.getString("intent." + appWidgetId, "about:");

        Intent launchIntent = Intent.getIntent(intentUri);

        Bitmap bitmap = getBitmapForIntent(packageManager, launchIntent);
        if (bitmap != null) {
            views.setBitmap(R.id.wicon, "setImageBitmap", bitmap);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
        views.setOnClickPendingIntent(R.id.widget, pendingIntent);
    }

    private static void setTitle(SharedPreferences prefs, int appWidgetId, RemoteViews views) {
        String title = prefs.getString("title." + appWidgetId, "UndeadApp " + appWidgetId);
        views.setCharSequence(R.id.title, "setText", title);
    }

    private static void setControlIntent(Context context, int appWidgetId, RemoteViews views) {
        // Set different data, for unique pending intent (even though I actually use extras)
        Intent controlIntent = new Intent(null, android.net.Uri.parse("differentiate://"+appWidgetId), context.getApplicationContext(), Configure.class);
        controlIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntentControl = PendingIntent.getActivity(context, 0, controlIntent, 0);
        views.setOnClickPendingIntent(R.id.wcontrol, pendingIntentControl);
    }

    private static Bitmap getBitmapForIntent(PackageManager packageManager, Intent intent) {
        Bitmap bitmap = null;
        List<ResolveInfo> pkgAppsList = packageManager.queryIntentActivities(intent, 0);
        if (pkgAppsList.size() == 1) {
            Drawable drawable = pkgAppsList.get(0).loadIcon(packageManager);
            bitmap = getBitmapFromDrawable(drawable);
        }
        return bitmap;
    }

    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
