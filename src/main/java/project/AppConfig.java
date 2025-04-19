package project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import project.model.CacheMap;

import java.time.Instant;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(); // Spring manages this object
    }

    @Bean
    public CacheMap powerStatus() {
        CacheMap cacheMap = new CacheMap();
        cacheMap.setValue("activePowerW", 0, Instant.MIN);
        return cacheMap;
    }
}

