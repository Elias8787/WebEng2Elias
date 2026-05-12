package macros.macros.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    @NotBlank(message = "Username is required")
    private String username;

    @Min(value = 1, message = "Weight must be positive")
    private double weight;

    @Min(value = 1, message = "Height must be positive")
    private double height;

    @Min(value = 1, message = "Age must be positive")
    private int age;

    private boolean male;

    @Min(value = 1, message = "PAL value must be at least 1")
    private double palValue;

    @NotBlank(message = "Goal is required")
    private String goal;

    @NotBlank(message = "City is required")
    private String city;

    private double dailyCalories;
}
