package buralek.newsbot.data.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"buralek.newsbot.data"})
@EntityScan(basePackages = {"buralek.newsbot.data"})
public class DataConfig {
}
