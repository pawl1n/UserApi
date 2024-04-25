package pawlin.userapi.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pawlin.userapi.model.User;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(UserProperties.class)
public class Config {
    @Bean
    public Map<Long, User> users() {
        return new HashMap<>();
    }
}
