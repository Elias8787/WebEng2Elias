package macros.macros.service;

import macros.macros.dto.MealDTO;
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
    void createMeal_savesAndReturnsMealDTO() {
        Meal meal = new Meal();
        meal.setName("Pasta");
        meal.setCalories(600);

        when(mealRepository.save(any(Meal.class))).thenReturn(meal);

        MealDTO dto = new MealDTO();
        dto.setName("Pasta");
        dto.setCalories(600);
        dto.setDate(LocalDate.now());

        MealDTO result = mealService.createMeal(dto);

        assertEquals("Pasta", result.getName());
        assertEquals(600, result.getCalories());
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

        List<MealDTO> result = mealService.getMealsByUserIdAndDate(1L, today);

        assertEquals(2, result.size());
    }
}
