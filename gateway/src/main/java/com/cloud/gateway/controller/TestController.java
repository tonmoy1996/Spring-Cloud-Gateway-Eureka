package com.cloud.gateway.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value = "/test")
public class TestController {
    @Autowired
    WebClient.Builder webClientBuilder;

    @Value("${string.test:hi}")
    public String text;


    @GetMapping(value = "/hello")
    public ResponseEntity<Flux<Post>> test() {


        Flux<Post> posts=
                webClientBuilder.build()
                        .get()
                        .uri("https://jsonplaceholder.typicode.com/posts")
                        .retrieve()
                        .bodyToFlux(Post.class);



        return ResponseEntity.ok(posts);
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Post{
        int id;
        String userId;
        String title;
        String body;
    }
}
