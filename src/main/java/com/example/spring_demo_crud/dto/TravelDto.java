package com.example.spring_demo_crud.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Date;

public record TravelDto(
        @NotBlank
        String destination,

        @NotNull

        LocalDate startDate,

        @NotNull

        LocalDate endDate
) { }
