package com.example.gestiontareas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

class GroupAdapter extends ArrayAdapter<Group> {
    private Context context;
    private List<Group> groups;

    public GroupAdapter(Context context, List<Group> groups) {
        super(context, R.layout.item_group, groups);
        this.context = context;
        this.groups = groups;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_group, parent, false);
            holder = new ViewHolder();
            holder.groupNameTextView = convertView.findViewById(R.id.groupNameTextView);
            holder.groupDescriptionTextView = convertView.findViewById(R.id.groupDescriptionTextView);
            holder.groupCodeTextView = convertView.findViewById(R.id.groupCodeTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Group group = groups.get(position);
        holder.groupNameTextView.setText(group.getName());
        holder.groupDescriptionTextView.setText(group.getDescription());
        holder.groupCodeTextView.setText(group.getCode());

        return convertView;
    }

    private static class ViewHolder {
        TextView groupNameTextView;
        TextView groupDescriptionTextView;
        TextView groupCodeTextView;
    }
}
