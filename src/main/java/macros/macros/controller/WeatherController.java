package macros.macros.controller;

import macros.macros.dto.ActivitySuggestionDTO;
import macros.macros.dto.WeatherDTO;
import macros.macros.service.ActivitySuggestionService;
import macros.macros.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    private final ActivitySuggestionService activitySuggestionService;

    @GetMapping
    public WeatherDTO getWeather(@RequestParam String city) {
        return weatherService.getWeather(city);
    }

    @GetMapping("/activities/{userId}")
    public List<ActivitySuggestionDTO> getActivitySuggestions(@PathVariable Long userId) {
        return activitySuggestionService.getSuggestionsForUser(userId);
    }

    @GetMapping("/activities/{userId}/history")
    public List<ActivitySuggestionDTO> getActivityHistory(@PathVariable Long userId) {
        return activitySuggestionService.getHistoryForUser(userId);
    }
}
