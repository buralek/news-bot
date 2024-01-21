package buralek.newsbot.logic.action;

import buralek.newsbot.data.entity.Page;
import buralek.newsbot.data.entity.Subscription;

import java.time.temporal.ChronoUnit;
import java.util.List;

public interface ActionService {
    List<Page> getLastNews(int amount, ChronoUnit chronoUnit);
    String addSubscription(String name, String url);
    String deleteSubscription(String name);
    List<Subscription> findAllSubscriptions();
}
