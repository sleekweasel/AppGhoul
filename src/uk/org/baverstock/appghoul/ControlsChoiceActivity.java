package uk.org.baverstock.appghoul;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

/**
 * Presents a dialogue to offer control options:
 * 1. Open app details (if the intent )
 * 2. Open long-press edit dialogue (including colours, eventually)
 * 3. Perhaps jump to launcher-chooser list. Maybe. Seems a bit pointless though.
 */

public class ControlsChoiceActivity extends Activity {

    private GhoulInfo ghoul;
    private int widgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        widgetId = GhoulInfo.getWidgetIdFromExtras(this);

        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            Toast.makeText(getApplicationContext(), "No widget to manage!", Toast.LENGTH_SHORT).show();
            finish();
        }

        SharedPreferences preferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        ghoul = GhoulInfo.ghoulFromPrefs(preferences, widgetId);

        String ghoulPackage = getPlatformCarefully();
        if (ghoulPackage == null) {
            onReconfigure(null);
        }

        setContentView(R.layout.controls_choice);
    }

    private String getPlatformCarefully() {
        Intent intent = ghoul.getIntent();
        String aPackage = intent.getPackage();
        aPackage = aPackage != null ? aPackage : intent.getComponent().getPackageName();
        return aPackage;
    }

    public void onAppDetails(View unused_view) {
        String aPackage = getPlatformCarefully();
        showInstalledAppDetails(this, aPackage);
        finish();
    }

    public void onReconfigure(View unused_view) {
        Intent reconfigure = new Intent(this, ReconfigureWidget.class);
        GhoulInfo.setWidgetAppIdExtra(reconfigure, widgetId);
        startActivity(reconfigure);
        finish();
    }

    private static final String SCHEME = "package";

    private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";

    private static final String APP_PKG_NAME_22 = "pkg";

    private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";

    private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";

    public static void showInstalledAppDetails(Context context, String packageName) {
        Intent intent = new Intent();
        final int apiLevel = Build.VERSION.SDK_INT;
        if (apiLevel >= 9) { // above 2.3
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts(SCHEME, packageName, null);
            intent.setData(uri);
        } else { // below 2.3
            final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22
                    : APP_PKG_NAME_21);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName(APP_DETAILS_PACKAGE_NAME,
                    APP_DETAILS_CLASS_NAME);
            intent.putExtra(appPkgName, packageName);
        }
        context.startActivity(intent);
    }
}
