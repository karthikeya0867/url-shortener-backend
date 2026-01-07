package com.karthikeya.urlshortener.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "url_seq")
    @SequenceGenerator(
            name = "url_seq",
            sequenceName = "url_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false , length = 2048)
    private String originalUrl;

    @Column(length = 15 , unique = true)
    private String shortUrl;

    @Column(nullable = false)
    private long visitCount;

    @Column(updatable = false)
    @CreationTimestamp
    private Instant createdAt;
}
