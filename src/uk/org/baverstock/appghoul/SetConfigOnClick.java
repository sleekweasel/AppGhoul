package uk.org.baverstock.appghoul;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;

import java.util.zip.Inflater;

/**
* Clicking on an entry configures that entry.
*/
class SetConfigOnClick implements View.OnClickListener, View.OnLongClickListener {

    private final Configure activity;
    private int widgetId;

    public SetConfigOnClick(Configure activity, int widgetId) {
        this.activity = activity;
        this.widgetId = widgetId;
    }

    @Override
    public void onClick(View view) {
        GhoulInfo info = (GhoulInfo) view.getTag();

        updateGhoulWidget(info, activity, widgetId);

        Intent widgetIntent = new Intent();
        widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        activity.setResult(Activity.RESULT_OK, widgetIntent);
        activity.finish();
    }

    @Override
    public boolean onLongClick(View view) {
        final GhoulInfo info = (GhoulInfo) view.getTag();
        final EditText editText = new EditText(activity);
        editText.setText(info.getDisplayTitle());
        new AlertDialog.Builder(activity)
                .setView(editText)
                .setPositiveButton("Retitle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        info.setDisplayTitle(editText.getText().toString());
                        updateGhoulWidget(info, activity, widgetId);

                        Intent widgetIntent = new Intent();
                        widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
                        activity.setResult(Activity.RESULT_OK, widgetIntent);
                        activity.finish();
                    }
                }).show();
        return true;
    }

    public static void updateGhoulWidget(GhoulInfo info, Context context, int widgetId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN, null);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        launcherIntent.setClassName(info.getPackage(), info.getClassName());

        PendingIntent pending = PendingIntent.getActivity(context, 0, launcherIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget, pending);

        CharSequence title = info.getDisplayTitle();
        remoteViews.setCharSequence(R.id.title, "setText", title);

        Drawable drawable = info.getResolveInfo().loadIcon(context.getPackageManager());
        Bitmap bitmap = getBitmapFromDrawable(drawable);
        remoteViews.setBitmap(R.id.wicon, "setImageBitmap", bitmap);

        appWidgetManager.updateAppWidget(widgetId, remoteViews);

        SharedPreferences.Editor prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).edit();
        prefs.putString("intent." + widgetId, launcherIntent.toURI().toString());
        prefs.putString("title." + widgetId, title.toString());
        prefs.commit();
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
