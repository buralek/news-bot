package buralek.newsbot.webapp.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "${service-settings.name}", version = "${service-settings.version}"))
public class OpenApiConfig {
}
