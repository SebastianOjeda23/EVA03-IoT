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

public class GrupoUnidoFragment extends Fragment {
    private ListView joinedGroupListView;
    private GroupAdapter groupAdapter;
    private ArrayList<Group> joinedGroups;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grupo_unido, container, false);
        joinedGroupListView = view.findViewById(R.id.listViewJoinedGroups);
        Button joinGroupButton = view.findViewById(R.id.buttonJoinGroup);
        db = FirebaseFirestore.getInstance();
        joinedGroups = new ArrayList<>();
        groupAdapter = new GroupAdapter(getContext(), joinedGroups);
        joinedGroupListView.setAdapter(groupAdapter);

        // Aquí no cargamos grupos de Firestore, comienza vacío

        joinGroupButton.setOnClickListener(v -> showJoinGroupDialog());

        return view;
    }

    private void showJoinGroupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Unirse a Grupo");

        // Inflar el layout para el diálogo
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_join_group, null);
        builder.setView(dialogView);

        // Obtener las vistas
        TextInputEditText groupCodeInput = dialogView.findViewById(R.id.editTextGroupCode);

        builder.setPositiveButton("Unirse", (dialog, which) -> {
            String groupCode = groupCodeInput.getText().toString();
            joinGroup(groupCode);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void joinGroup(String groupCode) {
        db.collection("groups")
                .whereEqualTo("code", groupCode)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().size() > 0) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Group group = document.toObject(Group.class);
                            // Agregar el grupo a la lista de grupos unidos
                            joinedGroups.add(group);
                            groupAdapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), "Te has unido al grupo", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    Toast.makeText(getContext(), "Código de grupo incorrecto", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al unirse al grupo", Toast.LENGTH_SHORT).show());
    }
}
