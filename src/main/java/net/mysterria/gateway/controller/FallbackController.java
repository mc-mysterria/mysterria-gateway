package net.mysterria.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/main")
    public Mono<ResponseEntity<Map<String, Object>>> mainFallback() {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "error", "Main API service is currently unavailable",
                        "timestamp", LocalDateTime.now(),
                        "service", "main-api"
                )));
    }

    @GetMapping("/archive")
    public Mono<ResponseEntity<Map<String, Object>>> archiveFallback() {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "error", "Archive API service is currently unavailable",
                        "timestamp", LocalDateTime.now(),
                        "service", "archive-api"
                )));
    }
}