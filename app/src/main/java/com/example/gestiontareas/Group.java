package com.example.gestiontareas;

public class Group {
    private String name;
    private String description;
    private String userId; // userId para identificar al usuario
    private String code; // Campo para el código del grupo

    // Constructor vacío necesario para Firestore
    public Group() {
    }

    public Group(String name, String description, String userId, String code) {
        this.name = name;
        this.description = description;
        this.userId = userId;
        this.code = code; // Asignar el código
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUserId() {
        return userId;
    }

    public String getCode() {
        return code; // Método para obtener el código
    }
}
