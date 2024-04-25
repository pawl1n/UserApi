package pawlin.userapi.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("user")
public record UserProperties(Integer minimumAge) {
}
