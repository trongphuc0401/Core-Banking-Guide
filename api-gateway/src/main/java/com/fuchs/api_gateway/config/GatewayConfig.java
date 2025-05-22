package com.fuchs.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("customer-service", r -> r
                        .path("/api/customers/**")
                        .filters(f -> f
                                .addRequestHeader("X-Gateway-Header", "API-Gateway")
                                .stripPrefix(1))
                        .uri("lb://CUSTOMER-SERVICE"))

                .route("transaction-service", r -> r
                        .path("/api/transactions/**")
                        .filters(f -> f
                                .addRequestHeader("X-Gateway-Header", "API-Gateway")
                                .stripPrefix(1)) // strip "/api", giữ "/transactions/**"
                        .uri("lb://TRANSACTION-SERVICE"))

                // Route swagger UI nếu cần
                .route("transaction-service-swagger", r -> r
                        .path("/transaction-api/**") // Đường dẫn từ gateway
                        .filters(f -> f
                                .stripPrefix(1)) // Chuyển "/transaction-api" thành "/"
                        .uri("lb://TRANSACTIONS"))
                .build();
    }



}
