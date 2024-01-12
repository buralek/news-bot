package buralek.newsbot.data.repository;

import buralek.newsbot.data.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface SubscriptionRepository extends JpaRepository<Subscription, BigInteger> {
}
