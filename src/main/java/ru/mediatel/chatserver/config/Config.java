package ru.mediatel.chatserver.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class Config {

    @Value("${system.dbservice-url}")
    private String dbServiceUrl;
}
