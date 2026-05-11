package macros.macros.service;

import macros.macros.model.User;
import macros.macros.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void calculateDailyCalories_maleLosingWeight() {
        User user = new User();
        user.setId(1L);
        user.setWeight(80);
        user.setHeight(180);
        user.setAge(30);
        user.setMale(true);
        user.setPalValue(1.5);
        user.setGoal("LOSE");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        double result = userService.calculateDailyCalories(1L);

        // BMR = (10*80) + (6.25*180) - (5*30) + 5 = 1780
        // Total = 1780 * 1.5 = 2670
        // Lose = 2670 - 500 = 2170
        assertEquals(2170, result);
    }

    @Test
    void calculateDailyCalories_femaleMaintaining() {
        User user = new User();
        user.setId(2L);
        user.setWeight(60);
        user.setHeight(165);
        user.setAge(25);
        user.setMale(false);
        user.setPalValue(1.4);
        user.setGoal("MAINTAIN");

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        double result = userService.calculateDailyCalories(2L);

        // BMR = (10*60) + (6.25*165) - (5*25) - 161 = 1345.25
        // Total = 1345.25 * 1.4 = 1883.35
        assertEquals(1883.35, result);
    }

    @Test
    void calculateDailyCalories_maleGaining() {
        User user = new User();
        user.setId(3L);
        user.setWeight(70);
        user.setHeight(175);
        user.setAge(22);
        user.setMale(true);
        user.setPalValue(1.7);
        user.setGoal("GAIN");

        when(userRepository.findById(3L)).thenReturn(Optional.of(user));

        double result = userService.calculateDailyCalories(3L);

        // BMR = (10*70) + (6.25*175) - (5*22) + 5 = 1688.75
        // Total = 1688.75 * 1.7 = 2870.875
        // Gain = 2870.875 + 500 = 3370.875
        assertEquals(3370.875, result);
    }

    @Test
    void getUserById_notFound_throwsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserById(99L));
    }
}
