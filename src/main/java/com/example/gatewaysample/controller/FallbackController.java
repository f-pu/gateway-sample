package com.example.gatewaysample.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author fei.pu@accenture.com
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {
    @RequestMapping
    public Mono<String> fallback() {
        return Mono.just("Fallback method is called!");
    }
}
