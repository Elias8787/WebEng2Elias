package macros.macros.service;

import macros.macros.dto.RecipeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpoonacularService {

    private final WebClient.Builder webClientBuilder;

    @Value("${spoonacular.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://api.spoonacular.com";

    // Sucht Rezepte über die Spoonacular API
    public List<RecipeDTO> searchRecipes(String query) {
        Map response = webClientBuilder.build()
                .get()
                .uri(BASE_URL + "/recipes/complexSearch?query={query}&addRecipeNutrition=true&number=5&apiKey={apiKey}", query, apiKey)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<Map> results = (List<Map>) response.get("results");
        return results.stream().map(this::mapFromApi).toList();
    }

    // Wandelt API-Antwort in RecipeDTO um
    private RecipeDTO mapFromApi(Map recipe) {
        RecipeDTO dto = new RecipeDTO();
        dto.setName((String) recipe.get("title"));

        Map nutrition = (Map) recipe.get("nutrition");
        List<Map> nutrients = (List<Map>) nutrition.get("nutrients");

        for (Map nutrient : nutrients) {
            String name = (String) nutrient.get("name");
            double amount = ((Number) nutrient.get("amount")).doubleValue();

            switch (name) {
                case "Calories" -> dto.setCalories((int) amount);
                case "Protein" -> dto.setProtein(amount);
                case "Fat" -> dto.setFat(amount);
                case "Carbohydrates" -> dto.setCarbs(amount);
            }
        }

        List<Map> ingredientsList = (List<Map>) nutrition.get("ingredients");
        dto.setIngredients(ingredientsList.stream()
                .map(i -> (String) i.get("name"))
                .toList());

        return dto;
    }
}
