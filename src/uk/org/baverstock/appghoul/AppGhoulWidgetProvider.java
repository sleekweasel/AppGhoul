package uk.org.baverstock.appghoul;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static uk.org.baverstock.appghoul.SetConfigOnClick.getBitmapFromDrawable;

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
        final int N = appWidgetIds.length;

        Log.w("AppGhoulWidgetProvider", "onUpdate..." + N);

        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

        PackageManager packageManager = context.getPackageManager();

        for (int appWidgetId : appWidgetIds) {
            try {
                String intentUri = prefs.getString("intent." + appWidgetId, "about:");

                Intent intent = Intent.getIntent(intentUri);

                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

                List<ResolveInfo> pkgAppsList = packageManager.queryIntentActivities(intent, 0);
                if (pkgAppsList.size() == 1) {
                    Drawable drawable = pkgAppsList.get(0).loadIcon(packageManager);
                    Bitmap bitmap = getBitmapFromDrawable(drawable);
                    views.setBitmap(R.id.wicon, "setImageBitmap", bitmap);
                }

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                views.setOnClickPendingIntent(R.id.widget, pendingIntent);

                String title = prefs.getString("title." + appWidgetId, "UndeadApp " + appWidgetId);
                views.setCharSequence(R.id.title, "setText", title);

                appWidgetManager.updateAppWidget(appWidgetId, views);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}
