package uk.org.baverstock.appghoul;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

/**
* Clicking on an entry configures that entry.
*/
class ConfigListOnClickListener implements View.OnClickListener {

    private final Configure activity;
    private int widgetId;

    public ConfigListOnClickListener(Configure activity, int widgetId) {
        this.activity = activity;
        this.widgetId = widgetId;
    }

    @Override
    public void onClick(View view) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(activity);
        RemoteViews remoteViews = new RemoteViews(activity.getPackageName(), R.layout.widget);

        GhoulInfo info = (GhoulInfo) view.getTag();

        Intent launcherIntent = new Intent(Intent.ACTION_MAIN, null);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        launcherIntent.setClassName(info.getPackage(), info.getClassName());

        Log.e("ConfigListOnClickListener", "Title." + widgetId);

        PendingIntent pending = PendingIntent.getActivity(this.activity, 0, launcherIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget, pending);

        CharSequence title = info.getDisplayTitle();
        remoteViews.setCharSequence(R.id.title, "setText", title);

        Drawable drawable = info.getResolveInfo().loadIcon(activity.getPackageManager());
        Bitmap bitmap = getBitmapFromDrawable(drawable);
        remoteViews.setBitmap(R.id.wicon, "setImageBitmap", bitmap);

        appWidgetManager.updateAppWidget(widgetId, remoteViews);

        SharedPreferences.Editor prefs = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE).edit();
        prefs.putString("intent." + widgetId, launcherIntent.toURI().toString());
        prefs.putString("title." + widgetId, title.toString());
        prefs.commit();

        Intent widgetIntent = new Intent();
        widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        activity.setResult(Activity.RESULT_OK, widgetIntent);
        activity.finish();
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
