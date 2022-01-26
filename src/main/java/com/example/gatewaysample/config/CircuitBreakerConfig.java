package com.example.gatewaysample.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Slf4j
public class CircuitBreakerConfig {
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> {
            io.github.resilience4j.circuitbreaker.CircuitBreakerConfig config = io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom()
                    .failureRateThreshold(50)
                    .slowCallRateThreshold(50)
                    .slowCallDurationThreshold(Duration.ofSeconds(10L))
                    .permittedNumberOfCallsInHalfOpenState(5)
                    .slidingWindowSize(10)
                    .minimumNumberOfCalls(10)
                    .waitDurationInOpenState(Duration.ofSeconds(60))
                    .automaticTransitionFromOpenToHalfOpenEnabled(true)
                    .build();
            CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
            registry.getEventPublisher()
                    .onEntryAdded(added -> added.getAddedEntry()
                            .getEventPublisher()
                            .onEvent(event -> log.info("Circuit breaker event：{}", event.getEventType().name())));
            factory.configureCircuitBreakerRegistry(registry);
        };
    }
}
