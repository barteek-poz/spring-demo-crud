package com.example.spring_demo_crud.dto;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;


public class UserUpdateDto {
    @NotNull
    private String email;
    @NotNull
    private String name;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
