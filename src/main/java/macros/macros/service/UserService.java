package macros.macros.service;

import macros.macros.model.User;
import macros.macros.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        User user = getUserById(id);
        user.setUsername(updatedUser.getUsername());
        user.setWeight(updatedUser.getWeight());
        user.setHeight(updatedUser.getHeight());
        user.setAge(updatedUser.getAge());
        user.setMale(updatedUser.isMale());
        user.setPalValue(updatedUser.getPalValue());
        user.setGoal(updatedUser.getGoal());
        user.setCity(updatedUser.getCity());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public double calculateDailyCalories(Long userId) {
        User user = getUserById(userId);
        double bmr;

        if (user.isMale()) {
            bmr = (10 * user.getWeight()) + (6.25 * user.getHeight()) - (5 * user.getAge()) + 5;
        } else {
            bmr = (10 * user.getWeight()) + (6.25 * user.getHeight()) - (5 * user.getAge()) - 161;
        }

        double totalCalories = bmr * user.getPalValue();

        return switch (user.getGoal()) {
            case "LOSE" -> totalCalories - 500;
            case "GAIN" -> totalCalories + 500;
            default -> totalCalories;
        };
    }
}
