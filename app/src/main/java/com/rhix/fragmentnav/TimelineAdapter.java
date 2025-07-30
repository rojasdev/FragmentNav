package com.rhix.fragmentnav;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TimelineAdapter extends ArrayAdapter<TimelineItem> {

    private final LayoutInflater inflater;
    private final int rowLayout;

    public TimelineAdapter(Context context, int rowLayout, List<TimelineItem> data) {
        super(context, rowLayout, data);
        this.inflater = LayoutInflater.from(context);
        this.rowLayout = rowLayout;
    }

    static class ViewHolder {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtDescription;
        TextView txtTimestamp;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TimelineItem item = getItem(position);
        ViewHolder h;

        if (convertView == null) {
            convertView = inflater.inflate(rowLayout, parent, false);
            h = new ViewHolder();
            h.imgIcon = convertView.findViewById(R.id.imgIcon);
            h.txtTitle = convertView.findViewById(R.id.txtTitle);
            h.txtDescription = convertView.findViewById(R.id.txtDescription);
            h.txtTimestamp = convertView.findViewById(R.id.txtTimestamp);
            convertView.setTag(h);
        } else {
            h = (ViewHolder) convertView.getTag();
        }

        if (item != null) {
            h.txtTitle.setText(item.title);
            h.txtDescription.setText(item.description);
            h.txtTimestamp.setText(item.timestamp);

            // Resolve drawable by name; fallback to ic_launcher_foreground
            int resId = getContext().getResources().getIdentifier(
                    item.icon, "drawable", getContext().getPackageName());
            if (resId == 0) resId = getContext().getResources().getIdentifier(
                    "ic_launcher_foreground", "drawable", getContext().getPackageName());
            h.imgIcon.setImageResource(resId);
        }

        return convertView;
    }
}
