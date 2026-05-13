package macros.macros.controller;

import macros.macros.dto.RecipeDTO;
import macros.macros.service.SpoonacularService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class SpoonacularController {

    private final SpoonacularService spoonacularService;

    @GetMapping("/search")
    public List<RecipeDTO> searchRecipes(@RequestParam String query) {
        return spoonacularService.searchRecipes(query);
    }
}
