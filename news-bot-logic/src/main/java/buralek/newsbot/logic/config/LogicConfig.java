package buralek.newsbot.logic.config;

import buralek.newsbot.collect.config.CollectConfig;
import buralek.newsbot.data.config.DataConfig;
import buralek.newsbot.logic.action.DefaultActionService;
import buralek.newsbot.logic.collect.DefaultCollectNewsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@Import({
        CollectConfig.class,
        DataConfig.class,
        DefaultCollectNewsService.class,
        DefaultActionService.class
})
@EnableScheduling
public class LogicConfig {
}
