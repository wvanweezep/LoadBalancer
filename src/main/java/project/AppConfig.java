package project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import project.model.PowerStatus;

import java.time.Instant;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(); // Spring manages this object
    }

    @Bean
    public PowerStatus powerStatus() {
        return new PowerStatus();
    }
}

