package macros.macros.controller;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/meals")
public class MealController {

    @GetMapping
    public List<String> getAllMeals() {
        return new ArrayList<>();
    }

    @GetMapping("/{id}")
    public String getMealById(@PathVariable Long id) {
        return "Meal " + id;
    }

    @PostMapping
    public String createMeal(@RequestBody String meal) {
        return "Meal created";
    }

    @PutMapping("/{id}")
    public String updateMeal(@PathVariable Long id, @RequestBody String meal) {
        return "Meal " + id + " updated";
    }

    @DeleteMapping("/{id}")
    public String deleteMeal(@PathVariable Long id) {
        return "Meal " + id + " deleted";
    }
}
