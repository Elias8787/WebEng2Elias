package macros.macros.service;

import macros.macros.dto.WeatherDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WebClient.Builder webClientBuilder;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5";

    public WeatherDTO getWeather(String city) {
        Map response = webClientBuilder.build()
                .get()
                .uri(BASE_URL + "/weather?q={city}&appid={apiKey}&units=metric&lang=de", city, apiKey)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        WeatherDTO dto = new WeatherDTO();
        dto.setCity(city);
        dto.setTemperature(((Number) ((Map) response.get("main")).get("temp")).doubleValue());

        Map weather = (Map) ((java.util.List) response.get("weather")).get(0);
        dto.setCondition((String) weather.get("main"));
        dto.setDescription((String) weather.get("description"));

        return dto;
    }
}
