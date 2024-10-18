package com.example.gestiontareas;

public class Task {
    private String id; // ID de la tarea en Firestore
    private String name;
    private String description;
    private boolean isCompleted;

    // Constructor para crear una nueva tarea
    public Task(String name, String description, boolean isCompleted) {
        this.name = name;
        this.description = description;
        this.isCompleted = isCompleted;
    }

    // Constructor para recuperar tareas de Firestore
    public Task(String id, String name, String description, boolean isCompleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isCompleted = isCompleted;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
