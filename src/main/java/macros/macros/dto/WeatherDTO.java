package macros.macros.dto;

import lombok.Data;

@Data
public class WeatherDTO {
    private String city;
    private String condition; // Clear, Clouds, Rain, Snow, etc.
    private double temperature;
    private String description;
}
