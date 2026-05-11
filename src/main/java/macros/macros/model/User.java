package macros.macros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private String goal; // LOSE, MAINTAIN, GAIN

    @NotBlank(message = "City is required")
    private String city;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Meal> meals;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ActivitySuggestion> activitySuggestions;
}
