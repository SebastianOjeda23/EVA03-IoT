package com.example.gestiontareas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

class TasksAdapter extends ArrayAdapter<Task> {
    private Context context;
    private ArrayList<Task> tasks;
    private OnTaskUpdateListener listener;

    public interface OnTaskUpdateListener {
        void onTaskUpdated(Task task);
    }

    public TasksAdapter(Context context, ArrayList<Task> tasks, OnTaskUpdateListener listener) {
        super(context, 0, tasks);
        this.context = context;
        this.tasks = tasks;
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_task, parent, false);
        }

        TextView textViewTaskName = convertView.findViewById(R.id.textViewTaskName);
        TextView textViewTaskDescription = convertView.findViewById(R.id.textViewTaskDescription);
        CheckBox checkBoxCompleted = convertView.findViewById(R.id.checkBoxCompleted);

        textViewTaskName.setText(task.getName());
        textViewTaskDescription.setText(task.getDescription());
        checkBoxCompleted.setChecked(task.isCompleted());

        checkBoxCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            listener.onTaskUpdated(task);
        });

        return convertView;
    }
}
