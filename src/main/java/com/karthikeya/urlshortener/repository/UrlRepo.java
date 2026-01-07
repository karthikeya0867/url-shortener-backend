package com.karthikeya.urlshortener.repository;

import com.karthikeya.urlshortener.entity.Url;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UrlRepo extends JpaRepository<@NonNull  Url , @NonNull Long> {

    @Modifying
    @Query("""
          UPDATE Url u
                    SET u.visitCount = u.visitCount + 1
                              where u.shortUrl = :shortUrl
          """)
    void incrementVisitCount(String shortUrl);

    @Query("""
           select u.originalUrl
                      from Url u
                                 where u.shortUrl = :shortUrl
           """)
    Optional<@NonNull String> findOriginalUrlByShortUrl(String shortUrl);
}
