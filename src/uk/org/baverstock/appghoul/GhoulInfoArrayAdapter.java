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

    public GhoulInfoArrayAdapter(Configure context, int widgetId, ListView list, List<GhoulInfo> ghoulAppsList) {
        super(context, 0, ghoulAppsList);
        this.context = context;
        this.list = list;
        this.widgetId = widgetId;
        this.packageManager = context.getPackageManager();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GhoulInfo info = getItem(position);

        View item = LayoutInflater.from(context).inflate(R.layout.listitem, list, false);
        ImageView icon = (ImageView) item.findViewById(R.id.icon);
        TextView name = (TextView) item.findViewById(R.id.name);

        icon.setImageDrawable(info.getResolveInfo().loadIcon(packageManager));
        name.setText(
                String.format("%s\n%s\n%s", info.getDisplayTitle(), info.getPackage(), info.getClassName()));
        item.setTag(info);
        item.setOnClickListener(new ConfigListOnClickListener(context, widgetId));
        return item;
    }
}
