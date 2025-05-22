package com.fuchs.api_gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimitConfig {


    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new  RedisRateLimiter(5,10);

    }

    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(
                exchange.getRequest().getHeaders()
                        .getFirst("Authorization")
                        .replace("Bearer ", "")
        );
    }
}
