package buralek.newsbot.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, BigInteger> {
}
