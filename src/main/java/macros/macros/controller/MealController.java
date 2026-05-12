package macros.macros.controller;

import macros.macros.dto.MealDTO;
import macros.macros.service.MealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @GetMapping
    public List<MealDTO> getAllMeals() {
        return mealService.getAllMeals();
    }

    @GetMapping("/{id}")
    public MealDTO getMealById(@PathVariable Long id) {
        return mealService.getMealById(id);
    }

    @GetMapping("/user/{userId}")
    public List<MealDTO> getMealsByUserId(@PathVariable Long userId) {
        return mealService.getMealsByUserId(userId);
    }

    @PostMapping
    public MealDTO createMeal(@Valid @RequestBody MealDTO mealDTO) {
        return mealService.createMeal(mealDTO);
    }

    @PutMapping("/{id}")
    public MealDTO updateMeal(@PathVariable Long id, @Valid @RequestBody MealDTO mealDTO) {
        return mealService.updateMeal(id, mealDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long id) {
        mealService.deleteMeal(id);
        return ResponseEntity.noContent().build();
    }
}
