package com.example.gestiontareas;

public class Task {
    private String id; // ID de la tarea en Firestore
    private String name;
    private String description;
    private boolean isCompleted;


    public Task(String name, String description, boolean isCompleted) {
        this.name = name;
        this.description = description;
        this.isCompleted = isCompleted;
    }


    public Task(String id, String name, String description, boolean isCompleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isCompleted = isCompleted;
    }


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
