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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Button volverButton = findViewById(R.id.Volver);
        Button registrarButton = findViewById(R.id.Registrar);
        EditText emailEditText = findViewById(R.id.editTextTextEmailAddress);
        EditText passwordEditText = findViewById(R.id.editTextTextPassword);

        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        registrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    // Verificar que la contraseña tenga más de 6 caracteres
                    if (password.length() >= 6) {
                        // Registrar al usuario en Firebase
                        registerUser(email, password);
                    } else {
                        Toast.makeText(MainActivity2.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity2.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // El usuario fue creado con éxito
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity2.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                            // Guardar los datos del usuario en Firestore
                            if (user != null) {
                                String userId = user.getUid();
                                saveUserToFirestore(userId, email, password);
                            }

                            // Limpiar los campos de texto
                            ((EditText) findViewById(R.id.editTextTextEmailAddress)).setText("");
                            ((EditText) findViewById(R.id.editTextTextPassword)).setText("");

                            // Redirigir al usuario a la pantalla de inicio de sesión
                            Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Si ocurre un error, muestra un mensaje al usuario
                            handleRegistrationError(task.getException());
                        }
                    }
                });
    }

    // Guardar los datos del usuario en Firebase Firestore
    private void saveUserToFirestore(String userId, String email, String password) {
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("password", password); // Almacenar contraseñas no es recomendado
        user.put("userId", userId);
        user.put("groups", new ArrayList<>()); // Inicializa como una lista vacía

        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Usuario registrado correctamente en Firestore"))
                .addOnFailureListener(e -> Log.w("Firestore", "Error al registrar usuario en Firestore", e));
    }

    // Manejo de errores durante el registro
    private void handleRegistrationError(Exception exception) {
        if (exception != null) {
            try {
                throw exception;
            } catch (FirebaseAuthWeakPasswordException e) {
                Toast.makeText(MainActivity2.this, "La contraseña es demasiado débil", Toast.LENGTH_SHORT).show();
            } catch (FirebaseAuthUserCollisionException e) {
                Toast.makeText(MainActivity2.this, "El correo electrónico ya está registrado", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("FirebaseError", "Error de registro: " + e.getMessage());
                Toast.makeText(MainActivity2.this, "Error de registro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
