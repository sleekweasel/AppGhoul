package uk.org.baverstock.appghoul;

import android.content.Intent;
import android.content.pm.ResolveInfo;

/**
 * Holds ghoulish information: Title, class, drawable, etc.
 */

public class GhoulInfo {
    private ResolveInfo resolveInfo;
    private String displayTitle;
    private Intent intent;

    public void setResolveInfo(ResolveInfo resolveInfo) {
        this.resolveInfo = resolveInfo;
    }

    public void setDisplayTitle(String displayTitle) {
        this.displayTitle = displayTitle;
    }

    public ResolveInfo getResolveInfo() {
        return resolveInfo;
    }


    public String getDisplayTitle() {
        return displayTitle;
    }

    public Intent getIntent() {
        /*
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN, null);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        launcherIntent.setClassName(info.getPackage(), info.getClassName());
        */
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}
