package com.example.HelloSpringWebFlux.webClient;

import com.example.HelloSpringWebFlux.controller.ReactiveMathController;
import com.example.HelloSpringWebFlux.dto.InputFailedValidationResponse;
import com.example.HelloSpringWebFlux.dto.MultiplyRequestDto;
import com.example.HelloSpringWebFlux.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ResponseTest extends BaseTest {

	@Autowired
	private WebClient webClient;

	@Autowired
	private ReactiveMathController reactiveMathController;

	@Test
	void test1() {
		// WebClient is non-blocking and async, either we have to use StepVerifier (recommended) or use .block() for tests if not then test would be exited immediately as thread is not blocked.
		Mono<Response> mono = this.webClient
				.get()
				.uri("/reactive-math/square/{input}", 5)
				.retrieve()
				.bodyToMono(Response.class);

		StepVerifier.create(mono)
				.expectNextMatches(i -> i.getOutput() == 25)
				.verifyComplete();
	}

	@Test
	void test2() {
		Flux<Response> flux = this.webClient
				.get()
				.uri("/reactive-math/table/{input}", 5)
				.retrieve()
				.bodyToFlux(Response.class)
				.doOnNext(i -> System.out.println("Received: " + i));

		StepVerifier.create(flux)
				.expectNextCount(10)
				.verifyComplete();
	}

	@Test
	void test3() {
		Flux<Response> flux = this.webClient
				.get()
				.uri("/reactive-math/table/{input}/stream", 5)
				.retrieve()
				.bodyToFlux(Response.class)
				.doOnNext(i -> System.out.println("Received: " + i));

		StepVerifier.create(flux)
				.expectNextCount(10)
				.verifyComplete();
	}

	@Test
	void test4() {
		Mono<Response> mono = this.webClient
				.post()
				.uri("/reactive-math/multiply")
				.bodyValue(new MultiplyRequestDto(1, 2))
				.retrieve() // send the request and give me the response
				.bodyToMono(Response.class)
				.doOnNext(i -> System.out.println("Received: " + i));


		StepVerifier.create(mono)
				.expectNextMatches(i -> i.getOutput() == 2)
				.verifyComplete();
	}

	@Test
	void test5() {
		Mono<Response> mono = this.webClient
				.get()
				.uri("/reactive-math/square/{input}/throw", 5)
				.retrieve()
				.bodyToMono(Response.class)
				.doOnNext(i -> System.out.println("Received: " + i))
				.doOnError(e -> System.out.println("Error: " + e.getMessage()));

		StepVerifier.create(mono)
				.verifyError(WebClientException.class);
	}

	@Test
	void test6() {
		Mono<Object> mono = this.webClient
				.get()
				.uri("/reactive-math/square/{input}/throw", 5)
				.exchangeToMono(clientResponse -> exchange(clientResponse)) // send the request and if we want to work on the response
				.doOnNext(i -> System.out.println("Received: " + i));

		StepVerifier.create(mono)
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	void test7() {
		Flux<Integer> flux = this.webClient
				.get()
				.uri("/jobs/search?count={count}&page={page}", 5, 5)
				.retrieve()
				.bodyToFlux(Integer.class)
				.doOnNext(i -> System.out.println("Received: " + i));

		StepVerifier.create(flux)
				.expectNextCount(2)
				.verifyComplete();
	}


	@Test
	void test8() {
		Flux<Integer> flux = this.webClient
				.get()
				.uri("/jobs/search?count={count}&page={page}", 5, 5)
				.attribute("auth", "oauth")
				.retrieve()
				.bodyToFlux(Integer.class)
				.doOnNext(i -> System.out.println("Received: " + i));

		// Authorization=Basic dXNlcm5hbWU6cGFzc3dvcmQ=
		// Authorization=Bearer some-token

		StepVerifier.create(flux)
				.expectNextCount(2)
				.verifyComplete();
	}

	private Mono<Object> exchange(ClientResponse clientResponse) {
		if (clientResponse.rawStatusCode() == 400) {
			// response body would be of InputFailedValidationResponse type in case of success
			return clientResponse.bodyToMono(InputFailedValidationResponse.class);
		} else {
			// response body would be of Response type in case of success
			return clientResponse.bodyToMono(Response.class);
		}
	}
}
