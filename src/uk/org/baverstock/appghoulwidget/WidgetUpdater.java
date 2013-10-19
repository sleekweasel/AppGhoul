package uk.org.baverstock.appghoulwidget;

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
import android.util.Log;
import android.widget.RemoteViews;

import java.util.List;

/**
 * Does widget updating
 */

public class WidgetUpdater {
    static public void updateWidgetsFromPrefs(Context context, AppWidgetManager appWidgetManager, int... appWidgetIds) {
        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

        PackageManager packageManager = context.getPackageManager();

        for (int appWidgetId : appWidgetIds) {
            Log.d("WidgetUpdater", "Updating widget " + appWidgetId);
            GhoulInfo ghoul = GhoulInfo.ghoulFromPrefs(prefs, appWidgetId);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

            Intent launchIntent = ghoul.getIntent();

            Bitmap bitmap = getBitmapForIntent(packageManager, launchIntent);
            if (bitmap == null) {
                bitmap = getBitmapFromDrawable(context.getResources().getDrawable(R.drawable.icon));
            }
            views.setBitmap(R.id.wicon, "setImageBitmap", bitmap);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.wicon, pendingIntent);

            views.setCharSequence(R.id.title, "setText", ghoul.getDisplayTitle());

            setControlIntent(context, appWidgetId, views);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private static void setControlIntent(Context context, int appWidgetId, RemoteViews views) {
        // Set different data, for unique pending intent (even though I actually use extras)
        Intent controlIntent = new Intent(
                null,
                android.net.Uri.parse("differentiate://" + appWidgetId),
                context.getApplicationContext(),
                ControlsChoiceActivity.class);
        GhoulInfo.setWidgetAppIdExtra(controlIntent, appWidgetId);
        PendingIntent pendingIntentControl = PendingIntent.getActivity(context, 0, controlIntent, 0);
        views.setOnClickPendingIntent(R.id.title, pendingIntentControl);
    }

    private static Bitmap getBitmapForIntent(PackageManager packageManager, Intent intent) {
        Bitmap bitmap = null;
        List<ResolveInfo> pkgAppsList = packageManager.queryIntentActivities(intent, 0);
        // Maybe if there're multiple, cook them into a pretty display!
        if (pkgAppsList.size() >= 1) {
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
