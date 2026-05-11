package macros.macros.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivitySuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String activityType; // Streng training , jogging ...
    private int estimatedCaloriesBurned;
    private String weatherCondition;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
