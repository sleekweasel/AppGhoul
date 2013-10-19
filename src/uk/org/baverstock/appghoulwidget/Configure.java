package uk.org.baverstock.appghoulwidget;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.*;

/**
 * Displays a list of application launchers, click causes configuration.
 */

public class Configure extends Activity {

    private AlertDialog dialog;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applist);

        int widgetId = GhoulInfo.getWidgetIdFromExtras(this);

        list = (ListView) findViewById(R.id.list);

        GhoulInfoArrayAdapter adapter = new GhoulInfoArrayAdapter(this, widgetId, list);
       adapter.collectLaunchers();
        list.setEnabled(false);
        list.setAdapter(adapter);
        dialog = new AlertDialog.Builder(this).setMessage("Loading...").show();
    }

    public void collectionComplete() {
        list.setFastScrollEnabled(true);
        list.setEnabled(true);
        dialog.dismiss();
    }
}
