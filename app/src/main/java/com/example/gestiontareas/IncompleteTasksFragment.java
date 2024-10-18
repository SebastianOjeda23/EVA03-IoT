package com.example.gestiontareas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class IncompleteTasksFragment extends Fragment {

    private ArrayList<Task> incompleteTasks; // Lista de tareas incompletas
    private TasksAdapter taskAdapter;
    private ListView listViewIncompleteTasks;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_incomplete_tasks, container, false); // Crear diseño para tareas incompletas

        listViewIncompleteTasks = view.findViewById(R.id.listViewIncompleteTasks);
        Button buttonAddTask = view.findViewById(R.id.buttonAddTask);

        incompleteTasks = new ArrayList<>();
        taskAdapter = new TasksAdapter(getActivity(), incompleteTasks, this::updateTask);
        listViewIncompleteTasks.setAdapter(taskAdapter);

        db = FirebaseFirestore.getInstance();

        // Cargar tareas incompletas desde Firebase
        loadIncompleteTasksFromFirebase();

        // Configurar el botón para agregar nuevas tareas
        buttonAddTask.setOnClickListener(v -> showAddTaskDialog());

        return view;
    }

    private void loadIncompleteTasksFromFirebase() {
        db.collection("tasks")
                .whereEqualTo("isCompleted", false)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        incompleteTasks.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String taskId = document.getId(); // Obtener el ID del documento
                            String taskName = document.getString("taskName");
                            String taskDescription = document.getString("taskDescription");
                            boolean isCompleted = document.getBoolean("isCompleted");

                            Task incompleteTask = new Task(taskName, taskDescription, isCompleted);
                            incompleteTask.setId(taskId); // Asignar el ID al objeto Task
                            incompleteTasks.add(incompleteTask);
                        }
                        taskAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Agregar nueva tarea");

        // Configurar el layout del diálogo
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
        builder.setView(customLayout);

        final EditText editTextTaskName = customLayout.findViewById(R.id.editTextTaskName);
        final EditText editTextTaskDescription = customLayout.findViewById(R.id.editTextTaskDescription);

        builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String taskName = editTextTaskName.getText().toString().trim();
                String taskDescription = editTextTaskDescription.getText().toString().trim();
                addTaskToFirebase(taskName, taskDescription);
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void addTaskToFirebase(String taskName, String taskDescription) {
        Task newTask = new Task(taskName, taskDescription, false);
        db.collection("tasks")
                .add(newTask)
                .addOnSuccessListener(documentReference -> {
                    // Tarea agregada con éxito
                    loadIncompleteTasksFromFirebase(); // Recargar la lista de tareas
                })
                .addOnFailureListener(e -> {
                    // Error al agregar la tarea
                });
    }

    private void updateTask(Task task) {
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
        }
    }
}
