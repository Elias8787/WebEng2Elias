package macros.macros.service;

import macros.macros.dto.MealRequest;
import macros.macros.dto.MealResponse;
import macros.macros.model.Meal;
import macros.macros.repository.MealRepository;
import macros.macros.repository.UserRepository;
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

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MealService mealService;

    @Test
    void createMeal_savesAndReturnsResponse() {
        Meal saved = new Meal();
        saved.setId(1L);
        saved.setName("Pasta");
        saved.setCalories(600);
        saved.setProtein(25.0);
        saved.setFat(15.0);
        saved.setCarbs(80.0);
        saved.setDate(LocalDate.now());

        when(mealRepository.save(any(Meal.class))).thenReturn(saved);

        MealRequest request = new MealRequest("Pasta", 600, 25.0, 15.0, 80.0, LocalDate.now(), null);
        MealResponse result = mealService.createMeal(request);

        assertEquals("Pasta", result.name());
        assertEquals(600, result.calories());
    }

    @Test
    void findById_notFound_returnsEmpty() {
        when(mealRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<MealResponse> result = mealService.findById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void getMealsByUserIdAndDate_returnsMeals() {
        LocalDate today = LocalDate.now();
        Meal meal1 = new Meal();
        meal1.setName("Frühstück");
        meal1.setDate(today);
        Meal meal2 = new Meal();
        meal2.setName("Mittagessen");
        meal2.setDate(today);

        when(mealRepository.findByUserIdAndDate(1L, today)).thenReturn(List.of(meal1, meal2));

        List<MealResponse> result = mealService.getMealsByUserIdAndDate(1L, today);

        assertEquals(2, result.size());
    }

    @Test
    void deleteMeal_exists_returnsTrue() {
        when(mealRepository.existsById(1L)).thenReturn(true);

        assertTrue(mealService.deleteMeal(1L));
        verify(mealRepository).deleteById(1L);
    }

    @Test
    void deleteMeal_notExists_returnsFalse() {
        when(mealRepository.existsById(99L)).thenReturn(false);

        assertFalse(mealService.deleteMeal(99L));
        verify(mealRepository, never()).deleteById(any());
    }
}
