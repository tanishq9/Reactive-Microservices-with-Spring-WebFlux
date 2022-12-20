package com.example.HelloSpringWebFlux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {
	@Bean
	public WebClient webClient() {
		return WebClient
				.builder()
				.baseUrl("http://localhost:8080")
				// enriching every request with Authorization header
				.filter((request, exchangeFunction) -> {
					// here we can check validity of existing header like auth-token and generate new one if required
					// if we would have only used .headers() then request checking wouldn't have been possible
					// Setting the value of authorization header
					// ClientRequest clientRequest = ClientRequest.from(request).headers(h -> h.setBearerAuth("some-token")).build();
					// exchanging original request with new request
					//return exchangeFunction.exchange(clientRequest);
					return createNewClientRequestOnBasisOfAttribute(request, exchangeFunction);
				})
				.build();
	}

	private Mono<ClientResponse> createNewClientRequestOnBasisOfAttribute(ClientRequest request, ExchangeFunction exchangeFunction) {
		// Setting the value of authorization header based on attribute value
		ClientRequest clientRequest = request.attribute("auth")
				.map(v -> v.equals("basic") ? withBasicAuth(request) : withOAuth(request))
				.orElse(request);
		return exchangeFunction.exchange(clientRequest);
	}

	private ClientRequest withOAuth(ClientRequest request) {
		return ClientRequest.from(request).headers(h -> h.setBearerAuth("some-token")).build();
	}

	private ClientRequest withBasicAuth(ClientRequest request) {
		return ClientRequest.from(request).headers(h -> h.setBasicAuth("username", "password")).build();
	}
}
