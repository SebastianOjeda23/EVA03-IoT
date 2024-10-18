package com.example.gestiontareas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;

import java.util.List;

public class MainActivity3 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // Inicializa Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();  // Inicializa Firestore

        Button volverButton = findViewById(R.id.Volver2);
        Button inicioSesionButton = findViewById(R.id.InicioSesion);
        EditText emailEditText = findViewById(R.id.editTextTextEmailAddress2);
        EditText passwordEditText = findViewById(R.id.editTextTextPassword2);

        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity3.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        inicioSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    // Iniciar sesión con Firebase Authentication
                    signInUser(email, password);
                } else {
                    Toast.makeText(MainActivity3.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Inicio de sesión exitoso
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity3.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                            // Cargar los grupos a los que se ha unido el usuario
                            loadUserGroups(user.getUid());

                        } else {
                            // Si falla el inicio de sesión, mostrar mensaje de error adecuado
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Error desconocido";
                            Toast.makeText(MainActivity3.this, "Error de autenticación: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void loadUserGroups(String userId) {
        // Obtener los grupos a los que se ha unido el usuario desde Firestore
        db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<String> groupIds = (List<String>) documentSnapshot.get("groups");
                if (groupIds != null && !groupIds.isEmpty()) {
                    // Cargar los detalles de los grupos a los que se ha unido el usuario
                    db.collection("groups").whereIn(FieldPath.documentId(), groupIds).get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    StringBuilder groupsString = new StringBuilder();
                                    for (DocumentSnapshot groupDoc : task.getResult()) {
                                        String groupName = groupDoc.getString("name");
                                        groupsString.append(groupName).append("\n");
                                    }

                                    // Redirigir a MainActivity4 con los grupos cargados
                                    Intent intent = new Intent(MainActivity3.this, MainActivity4.class);
                                    intent.putExtra("userGroups", groupsString.toString());
                                    intent.putExtra("isUserJoinedGroups", true); // Indica que hay grupos unidos
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(MainActivity3.this, "Error al cargar los grupos", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(MainActivity3.this, "No estás unido a ningún grupo", Toast.LENGTH_SHORT).show();

                    // Redirigir a MainActivity4 sin grupos unidos
                    Intent intent = new Intent(MainActivity3.this, MainActivity4.class);
                    intent.putExtra("userGroups", ""); // Pasar vacío o "No hay grupos"
                    intent.putExtra("isUserJoinedGroups", false); // Indica que no hay grupos unidos
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(MainActivity3.this, "No se encontraron datos del usuario", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(MainActivity3.this, "Error al acceder a Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
