package com.example.spring_demo_crud.entity;

import jakarta.persistence.*;

@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="email", unique = true)
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="role")
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User(String firstName, String email, String password) {
        this.firstName = firstName;
        this.email = email;
        this.password = password;
    }

    public User(){};

    public int getId() {
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
