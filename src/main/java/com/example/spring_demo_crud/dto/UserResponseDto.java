package com.example.spring_demo_crud.dto;

import com.example.spring_demo_crud.entity.Travel;

import java.util.List;

public record UserResponseDto(String firstName, String email, List<TravelDto> travels) {
}
