package com.example.gestiontareas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

class TasksFragment extends Fragment {

    private ListView listViewTasks;
    private TasksAdapter tasksAdapter; // Cambiar a TasksAdapter
    private ArrayList<Task> taskList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        // Inicializar el ListView
        listViewTasks = view.findViewById(R.id.listViewTasks);

        // Crear una lista de tareas de ejemplo
        taskList = new ArrayList<>();
        taskList.add(new Task("Tarea 1", "Descripción de la tarea 1", false));
        taskList.add(new Task("Tarea 2", "Descripción de la tarea 2", true));

        // Inicializar el adaptador y asignarlo al ListView
        tasksAdapter = new TasksAdapter(getActivity(), taskList, task -> {
            // Aquí puedes manejar la lógica cuando una tarea es actualizada
        });
        listViewTasks.setAdapter(tasksAdapter);

        return view;
    }
}
