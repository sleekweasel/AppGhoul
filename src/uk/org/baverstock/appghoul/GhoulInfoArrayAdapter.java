package uk.org.baverstock.appghoul;

import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;

/**
* Presents each ghoul as a list item.
*/
class GhoulInfoArrayAdapter extends ArrayAdapter<GhoulInfo> implements SectionIndexer {
    private final ListView list;
    private final PackageManager packageManager;
    private final Configure context;
    private int widgetId;
    private String[] sectionLetters;
    private Integer[] ghoulPositionForSectionHead;

    public GhoulInfoArrayAdapter(Configure context, int widgetId, ListView list) {
        super(context, 0);
        this.context = context;
        this.list = list;
        this.widgetId = widgetId;
        this.packageManager = context.getPackageManager();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final GhoulInfo info = getItem(position);

        View item = convertView != null ? convertView : getInflatedView();

        ImageView icon = (ImageView) item.findViewById(R.id.icon);
        TextView name = (TextView) item.findViewById(R.id.name);

        if (icon == null || name == null) {
            item = getInflatedView();
            icon = (ImageView) item.findViewById(R.id.icon);
            name = (TextView) item.findViewById(R.id.name);
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
        return LayoutInflater.from(context).inflate(R.layout.listitem, list, false);
    }

    public void indexFastThumb() {
        ArrayList<String> letters = new ArrayList<String>();
        ArrayList<Integer> sections = new ArrayList<Integer>();
        for (int i = 0, ix = 0; i < getCount(); ++i) {
            GhoulInfo ghoul = getItem(i);
            String initial = ghoul.getDisplayTitle().toLowerCase().substring(0, 1);
            if (ix == 0 || (initial.compareToIgnoreCase("a") >= 0
                            && letters.get(ix-1).compareToIgnoreCase(initial) < 0)) {
                if (initial.compareToIgnoreCase("a") < 0) {
                    initial = "<A";
                }
                if (initial.compareToIgnoreCase("z") > 0) {
                    initial = ">Z";
                    i = getCount();
                }
                letters.add(ix, initial);
                sections.add(ix, i);
                ++ix;
            }
        }
        sectionLetters = letters.toArray(new String[letters.size()]); // <a, a..z, >z
        ghoulPositionForSectionHead = sections.toArray(new Integer[letters.size()]);
    }

    @Override
    public String[] getSections() {
        return sectionLetters;
    }

    @Override
    public int getPositionForSection(int i) {
        return ghoulPositionForSectionHead[i];
    }

    @Override
    public int getSectionForPosition(int i) {
        int length = ghoulPositionForSectionHead.length - 1;
        for (int section = 0; section < length; ++section) {
            if (i >= ghoulPositionForSectionHead[section]) {
                return section;
            }
        }
        return length - 1;
    }
}
