package uk.org.baverstock.appghoulwidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

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

    private void setActivityResult() {
        Intent widgetIntent = new Intent();
        GhoulInfo.setWidgetAppIdExtra(widgetIntent, widgetId);
        activity.setResult(Activity.RESULT_OK, widgetIntent);
        activity.finish();
    }

    @Override
    public void onClick(View view) {
        GhoulInfo info = (GhoulInfo) view.getTag();

        updateGhoulPrefsAndWidget(info, activity, widgetId);

        setActivityResult();
    }

    @Override
    public boolean onLongClick(View view) {
        GhoulInfo info = (GhoulInfo) view.getTag();
        updateGhoulPrefsAndWidget(info, activity, widgetId);
        Intent reconfigure = new Intent(activity.getApplicationContext(), ReconfigureWidget.class);
        GhoulInfo.setWidgetAppIdExtra(reconfigure, widgetId);
        activity.startActivity(reconfigure);
        setActivityResult();
        return true;
    }

    public static void updateGhoulPrefsAndWidget(GhoulInfo info, Context context, int widgetId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);

        GhoulInfo.ghoulToPrefs(info, widgetId, sharedPreferences);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        WidgetUpdater.updateWidgetsFromPrefs(context, appWidgetManager, widgetId);
    }
}
