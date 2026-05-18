package macros.macros.service;

import macros.macros.dto.MealDTO;
import macros.macros.model.Meal;
import macros.macros.model.User;
import macros.macros.repository.MealRepository;
import macros.macros.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;
    private final UserRepository userRepository;

    public List<MealDTO> getAllMeals() {
        return mealRepository.findAll().stream().map(this::toDTO).toList();
    }

    public Optional<MealDTO> findById(Long id) {
        return mealRepository.findById(id).map(this::toDTO);
    }

    public List<MealDTO> getMealsByUserId(Long userId) {
        return mealRepository.findByUserId(userId).stream().map(this::toDTO).toList();
    }

    public List<MealDTO> getMealsByUserIdAndDate(Long userId, LocalDate date) {
        return mealRepository.findByUserIdAndDate(userId, date).stream().map(this::toDTO).toList();
    }

    public MealDTO createMeal(MealDTO mealDTO) {
        Meal meal = toEntity(mealDTO);
        return toDTO(mealRepository.save(meal));
    }

    public Optional<MealDTO> updateMeal(Long id, MealDTO mealDTO) {
        return mealRepository.findById(id)
                .map(meal -> {
                    meal.setName(mealDTO.getName());
                    meal.setCalories(mealDTO.getCalories());
                    meal.setProtein(mealDTO.getProtein());
                    meal.setFat(mealDTO.getFat());
                    meal.setCarbs(mealDTO.getCarbs());
                    meal.setDate(mealDTO.getDate());
                    return toDTO(mealRepository.save(meal));
                });
    }

    public boolean deleteMeal(Long id) {
        if (mealRepository.existsById(id)) {
            mealRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private MealDTO toDTO(Meal meal) {
        MealDTO dto = new MealDTO();
        dto.setId(meal.getId());
        dto.setName(meal.getName());
        dto.setCalories(meal.getCalories());
        dto.setProtein(meal.getProtein());
        dto.setFat(meal.getFat());
        dto.setCarbs(meal.getCarbs());
        dto.setDate(meal.getDate());
        dto.setUserId(meal.getUser() != null ? meal.getUser().getId() : null);
        return dto;
    }

    private Meal toEntity(MealDTO dto) {
        Meal meal = new Meal();
        meal.setName(dto.getName());
        meal.setCalories(dto.getCalories());
        meal.setProtein(dto.getProtein());
        meal.setFat(dto.getFat());
        meal.setCarbs(dto.getCarbs());
        meal.setDate(dto.getDate());
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));
            meal.setUser(user);
        }
        return meal;
    }
}
