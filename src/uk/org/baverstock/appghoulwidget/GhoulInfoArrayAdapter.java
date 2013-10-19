package uk.org.baverstock.appghoulwidget;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
* Presents each ghoul as a list item.
*/
class GhoulInfoArrayAdapter extends ArrayAdapter<GhoulInfo> implements SectionIndexer {
    private final GridView list;
    private final PackageManager packageManager;
    private final Configure context;
    private int widgetId;
    private String[] sectionHeading;
    private Integer[] appIndexForSectionHeading;

    public GhoulInfoArrayAdapter(Configure context, int widgetId, GridView list) {
        super(context, 0);
        this.context = context;
        this.list = list;
        this.widgetId = widgetId;
        this.packageManager = context.getPackageManager();
    }

    void collectLaunchers() {
        new Thread(new PackageGatherer(), "packageGatherer").start();
    }

    GhoulInfo convertToGhoulInfo(ResolveInfo resolveInfo) {
        GhoulInfo ghoul = new GhoulInfo();
        ghoul.setResolveInfo(resolveInfo);
        ghoul.setDisplayTitle((String) resolveInfo.loadLabel(packageManager));

        Intent launcherIntent = new Intent(Intent.ACTION_MAIN, null);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        launcherIntent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);

        ghoul.setIntent(launcherIntent);
        return ghoul;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final GhoulInfo info = getItem(position);

        View item = convertView != null ? convertView : getInflatedView();

        ImageView icon = (ImageView) item.findViewById(R.id.wicon);
        TextView name = (TextView) item.findViewById(R.id.title);

        if (icon == null || name == null) {
            item = getInflatedView();
            icon = (ImageView) item.findViewById(R.id.wicon);
            name = (TextView) item.findViewById(R.id.title);
        }

        icon.setImageDrawable(info.getResolveInfo().loadIcon(packageManager));
        name.setText(info.getDisplayTitle());

        item.setTag(info);

        SetConfigOnClick l = new SetConfigOnClick(context, widgetId);
        item.setOnClickListener(l);
        item.setLongClickable(true);
        item.setOnLongClickListener(l);

        return item;
    }

    private View getInflatedView() {
        return LayoutInflater.from(context).inflate(R.layout.widget, list, false);
    }

    public void createFastThumbIndex() {
        ArrayList<String> sectionInitials = new ArrayList<String>();
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        for (int i = 0, ix = 0; i < getCount(); ++i) {
            GhoulInfo ghoul = getItem(i);
            String initial = ghoul.getDisplayTitle().toLowerCase().substring(0, 1);
            if (ix == 0 || (
//                    initial.compareToIgnoreCase("a") >= 0 &&
                            sectionInitials.get(ix-1).compareToIgnoreCase(initial) < 0)) {
//                if (initial.compareToIgnoreCase("a") < 0) {
//                    initial = "<A";
//                }
//                if (initial.compareToIgnoreCase("z") > 0) {
//                    initial = ">Z";
//                    i = getCount();
//                }
                sectionInitials.add(ix, initial);
                sectionIndices.add(ix, i);
                ++ix;
            }
        }
        sectionHeading = sectionInitials.toArray(new String[sectionInitials.size()]);
        appIndexForSectionHeading = sectionIndices.toArray(new Integer[sectionInitials.size()]);
    }

    @Override
    public String[] getSections() {
        return sectionHeading;
    }

    @Override
    public int getPositionForSection(int i) {
        return appIndexForSectionHeading[i];
    }

    @Override
    public int getSectionForPosition(int i) {
        int length = appIndexForSectionHeading.length - 1;
        for (int section = 0; section < length; ++section) {
            if (i >= appIndexForSectionHeading[section]) {
                return section;
            }
        }
        return length - 1;
    }

    private class PackageGatherer implements Runnable {
        @Override
        public void run() {
            final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> pkgAppsList = packageManager.queryIntentActivities(mainIntent, 0);
            for (ResolveInfo resolveInfo : pkgAppsList) {
                final GhoulInfo ghoul = convertToGhoulInfo(resolveInfo);
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        add(ghoul);
                    }
                });
            }
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sort(new Comparator<GhoulInfo>() {
                        @Override
                        public int compare(GhoulInfo resolveInfo, GhoulInfo resolveInfo2) {
                            return resolveInfo.getDisplayTitle().compareToIgnoreCase(
                                    resolveInfo2.getDisplayTitle());
                        }
                    });
                    createFastThumbIndex();
                    context.collectionComplete();
                    Toast.makeText(context, "Long press to retitle a launcher", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
