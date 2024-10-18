package com.example.gestiontareas;

import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class JoinGroupDialogFragment extends DialogFragment {
    private final OnJoinGroupListener listener;

    // Interfaz para manejar el evento de unirse al grupo
    public interface OnJoinGroupListener {
        void onJoinGroup(String groupCode);
    }

    // Constructor que toma un listener
    public JoinGroupDialogFragment(OnJoinGroupListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        EditText editTextGroupCode = new EditText(getContext());
        editTextGroupCode.setInputType(InputType.TYPE_CLASS_TEXT); // Establecer tipo de entrada

        return new AlertDialog.Builder(requireContext())
                .setTitle("Unirse a Grupo")
                .setMessage("Ingresa el código del grupo")
                .setView(editTextGroupCode) // Añadir el EditText al diálogo
                .setPositiveButton("Unirse", (dialog, which) -> {
                    String groupCode = editTextGroupCode.getText().toString();
                    listener.onJoinGroup(groupCode); // Llamar al método en el listener
                })
                .setNegativeButton("Cancelar", null)
                .create();
    }
}
