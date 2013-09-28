package uk.org.baverstock.appghoul;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Does...
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

        Collections.sort(pkgAppsList, new ResolveInfoComparator());

        final ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(new ArrayAdapter<ResolveInfo>(this, 0, pkgAppsList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View item = LayoutInflater.from(Configure.this).inflate(R.layout.listitem, list, false);
                ImageView icon = (ImageView) item.findViewById(R.id.icon);
                ResolveInfo info = getItem(position);
                icon.setImageDrawable(info.loadIcon(packageManager));
                TextView name = (TextView) item.findViewById(R.id.name);
                name.setText(info.loadLabel(packageManager) + "\n" + info.activityInfo.packageName + "\n" + info.activityInfo.name);
                item.setTag(info);
                item.setOnClickListener(new MyOnClickListener());
                return item;
            }
        });
    }

    private static class ResolveInfoComparator implements Comparator<ResolveInfo> {
        @Override
        public int compare(ResolveInfo resolveInfo, ResolveInfo resolveInfo2) {
            return resolveInfo.activityInfo.packageName.compareToIgnoreCase(resolveInfo2.activityInfo.packageName);
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(Configure.this);
            RemoteViews remoteViews = new RemoteViews(Configure.this.getPackageName(), R.layout.widget);

            ResolveInfo info = (ResolveInfo) view.getTag();

            Intent launcherIntent = new Intent(Intent.ACTION_MAIN, null);
            launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            launcherIntent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
            PendingIntent activity = PendingIntent.getActivity(Configure.this, 0, launcherIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.thing, activity);
            remoteViews.setCharSequence(R.id.title, "setText", info.loadLabel(getPackageManager()));
            Drawable drawable = info.loadIcon(getPackageManager());

            int width = drawable.getIntrinsicWidth();
            width = width > 0 ? width : 1;
            int height = drawable.getIntrinsicHeight();
            height = height > 0 ? height : 1;

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            remoteViews.setBitmap(R.id.thing, "setImageBitmap", bitmap);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);

            //

            Intent widgetIntent = new Intent();
            widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            setResult(RESULT_OK, widgetIntent);
            finish();
        }
    }
}
