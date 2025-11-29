package com.example.demo.serviceImpl;


import java.util.Map;
import java.time.Duration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;

@Service
public class GenericApiService {
	
	  private final WebClient.Builder webClientBuilder;
	    private final ObjectMapper mapper = new ObjectMapper();

	    public GenericApiService(WebClient.Builder webClientBuilder) {
	        this.webClientBuilder = webClientBuilder;
	    }

	    public String callApi(String url, String method, String headersJson) throws Exception {
	        WebClient client = webClientBuilder.build();

	        WebClient.RequestHeadersSpec<?> req;
	        
	        if ("POST".equalsIgnoreCase(method)) {
	            req = client.post().uri(url);
	        } else {
	            req = client.get().uri(url);
	        }
	        
	        if (headersJson != null && !headersJson.isBlank()) {
	            Map<String, String> headers = mapper.readValue(headersJson, Map.class);
	            for (Map.Entry<String, String> e : headers.entrySet()) {
	                req = req.header(e.getKey(), e.getValue());
	            }
	        }
	        
	        Mono<String> mono = req
	                .accept(MediaType.APPLICATION_JSON)
	                .exchangeToMono((ClientResponse r) -> {
	                    if (r.statusCode().is2xxSuccessful()) {
	                        return r.bodyToMono(String.class);
	                    } else {
	                        return r.bodyToMono(String.class).flatMap(body -> Mono.error(new RuntimeException(
	                                "External call failed: " + r.statusCode().value() + " - " + body)));
	                    }
	                });

	
	        return mono.block(Duration.ofSeconds(20));
	    }

}
