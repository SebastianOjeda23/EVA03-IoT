package com.example.gestiontareas;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CompletedTasksFragment extends Fragment {

    private ArrayList<Task> completedTasks; // Lista de tareas completadas
    private TasksAdapter taskAdapter;
    private ListView listViewCompletedTasks;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_tasks, container, false); // Crear diseño para tareas completadas

        listViewCompletedTasks = view.findViewById(R.id.listViewCompletedTasks);
        completedTasks = new ArrayList<>();
        taskAdapter = new TasksAdapter(getActivity(), completedTasks, this::updateTask);
        listViewCompletedTasks.setAdapter(taskAdapter);

        db = FirebaseFirestore.getInstance();

        // Cargar tareas completadas desde Firebase
        loadCompletedTasksFromFirebase();

        return view;
    }

    private void loadCompletedTasksFromFirebase() {
        db.collection("tasks")
                .whereEqualTo("isCompleted", true)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        completedTasks.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String taskId = document.getId(); // Obtener el ID del documento
                            String taskName = document.getString("taskName");
                            String taskDescription = document.getString("taskDescription");
                            boolean isCompleted = document.getBoolean("isCompleted");

                            Task completedTask = new Task(taskName, taskDescription, isCompleted);
                            completedTask.setId(taskId); // Asignar el ID al objeto Task
                            completedTasks.add(completedTask);
                        }
                        taskAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void updateTask(Task task) {
        // Verificar que el ID no sea nulo antes de intentar actualizar
        if (task.getId() != null) {
            db.collection("tasks").document(task.getId())
                    .update("isCompleted", task.isCompleted())
                    .addOnSuccessListener(aVoid -> {
                        // Tarea actualizada con éxito
                    })
                    .addOnFailureListener(e -> {
                        // Error al actualizar
                    });
        } else {
            // Manejar el caso en que el ID sea nulo
            // Aquí puedes mostrar un mensaje de error o realizar otra acción
        }
    }
}
