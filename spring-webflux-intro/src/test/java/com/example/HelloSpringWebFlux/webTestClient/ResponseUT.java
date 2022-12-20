package com.example.HelloSpringWebFlux.webTestClient;

import static org.mockito.ArgumentMatchers.any;

import com.example.HelloSpringWebFlux.controller.ParamsController;
import com.example.HelloSpringWebFlux.controller.ReactiveMathController;
import com.example.HelloSpringWebFlux.controller.ReactiveMathValidationController;
import com.example.HelloSpringWebFlux.dto.MultiplyRequestDto;
import com.example.HelloSpringWebFlux.dto.Response;
import com.example.HelloSpringWebFlux.service.ReactiveMathService;
import java.time.Duration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = {ReactiveMathController.class, ParamsController.class, ReactiveMathValidationController.class})
// @SpringBootTest annotation will initialise all the beans which we have defined before tests are executed, ideally this annotation is only used for Integration tests.
public class ResponseUT {

	// Assume (n+1)th layer is working correctly when doing unit tests for n layer

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private ReactiveMathService reactiveMathService;

	@Test
	public void test1() {
		Mockito.when(reactiveMathService.findSquare(Mockito.anyInt())).thenReturn(Mono.just(new Response(25)));

		this.webTestClient
				.get()
				.uri("/reactive-math/square/{number}", 5)
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBody(Response.class)
				.value(response -> Assertions.assertThat(response.getOutput()).isEqualTo(25));
	}

	@Test
	public void test2() {
		Mockito.when(reactiveMathService.findSquare(Mockito.anyInt())).thenReturn(Mono.empty());

		this.webTestClient
				.get()
				.uri("/reactive-math/square/{number}", 5)
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBody(Response.class)
				.value(response -> Assertions.assertThat(response.getOutput()).isEqualTo(-1));
	}

	@Test
	public void listResponseTest() {
		Flux<Response> flux = Flux.range(1, 3)
				.map(Response::new);

		Mockito.when(reactiveMathService
				.multiplicationTable(Mockito.anyInt()))
				.thenReturn(flux.cast(Object.class));

		this.webTestClient
				.get()
				.uri("/reactive-math/table/{number}", 5)
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Response.class)
				.hasSize(3)
				.value(list -> list.forEach(item ->
						Assertions.assertThat(item.getOutput()).isGreaterThanOrEqualTo(1)
				));
	}

	@Test
	public void streamResponseTest() {
		Flux<Response> flux = Flux.range(1, 3)
				.map(Response::new)
				.delayElements(Duration.ofMillis(100));

		Mockito.when(reactiveMathService
				.multiplicationTable2(Mockito.anyInt()))
				.thenReturn(flux.cast(Object.class));

		this.webTestClient
				.get()
				.uri("/reactive-math/table/{number}/stream", 5)
				.exchange()
				.expectStatus().is2xxSuccessful()
				// .expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
				.expectBodyList(Response.class)
				.hasSize(3)
				.value(list -> list.forEach(item ->
						Assertions.assertThat(item.getOutput()).isGreaterThanOrEqualTo(1)
				));
	}

	@Test
	public void paramsResponseTest() {
		this.webTestClient
				.get()
				.uri("/jobs/search?count={count}&page={page}", 1, 2)
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				// .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
				.expectBodyList(Integer.class)
				.hasSize(2).contains(1, 2);
	}

	@Test
	public void postResponseTest() {
		Mockito.when(reactiveMathService
				.multiply(any()))
				.thenReturn(Mono.just(new Response(18)));

		this.webTestClient
				.post()
				.uri("/reactive-math/multiply")
				.accept(MediaType.APPLICATION_JSON)
				.headers(httpHeaders -> httpHeaders.set("key", "value"))
				.bodyValue(new MultiplyRequestDto(3, 6))
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody(Response.class)
				.value(response -> Assertions.assertThat(response.getOutput()).isEqualTo(18));
	}

	@Test
	public void controllerAdviceTest() {
		Mockito.when(reactiveMathService
				.findSquare(Mockito.anyInt()))
				.thenReturn(Mono.just(new Response(18)));

		this.webTestClient
				.get()
				.uri("/reactive-math/square/{input}/throw", 5)
				.exchange()
				.expectStatus().is4xxClientError()
				.expectBody()
				.jsonPath("$.message").isEqualTo("allowed range is 10 to 20")
				.jsonPath("$.errorCode").isEqualTo(100);
	}

}
