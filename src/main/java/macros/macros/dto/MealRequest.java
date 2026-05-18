package macros.macros.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record MealRequest(
    @NotBlank(message = "Name is required")
    String name,

    @Min(value = 0, message = "Calories must be positive")
    int calories,

    @Min(value = 0, message = "Protein must be positive")
    double protein,

    @Min(value = 0, message = "Fat must be positive")
    double fat,

    @Min(value = 0, message = "Carbs must be positive")
    double carbs,

    @NotNull(message = "Date is required")
    LocalDate date,

    Long userId
) {}