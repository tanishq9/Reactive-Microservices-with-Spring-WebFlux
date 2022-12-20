package com.example.HelloSpringWebFlux.webTestClient;

import com.example.HelloSpringWebFlux.dto.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@AutoConfigureWebTestClient
public class ResponseTest {

	// We do not need to up the server locally when testing using WebTestClient
	@Autowired
	private WebTestClient webTestClient;

	@Test
	void test1() {
		Flux<Response> responseMono = this.webTestClient
				.get()
				.uri("/reactive-math/square/{number}", 5)
				.exchange()
				.expectStatus().is2xxSuccessful()
				.returnResult(Response.class)
				.getResponseBody();

		StepVerifier.create(responseMono)
				.expectNextMatches(r -> r.getOutput() == 25)
				.verifyComplete();
	}

	@Test
	void test2() {
		this.webTestClient
				.get()
				.uri("/reactive-math/square/{number}", 5)
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBody(Response.class)
				.value(response -> Assertions.assertThat(response.getOutput()).isEqualTo(25));
	}
}
