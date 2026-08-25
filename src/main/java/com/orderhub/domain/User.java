package com.orderhub.domain;

public class User {
    private String id;
    private String email;
    private String name;

    public User(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }


    public String getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public void rename(String newName) {
        if(newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }

        this.name = newName;
    }



}
