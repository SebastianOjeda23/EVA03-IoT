package com.example.gestiontareas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Random;

public class GrupoCreadoFragment extends Fragment {
    private ListView createdGroupListView;
    private GroupAdapter groupAdapter;
    private ArrayList<Group> createdGroups;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grupo_creado, container, false);
        createdGroupListView = view.findViewById(R.id.listViewCreatedGroups);
        Button createGroupButton = view.findViewById(R.id.buttonCreateGroup);
        db = FirebaseFirestore.getInstance();
        createdGroups = new ArrayList<>();
        groupAdapter = new GroupAdapter(getContext(), createdGroups);
        createdGroupListView.setAdapter(groupAdapter);

        // Recuperar grupos creados por userId
        loadCreatedGroups("user123");

        createGroupButton.setOnClickListener(v -> showCreateGroupDialog());

        return view;
    }

    private void loadCreatedGroups(String userId) {
        db.collection("groups")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        createdGroups.clear(); // Limpiar la lista antes de añadir nuevos grupos
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Group group = document.toObject(Group.class);
                            createdGroups.add(group);
                        }
                        groupAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Error al cargar grupos", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showCreateGroupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Crear Grupo");

        // Inflar el layout para el diálogo
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_create_group, null);
        builder.setView(dialogView);

        // Obtener las vistas
        TextInputEditText groupNameInput = dialogView.findViewById(R.id.editTextGroupName);
        TextInputEditText groupDescriptionInput = dialogView.findViewById(R.id.editTextGroupDescription);

        builder.setPositiveButton("Crear", (dialog, which) -> {
            String groupName = groupNameInput.getText().toString();
            String groupDescription = groupDescriptionInput.getText().toString();
            String groupCode = generateGroupCode();

            Group newGroup = new Group(groupName, groupDescription, "user123", groupCode);
            saveGroupToFirestore(newGroup);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private String generateGroupCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000)); // Código de 6 dígitos
    }

    private void saveGroupToFirestore(Group group) {
        db.collection("groups")
                .add(group)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Grupo creado con éxito", Toast.LENGTH_SHORT).show();
                    loadCreatedGroups("user123"); // Recargar los grupos después de crear uno nuevo
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al crear grupo", Toast.LENGTH_SHORT).show());
    }
}
