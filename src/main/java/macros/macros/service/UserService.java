package macros.macros.service;

import macros.macros.dto.UserDTO;
import macros.macros.model.User;
import macros.macros.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDTO).toList();
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return toDTO(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = toEntity(userDTO);
        User saved = userRepository.save(user);
        return toDTO(saved);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setUsername(userDTO.getUsername());
        user.setWeight(userDTO.getWeight());
        user.setHeight(userDTO.getHeight());
        user.setAge(userDTO.getAge());
        user.setMale(userDTO.isMale());
        user.setPalValue(userDTO.getPalValue());
        user.setGoal(userDTO.getGoal());
        user.setCity(userDTO.getCity());
        return toDTO(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public double calculateDailyCalories(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return calculateCalories(user);
    }

    private double calculateCalories(User user) {
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

    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setWeight(user.getWeight());
        dto.setHeight(user.getHeight());
        dto.setAge(user.getAge());
        dto.setMale(user.isMale());
        dto.setPalValue(user.getPalValue());
        dto.setGoal(user.getGoal());
        dto.setCity(user.getCity());
        dto.setDailyCalories(calculateCalories(user));
        return dto;
    }

    private User toEntity(UserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setWeight(dto.getWeight());
        user.setHeight(dto.getHeight());
        user.setAge(dto.getAge());
        user.setMale(dto.isMale());
        user.setPalValue(dto.getPalValue());
        user.setGoal(dto.getGoal());
        user.setCity(dto.getCity());
        return user;
    }
}
