package buralek.newsbot.collect.rss;

import buralek.newsbot.collect.exception.CollectException;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class DefaultRssService implements RssService {
    private final Logger LOG = LoggerFactory.getLogger(DefaultRssService.class);
    private final SyndFeedInput input;
    private final RestTemplate restTemplate;

    public DefaultRssService(SyndFeedInput input, RestTemplate restTemplate) {
        this.input = input;
        this.restTemplate = restTemplate;
    }

    @Override
    public SyndFeed getLastNews(String url) throws CollectException {
        try {
            LOG.info("Try to get news by RSS from url = {}", url);
            ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(new URI(url), byte[].class);

            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                throw new CollectException("RSS request to " + url +
                        "failed with status code: " + responseEntity.getStatusCode());
            }
            if (responseEntity.getBody() == null) {
                throw new CollectException("Response from " + url + " is empty. Can't create feed to parse it");
            }

            InputStream responseStream = new ByteArrayInputStream(responseEntity.getBody());
            SyndFeed syndFeed = input.build(new InputSource(responseStream));
            LOG.info("Last news from {} received successfully", url);
            return syndFeed;
        } catch (URISyntaxException e) {
            throw new CollectException("Can't parse url " + url, e);
        } catch (FeedException e) {
            throw new CollectException("Can't parse the response from " + url, e);
        }
    }
}
