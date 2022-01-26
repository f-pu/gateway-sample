package com.example.gatewaysample.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Set;

@Configuration
@Slf4j
public class RouteConfig {
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("call-sample-service", r -> r.path("/service")
                        .filters(f -> f
                                // add ModifyResponseBody filter
                                .modifyResponseBody(String.class, String.class, (exchange, bodyStr) -> {
                                    log.info("Original body is : [{}], " +
                                            "modified to : [The response body is modified]", bodyStr);
                                    return Mono.just("The response body is modified");
                                })
                                // add Circuit breaker filter
                                .circuitBreaker(config -> {
                                    config.setFallbackUri("forward:/fallback");
                                    config.setStatusCodes(Set.of("400"));
                                }))
                        .uri("lb://SAMPLE-SERVICE"))
                .build();
    }
}
