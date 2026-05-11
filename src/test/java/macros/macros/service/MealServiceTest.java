package macros.macros.service;

import macros.macros.model.Meal;
import macros.macros.repository.MealRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @InjectMocks
    private MealService mealService;

    @Test
    void createMeal_savesAndReturnsMeal() {
        Meal meal = new Meal();
        meal.setName("Pasta");
        meal.setCalories(600);

        when(mealRepository.save(meal)).thenReturn(meal);

        Meal result = mealService.createMeal(meal);

        assertEquals("Pasta", result.getName());
        assertEquals(600, result.getCalories());
        verify(mealRepository).save(meal);
    }

    @Test
    void getMealById_notFound_throwsException() {
        when(mealRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> mealService.getMealById(99L));
    }

    @Test
    void getMealsByUserIdAndDate_returnsMeals() {
        LocalDate today = LocalDate.now();
        Meal meal1 = new Meal();
        meal1.setName("Frühstück");
        Meal meal2 = new Meal();
        meal2.setName("Mittagessen");

        when(mealRepository.findByUserIdAndDate(1L, today)).thenReturn(List.of(meal1, meal2));

        List<Meal> result = mealService.getMealsByUserIdAndDate(1L, today);

        assertEquals(2, result.size());
    }
}
