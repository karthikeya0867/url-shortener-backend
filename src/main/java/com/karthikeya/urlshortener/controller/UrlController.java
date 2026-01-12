package com.karthikeya.urlshortener.controller;

import com.karthikeya.urlshortener.dto.UrlRequest;
import com.karthikeya.urlshortener.dto.UrlResponse;
import com.karthikeya.urlshortener.service.UrlService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/url")
public class UrlController {

    private final UrlService urlService;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    public ResponseEntity<@NonNull UrlResponse> createEntry(@RequestBody UrlRequest urlRequest) {
        UrlResponse urlResponse = new UrlResponse();
        urlResponse.setOriginalUrl(urlRequest.getOriginalUrl());
        urlResponse.setShortUrl(urlService.createEntry(urlRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(urlResponse);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<@NonNull UrlResponse> getEntry(@PathVariable String shortCode) {
        String url = urlService.getOriginalUrl(shortCode);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new UrlResponse(url , shortCode));
    }
}

