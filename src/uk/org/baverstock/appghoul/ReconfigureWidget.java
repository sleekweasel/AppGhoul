package uk.org.baverstock.appghoul;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.URISyntaxException;

import static uk.org.baverstock.appghoul.SetConfigOnClick.updateGhoulPrefsAndWidget;

/**
 * Allows the user to edit the title and intent.
 */

public class ReconfigureWidget extends Activity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int widgetId = GhoulInfo.getWidgetIdFromExtras(this);

        View custom = LayoutInflater.from(this).inflate(R.layout.edit, null);
        setContentView(custom);

        final EditText editText = (EditText) custom.findViewById(R.id.edit_title);
        final EditText editUrl = (EditText) custom.findViewById(R.id.edit_url);
        final Button button = (Button) custom.findViewById(R.id.edit_repurpose);

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        final GhoulInfo info = GhoulInfo.ghoulFromPrefs(sharedPreferences, widgetId);

        editText.setText(info.getDisplayTitle());

        intent = info.getIntent();
        editUrl.setText(intent.toUri(Intent.URI_INTENT_SCHEME));

        editUrl.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                try {
                    ReconfigureWidget.this.intent = Intent.parseUri(editUrl.getText().toString(), Intent.URI_INTENT_SCHEME);
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
                    info.setDisplayTitle(editText.getText().toString());
                    info.setIntent(ReconfigureWidget.this.intent);

                    updateGhoulPrefsAndWidget(info, ReconfigureWidget.this, widgetId);

                    finish();
            }
        });
    }
}
