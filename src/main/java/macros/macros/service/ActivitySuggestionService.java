package macros.macros.service;

import macros.macros.dto.ActivitySuggestionDTO;
import macros.macros.dto.WeatherDTO;
import macros.macros.model.ActivitySuggestion;
import macros.macros.model.User;
import macros.macros.repository.ActivitySuggestionRepository;
import macros.macros.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivitySuggestionService {

    private final ActivitySuggestionRepository repository;
    private final UserRepository userRepository;
    private final WeatherService weatherService;

    public List<ActivitySuggestionDTO> getSuggestionsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        WeatherDTO weather = weatherService.getWeather(user.getCity());
        List<ActivitySuggestion> suggestions = generateSuggestions(weather, user);

        suggestions.forEach(repository::save);

        return suggestions.stream().map(this::toDTO).toList();
    }

    public List<ActivitySuggestionDTO> getHistoryForUser(Long userId) {
        return repository.findByUserId(userId).stream().map(this::toDTO).toList();
    }

    private List<ActivitySuggestion> generateSuggestions(WeatherDTO weather, User user) {
        String condition = weather.getCondition();
        double temp = weather.getTemperature();
        double weight = user.getWeight();

        return switch (condition) {
            case "Clear" -> List.of(
                    createSuggestion("Joggen", calcCalories(weight, 7.0), condition, user),
                    createSuggestion("Radfahren", calcCalories(weight, 8.0), condition, user)
            );
            case "Clouds" -> List.of(
                    createSuggestion("Joggen", calcCalories(weight, 7.0), condition, user),
                    createSuggestion("Wandern", calcCalories(weight, 4.5), condition, user)
            );
            case "Rain", "Drizzle" -> List.of(
                    createSuggestion("Fitnessstudio", calcCalories(weight, 6.0), condition, user),
                    createSuggestion("Yoga", calcCalories(weight, 2.5), condition, user)
            );
            case "Snow" -> List.of(
                    createSuggestion("Skifahren", calcCalories(weight, 7.0), condition, user),
                    createSuggestion("Fitnessstudio", calcCalories(weight, 6.0), condition, user)
            );
            default -> {
                if (temp > 25) {
                    yield List.of(
                            createSuggestion("Schwimmen", calcCalories(weight, 6.0), condition, user),
                            createSuggestion("Radfahren", calcCalories(weight, 8.0), condition, user)
                    );
                }
                yield List.of(
                        createSuggestion("Spaziergang", calcCalories(weight, 3.0), condition, user),
                        createSuggestion("Fitnessstudio", calcCalories(weight, 6.0), condition, user)
                );
            }
        };
    }

    // Kalorienberechnung: MET-Wert * Gewicht  * Dauer  1 Stunde
    private int calcCalories(double weight, double metValue) {
        return (int) (metValue * weight);
    }

    private ActivitySuggestion createSuggestion(String type, int calories, String condition, User user) {
        ActivitySuggestion s = new ActivitySuggestion();
        s.setActivityType(type);
        s.setEstimatedCaloriesBurned(calories);
        s.setWeatherCondition(condition);
        s.setUser(user);
        return s;
    }

    private ActivitySuggestionDTO toDTO(ActivitySuggestion entity) {
        ActivitySuggestionDTO dto = new ActivitySuggestionDTO();
        dto.setId(entity.getId());
        dto.setActivityType(entity.getActivityType());
        dto.setEstimatedCaloriesBurned(entity.getEstimatedCaloriesBurned());
        dto.setWeatherCondition(entity.getWeatherCondition());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        return dto;
    }
}
