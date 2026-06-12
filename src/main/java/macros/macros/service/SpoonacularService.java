package macros.macros.service;

import macros.macros.dto.RecipeDTO;
import macros.macros.model.Recipe;
import macros.macros.repository.RecipeRepository;
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
    private final RecipeRepository recipeRepository;

    @Value("${spoonacular.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://api.spoonacular.com";

    public List<RecipeDTO> searchRecipes(String query) {
        // Erst in DB schauen
        List<Recipe> cached = recipeRepository.findBySearchQuery(query.toLowerCase());
        if (!cached.isEmpty()) {
            return cached.stream().map(this::mapFromEntity).toList();
        }

        // Wenn nicht -> API anfragen
        Map response = webClientBuilder.build()
                .get()
                .uri(BASE_URL + "/recipes/complexSearch?query={query}&addRecipeNutrition=true&number=5&apiKey={apiKey}", query, apiKey)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<Map> results = (List<Map>) response.get("results");
        List<RecipeDTO> recipes = results.stream().map(this::mapFromApi).toList();

        // In DB speichern
        recipes.forEach(dto -> {
            Recipe recipe = new Recipe();
            recipe.setName(dto.getName());
            recipe.setCalories(dto.getCalories());
            recipe.setProtein(dto.getProtein());
            recipe.setFat(dto.getFat());
            recipe.setCarbs(dto.getCarbs());
            recipe.setIngredients(String.join(",", dto.getIngredients()));
            recipe.setImage(dto.getImage());
            recipe.setSearchQuery(query.toLowerCase());
            recipeRepository.save(recipe);
        });

        return recipes;
    }

    private RecipeDTO mapFromEntity(Recipe recipe) {
        RecipeDTO dto = new RecipeDTO();
        dto.setName(recipe.getName());
        dto.setCalories(recipe.getCalories());
        dto.setProtein(recipe.getProtein());
        dto.setFat(recipe.getFat());
        dto.setCarbs(recipe.getCarbs());
        dto.setImage(recipe.getImage());
        dto.setIngredients(List.of(recipe.getIngredients().split(",")));
        return dto;
    }

    private RecipeDTO mapFromApi(Map recipe) {
        RecipeDTO dto = new RecipeDTO();
        dto.setName((String) recipe.get("title"));
        dto.setImage((String) recipe.get("image"));

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
