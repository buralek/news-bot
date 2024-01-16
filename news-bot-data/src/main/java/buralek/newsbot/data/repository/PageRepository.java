package buralek.newsbot.data.repository;

import buralek.newsbot.data.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

public interface PageRepository extends JpaRepository<Page, BigInteger> {
    Page findByUrl(String url);

    List<Page> findByUrlIn(Set<String> urls);

    List<Page> findAllByPublishedDateAfter(java.util.Date publishedDate);
}
