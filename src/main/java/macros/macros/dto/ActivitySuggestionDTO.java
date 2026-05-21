package macros.macros.dto;

import lombok.Data;

@Data
public class ActivitySuggestionDTO {
    private Long id;
    private String activityType;
    private int estimatedCaloriesBurned;
    private String weatherCondition;
    private Long userId;
}
