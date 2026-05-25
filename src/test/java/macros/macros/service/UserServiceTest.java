package macros.macros.service;

import macros.macros.dto.UserDTO;
import macros.macros.model.User;
import macros.macros.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void calculateDailyCalories_maleLose() {
        // Mann: 80kg, 180cm, 25 Jahre, PAL 1.5, Ziel: Abnehmen
        User user = new User();
        user.setId(1L);
        user.setMale(true);
        user.setWeight(80);
        user.setHeight(180);
        user.setAge(25);
        user.setPalValue(1.5);
        user.setGoal("LOSE");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        double result = userService.calculateDailyCalories(1L);

        // BMR = (10*80) + (6.25*180) - (5*25) + 5 = 800 + 1125 - 125 + 5 = 1805
        // Total = 1805 * 1.5 = 2707.5
        // LOSE = 2707.5 - 500 = 2207.5
        assertEquals(2207.5, result);
    }

    @Test
    void calculateDailyCalories_maleMaintain() {
        // Mann: 75kg, 175cm, 28 Jahre, PAL 1.6, Ziel: Halten
        User user = new User();
        user.setId(2L);
        user.setMale(true);
        user.setWeight(75);
        user.setHeight(175);
        user.setAge(28);
        user.setPalValue(1.6);
        user.setGoal("MAINTAIN");

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        double result = userService.calculateDailyCalories(2L);

        // BMR = (10*75) + (6.25*175) - (5*28) + 5 = 750 + 1093.75 - 140 + 5 = 1708.75
        // Total = 1708.75 * 1.6 = 2734.0
        // MAINTAIN = 2734.0
        assertEquals(2734.0, result);
    }

    @Test
    void calculateDailyCalories_maleGain() {
        // Mann: 80kg, 180cm, 25 Jahre, PAL 1.5, Ziel: Zunehmen
        User user = new User();
        user.setId(3L);
        user.setMale(true);
        user.setWeight(80);
        user.setHeight(180);
        user.setAge(25);
        user.setPalValue(1.5);
        user.setGoal("GAIN");

        when(userRepository.findById(3L)).thenReturn(Optional.of(user));

        double result = userService.calculateDailyCalories(3L);

        // BMR = (10*80) + (6.25*180) - (5*25) + 5 = 1805
        // Total = 1805 * 1.5 = 2707.5
        // GAIN = 2707.5 + 500 = 3207.5
        assertEquals(3207.5, result);
    }

    @Test
    void calculateDailyCalories_femaleLose() {
        // Frau: 60kg, 165cm, 30 Jahre, PAL 1.4, Ziel: Abnehmen
        User user = new User();
        user.setId(4L);
        user.setMale(false);
        user.setWeight(60);
        user.setHeight(165);
        user.setAge(30);
        user.setPalValue(1.4);
        user.setGoal("LOSE");

        when(userRepository.findById(4L)).thenReturn(Optional.of(user));

        double result = userService.calculateDailyCalories(4L);

        // BMR = (10*60) + (6.25*165) - (5*30) - 161 = 600 + 1031.25 - 150 - 161 = 1320.25
        // Total = 1320.25 * 1.4 = 1848.35
        // LOSE = 1848.35 - 500 = 1348.35
        assertEquals(1348.35, result);
    }

    @Test
    void calculateDailyCalories_femaleMaintain() {
        // Frau: 65kg, 170cm, 27 Jahre, PAL 1.5, Ziel: Halten
        User user = new User();
        user.setId(5L);
        user.setMale(false);
        user.setWeight(65);
        user.setHeight(170);
        user.setAge(27);
        user.setPalValue(1.5);
        user.setGoal("MAINTAIN");

        when(userRepository.findById(5L)).thenReturn(Optional.of(user));

        double result = userService.calculateDailyCalories(5L);

        // BMR = (10*65) + (6.25*170) - (5*27) - 161 = 650 + 1062.5 - 135 - 161 = 1416.5
        // Total = 1416.5 * 1.5 = 2124.75
        // MAINTAIN = 2124.75
        assertEquals(2124.75, result);
    }

    @Test
    void calculateDailyCalories_femaleGain() {
        // Frau: 60kg, 165cm, 30 Jahre, PAL 1.4, Ziel: Zunehmen
        User user = new User();
        user.setId(6L);
        user.setMale(false);
        user.setWeight(60);
        user.setHeight(165);
        user.setAge(30);
        user.setPalValue(1.4);
        user.setGoal("GAIN");

        when(userRepository.findById(6L)).thenReturn(Optional.of(user));

        double result = userService.calculateDailyCalories(6L);

        // BMR = (10*60) + (6.25*165) - (5*30) - 161 = 600 + 1031.25 - 150 - 161 = 1320.25
        // Total = 1320.25 * 1.4 = 1848.35
        // GAIN = 1848.35 + 500 = 2348.35
        assertEquals(2348.35, result);
    }

    @Test
    void calculateDailyCalories_userNotFound_throwsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.calculateDailyCalories(99L));
    }
}
