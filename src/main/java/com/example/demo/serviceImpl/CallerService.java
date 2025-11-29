package com.example.demo.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class CallerService {
	
	@Autowired
	WebClient.Builder webClientBuilder;

	    public String callExternalApi(String url, String token) {

	        return webClientBuilder.build()
	                .get()
	                .uri(url)
	                .header("Authorization", "Bearer " + token)
	                .retrieve()
	                .bodyToMono(String.class)
	                .onErrorResume(ex -> Mono.just("API Error: " + ex.getMessage()))
	                .block();
	    }

}
