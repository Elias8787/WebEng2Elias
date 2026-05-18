package macros.macros.service;

import macros.macros.dto.MealRequest;
import macros.macros.dto.MealResponse;
import macros.macros.mapper.MealMapper;
import macros.macros.model.Meal;
import macros.macros.model.User;
import macros.macros.repository.MealRepository;
import macros.macros.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;
    private final UserRepository userRepository;

    public List<MealResponse> getAllMeals() {
        return mealRepository.findAll().stream().map(MealMapper::toResponse).toList();
    }

    public Optional<MealResponse> findById(Long id) {
        return mealRepository.findById(id).map(MealMapper::toResponse);
    }

    public List<MealResponse> getMealsByUserId(Long userId) {
        return mealRepository.findByUserId(userId).stream().map(MealMapper::toResponse).toList();
    }

    public List<MealResponse> getMealsByUserIdAndDate(Long userId, LocalDate date) {
        return mealRepository.findByUserIdAndDate(userId, date).stream().map(MealMapper::toResponse).toList();
    }

    public MealResponse createMeal(MealRequest mealRequest) {
        User user = null;
        if (mealRequest.userId() != null) {
            user = userRepository.findById(mealRequest.userId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + mealRequest.userId()));
        }
        Meal meal = MealMapper.toEntity(mealRequest, user);
        return MealMapper.toResponse(mealRepository.save(meal));
    }

    public Optional<MealResponse> updateMeal(Long id, MealRequest mealRequest) {
        return mealRepository.findById(id)
                .map(meal -> {
                    meal.setName(mealRequest.name());
                    meal.setCalories(mealRequest.calories());
                    meal.setProtein(mealRequest.protein());
                    meal.setFat(mealRequest.fat());
                    meal.setCarbs(mealRequest.carbs());
                    meal.setDate(mealRequest.date());
                    if (mealRequest.userId() != null) {
                        User user = userRepository.findById(mealRequest.userId())
                                .orElseThrow(() -> new RuntimeException("User not found with id: " + mealRequest.userId()));
                        meal.setUser(user);
                    }
                    return MealMapper.toResponse(mealRepository.save(meal));
                });
    }

    public boolean deleteMeal(Long id) {
        if (mealRepository.existsById(id)) {
            mealRepository.deleteById(id);
            return true;
        }
        return false;
    }


}
