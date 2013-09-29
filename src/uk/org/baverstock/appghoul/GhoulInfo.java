package uk.org.baverstock.appghoul;

import android.content.pm.ResolveInfo;

/**
 * Holds ghoulish information: Title, class, drawable, etc.
 */

public class GhoulInfo {
    private ResolveInfo resolveInfo;
    private String displayTitle;
    private String className;
    private String aPackage;

    public void setResolveInfo(ResolveInfo resolveInfo) {
        this.resolveInfo = resolveInfo;
    }

    public void setDisplayTitle(String displayTitle) {
        this.displayTitle = displayTitle;
    }

    public void setPackage(String aPackage) {
        this.aPackage = aPackage;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public ResolveInfo getResolveInfo() {
        return resolveInfo;
    }


    public String getDisplayTitle() {
        return displayTitle;
    }

    public String getPackage() {
        return aPackage;
    }

    public String getClassName() {
        return className;
    }
}
