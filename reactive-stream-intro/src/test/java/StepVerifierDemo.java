import java.time.Duration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class StepVerifierDemo {

	@Test
	void test1() {
		Flux<Integer> flux = Flux.just(1, 2, 3);

		StepVerifier
				.create(flux)
				.expectNext(1, 2, 3)
				.verifyComplete();
	}

	@Test
	void test2() {
		Flux<Integer> integerFlux = Flux.just(1, 2, 3);
		Flux<Integer> flux = Flux.error(new RuntimeException("error"));

		StepVerifier
				.create(Flux.concat(integerFlux, flux))
				.expectNext(1, 2, 3)
				.verifyErrorMessage("error");
	}

	@Test
	void test3() {
		Flux<Integer> just = Flux.range(1, 50);

		StepVerifier.create(just)
				.expectNextCount(50)
				.verifyComplete();
	}

	@Test
	void test4() {
		Flux<Integer> just = Flux.range(1, 50);

		StepVerifier.create(just)
				// used to programmatically check values emitted
				.thenConsumeWhile(i -> i <= 50)
				.verifyComplete();
	}

	@Test
	void test5() {
		Mono<Integer> mono = Mono.just(1);

		StepVerifier
				.create(mono)
				.assertNext(Assertions::assertNotNull)
				.expectComplete()
				.verify(Duration.ofSeconds(1)); // We can set timer for test case as well.
	}
}
