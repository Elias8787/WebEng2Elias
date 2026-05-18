package macros.macros.controller;

import macros.macros.dto.MealRequest;
import macros.macros.dto.MealResponse;
import macros.macros.mapper.MealMapper;
import macros.macros.service.MealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @GetMapping
    public List<MealResponse> getAllMeals() {
        return mealService.getAllMeals();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealResponse> getMealById(@PathVariable Long id) {
        return mealService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<MealResponse> getMealsByUserId(@PathVariable Long userId) {
        return mealService.getMealsByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<MealResponse> createMeal(@Valid @RequestBody MealRequest mealRequest) {
        MealResponse created = mealService.createMeal(mealRequest);
        return ResponseEntity
            .created(URI.create("/api/meals/" + created.id()))
            .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MealResponse> updateMeal(@PathVariable Long id, @Valid @RequestBody MealRequest mealRequest) {
        return mealService.updateMeal(id, mealRequest)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long id) {
        if (mealService.deleteMeal(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
