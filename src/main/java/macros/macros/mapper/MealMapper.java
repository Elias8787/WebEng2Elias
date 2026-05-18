package macros.macros.mapper;

import macros.macros.dto.MealRequest;
import macros.macros.dto.MealResponse;
import macros.macros.model.Meal;
import macros.macros.model.User;

public final class MealMapper {

    private MealMapper() {}

    public static Meal toEntity(MealRequest request, User user) {
        Meal meal = new Meal();
        meal.setName(request.name());
        meal.setCalories(request.calories());
        meal.setProtein(request.protein());
        meal.setFat(request.fat());
        meal.setCarbs(request.carbs());
        meal.setDate(request.date());
        meal.setUser(user);
        return meal;
    }

    public static MealResponse toResponse(Meal meal) {
        return new MealResponse(
            meal.getId(),
            meal.getName(),
            meal.getCalories(),
            meal.getProtein(),
            meal.getFat(),
            meal.getCarbs(),
            meal.getDate(),
            meal.getUser() != null ? meal.getUser().getId() : null
        );
    }
}