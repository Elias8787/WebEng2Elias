package macros.macros.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int calories;
    private double protein;
    private double fat;
    private double carbs;

    @Column(length = 2000)
    private String ingredients; // komma getrennt gespeichert

    private String searchQuery; // welcher Suchbegriff zu diesem Rezept führte
}
