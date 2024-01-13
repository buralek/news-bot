package buralek.newsbot.collect.config;

import buralek.newsbot.collect.rss.DefaultRssService;
import com.rometools.rome.io.SyndFeedInput;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

@Configuration
@Import({DefaultRssService.class})
public class CollectConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public SyndFeedInput syndFeedInput() {
        return new SyndFeedInput();
    }
}
