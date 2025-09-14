package net.mysterria.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Value("${gateway.backend.main.url}")
    private String mainBackendUrl;

    @Value("${gateway.backend.archive.url}")
    private String archiveBackendUrl;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("main-api", r -> r.path("/api/**")
                        .filters(f -> f
                                .stripPrefix(0)
                                .circuitBreaker(config -> config
                                        .setName("main-api-circuit-breaker")
                                        .setFallbackUri("forward:/fallback/main"))
                                .requestRateLimiter(config -> config
                                        .setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(hostKeyResolver())))
                        .uri(mainBackendUrl))
                .route("archive-api", r -> r.path("/archive/**")
                        .filters(f -> f
                                .stripPrefix(0)
                                .circuitBreaker(config -> config
                                        .setName("archive-api-circuit-breaker")
                                        .setFallbackUri("forward:/fallback/archive"))
                                .requestRateLimiter(config -> config
                                        .setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(hostKeyResolver())))
                        .uri(archiveBackendUrl))
                .build();
    }

    @Bean
    public org.springframework.cloud.gateway.filter.ratelimit.KeyResolver hostKeyResolver() {
        return exchange -> reactor.core.publisher.Mono.just(
                exchange.getRequest().getRemoteAddress() != null ?
                        exchange.getRequest().getRemoteAddress().getAddress().getHostAddress() :
                        "unknown"
        );
    }

    @Bean
    public org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter redisRateLimiter() {
        return new org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter(100, 200);
    }
}