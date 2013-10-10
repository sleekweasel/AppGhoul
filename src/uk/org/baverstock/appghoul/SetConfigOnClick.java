package uk.org.baverstock.appghoul;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.URISyntaxException;

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

        updateGhoulPrefsAndWidget(info, activity, widgetId);

        setActivityResult();
    }

    private void setActivityResult() {
        Intent widgetIntent = new Intent();
        widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        activity.setResult(Activity.RESULT_OK, widgetIntent);
        activity.finish();
    }

    @Override
    public boolean onLongClick(View view) {
        final GhoulInfo info = (GhoulInfo) view.getTag();
        View custom = LayoutInflater.from(activity).inflate(R.layout.edit, null);

        final EditText editText = (EditText) custom.findViewById(R.id.edit_title);
        final EditText editUrl = (EditText) custom.findViewById(R.id.edit_url);
        final Button button = (Button) custom.findViewById(R.id.edit_repurpose);

        editText.setText(info.getDisplayTitle());

        editUrl.setText(info.getIntent().toUri(Intent.URI_INTENT_SCHEME));

        editUrl.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                try {
                    Intent.parseUri(editUrl.getText().toString(), Intent.URI_INTENT_SCHEME);
                    button.setEnabled(true);
                    editUrl.setBackgroundColor(Color.WHITE);
                } catch (URISyntaxException e) {
                    button.setEnabled(false);
                    editUrl.setBackgroundColor(Color.RED);
                }
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    info.setDisplayTitle(editText.getText().toString());
                    info.setIntent(Intent.parseUri(editUrl.getText().toString(), 0));

                    updateGhoulPrefsAndWidget(info, activity, widgetId);

                    setActivityResult();
                } catch (URISyntaxException e) {
                }
            }
        });

        new AlertDialog.Builder(activity).setView(custom).show();
        return true;
    }

    public static void updateGhoulPrefsAndWidget(GhoulInfo info, Context context, int widgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(
                context.getPackageName(),Context.MODE_PRIVATE).edit();
        prefs.putString("intent." + widgetId, info.getIntent().toURI().toString());
        prefs.putString("title." + widgetId, info.getDisplayTitle().toString());
        prefs.commit();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        WidgetUpdater.updateWidgetsFromPrefs(context, appWidgetManager, widgetId);
    }
}
