package de.hilsky.bookservice.openai;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "openai.api")
public class OpenAiConfig {
    private String url;
    private String key;
    private String model;
}