package com.karthikeya.urlshortener.service;

import com.karthikeya.urlshortener.dto.UrlRequest;
import com.karthikeya.urlshortener.entity.Url;
import com.karthikeya.urlshortener.repository.UrlRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UrlService {

    private final UrlRepo urlRepo;
    private static final char[] BASE_62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private final long xorKey;


    public UrlService(UrlRepo urlRepo , @Value("${url.bitmask}") String xorKey) {
        this.xorKey = Long.parseLong(xorKey , 16);
        this.urlRepo = urlRepo;
    }

    @Transactional
    public String createEntry(UrlRequest urlRequest) {
        Url url = new Url();

        url.setOriginalUrl(urlRequest.getOriginalUrl());

        urlRepo.save(url);

        String shortCode =  encodeBase62(url.getId());
        url.setShortUrl(shortCode);

        return shortCode;
    }

    private String encodeBase62(long id) {
        long mixed = id ^ xorKey;
        StringBuilder code = new StringBuilder(11);

        /*
            mixed will be zero only when id == xorKey only once in application
            life cycle. we can make xorKey as mixed, because it will be a
            valid key and is not used before or after since to get xorKey as mixed
            id ^ xorKey need to be equal to xorKey which is only possible for id 0
            and since id is generated from 0 it is safe.
         */
        if(mixed == 0)mixed = xorKey;

        while (mixed > 0) {
            int rem = (int)(mixed % 62);
            code.append(BASE_62_CHARS[rem]);
            mixed /= 62;
        }
        return code.reverse().toString();
    }

    @Transactional
    public String getOriginalUrl(String shortCode) {
        urlRepo.incrementVisitCount(shortCode);
        return urlRepo.findOriginalUrlByShortUrl(shortCode).
                orElseThrow(() -> new EntityNotFoundException("Invalid short URL" + shortCode));
    }
}
