package project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.service.PowerService;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
public class PowerServiceTest {

    @Autowired
    private PowerService powerService;

    @Test
    void getDataTest() {
        String data = powerService.getData("activePowerW");
        assertThat(data).isInstanceOf(String.class);
        assertThat(Objects.equals(data, "")).isEqualTo(false);

        String secondPollData = powerService.getData("activePowerW");
        assertThat(Objects.equals(data, secondPollData)).isEqualTo(true);
    }

    @Test
    void getDataInvalidKeyTest() {
        assertThatThrownBy(() -> {
            powerService.getData("key");
        }).isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            powerService.getData(null);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
