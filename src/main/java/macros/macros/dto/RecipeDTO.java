package macros.macros.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecipeDTO {

    private Long id;
    private String name;
    private String image;
    private int calories;
    private double protein;
    private double fat;
    private double carbs;
    private List<String> ingredients;
}
