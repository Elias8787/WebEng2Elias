package macros.macros.repository;

import macros.macros.model.ActivitySuggestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivitySuggestionRepository extends JpaRepository<ActivitySuggestion, Long> {

    List<ActivitySuggestion> findByUserId(Long userId);
}
