package macros.macros.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MealDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Min(value = 0, message = "Calories must be positive")
    private int calories;

    @Min(value = 0, message = "Protein must be positive")
    private double protein;

    @Min(value = 0, message = "Fat must be positive")
    private double fat;

    @Min(value = 0, message = "Carbs must be positive")
    private double carbs;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private Long userId;
}
