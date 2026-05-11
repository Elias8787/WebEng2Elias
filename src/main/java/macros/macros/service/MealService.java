package macros.macros.service;

import macros.macros.model.Meal;
import macros.macros.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;

    public List<Meal> getAllMeals() {
        return mealRepository.findAll();
    }

    public Meal getMealById(Long id) {
        return mealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meal not found with id: " + id));
    }

    public List<Meal> getMealsByUserId(Long userId) {
        return mealRepository.findByUserId(userId);
    }

    public List<Meal> getMealsByUserIdAndDate(Long userId, LocalDate date) {
        return mealRepository.findByUserIdAndDate(userId, date);
    }

    public Meal createMeal(Meal meal) {
        return mealRepository.save(meal);
    }

    public Meal updateMeal(Long id, Meal updatedMeal) {
        Meal meal = getMealById(id);
        meal.setName(updatedMeal.getName());
        meal.setCalories(updatedMeal.getCalories());
        meal.setProtein(updatedMeal.getProtein());
        meal.setFat(updatedMeal.getFat());
        meal.setCarbs(updatedMeal.getCarbs());
        meal.setDate(updatedMeal.getDate());
        return mealRepository.save(meal);
    }

    public void deleteMeal(Long id) {
        mealRepository.deleteById(id);
    }
}
