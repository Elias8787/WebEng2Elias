package macros.macros.dto;

import java.time.LocalDate;

public record MealResponse(
    Long id,
    String name,
    int calories,
    double protein,
    double fat,
    double carbs,
    LocalDate date,
    Long userId
) {}