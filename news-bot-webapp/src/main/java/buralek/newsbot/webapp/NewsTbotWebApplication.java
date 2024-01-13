package buralek.newsbot.webapp;

import buralek.newsbot.logic.config.LogicConfig;
import buralek.newsbot.telegram.config.TelegramConfig;
import buralek.newsbot.webapp.config.OpenApiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
        OpenApiConfig.class,
        TelegramConfig.class,
        LogicConfig.class
})
public class NewsTbotWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsTbotWebApplication.class, args);
    }

}
