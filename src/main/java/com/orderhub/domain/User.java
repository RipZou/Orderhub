package com.orderhub.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String email;
    private String name;
    private String passwordHash;

    protected User(){}

    public User(String id, String email, String name, String passwordHash, Role role) {
        if(id == null || id.isBlank()) throw new IllegalArgumentException("user Id cannot be null or blank");
        this.id = id;

        if(email == null || email.isBlank()) throw new IllegalArgumentException("user email cannot be null or blank");
        this.email = email;

        if(name == null || name.isBlank()) throw new IllegalArgumentException("user name cannot be null or blank");
        this.name = name;

        if(passwordHash == null || passwordHash.isBlank()) throw new IllegalArgumentException("user password cannot be null or blank");
        this.passwordHash = passwordHash;

        if(role == null) throw new IllegalArgumentException("user role cannot be null");
        this.role = role;
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

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public Role getRole() {
        return this.role;
    }


    public void rename(String newName) {
        if(newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }

        this.name = newName;
    }



}
