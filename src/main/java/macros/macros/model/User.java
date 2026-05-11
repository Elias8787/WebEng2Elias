package macros.macros.model;

import jakarta.persistence.*;
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

    private String username;
    private double weight;
    private double height;
    private int age;
    private boolean male;
    private double palValue;
    private String goal; // lose, maintain, gain
    private String city; // for weather api

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Meal> meals;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ActivitySuggestion> activitySuggestions;
}
