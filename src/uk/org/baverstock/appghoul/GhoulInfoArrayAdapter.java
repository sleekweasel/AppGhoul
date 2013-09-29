package uk.org.baverstock.appghoul;

import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
* Presents each ghoul as a list item.
*/
class GhoulInfoArrayAdapter extends ArrayAdapter<GhoulInfo> {
    private final ListView list;
    private final PackageManager packageManager;
    private final Configure context;
    private int widgetId;

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
        name.setText(String.format("%s\n%s\n%s", info.getDisplayTitle(), info.getPackage(), info.getClassName()));

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
}
