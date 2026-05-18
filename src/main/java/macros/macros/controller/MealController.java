package macros.macros.controller;

import macros.macros.dto.MealDTO;
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
    public List<MealDTO> getAllMeals() {
        return mealService.getAllMeals();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealDTO> getMealById(@PathVariable Long id) {
        return mealService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<MealDTO> getMealsByUserId(@PathVariable Long userId) {
        return mealService.getMealsByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<MealDTO> createMeal(@Valid @RequestBody MealDTO mealDTO) {
        MealDTO created = mealService.createMeal(mealDTO);
        return ResponseEntity
            .created(URI.create("/api/meals/" + created.getId()))
            .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MealDTO> updateMeal(@PathVariable Long id, @Valid @RequestBody MealDTO mealDTO) {
        return mealService.updateMeal(id, mealDTO)
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
