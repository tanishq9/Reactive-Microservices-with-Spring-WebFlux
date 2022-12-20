import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxFromToMono {
	public static void main(String[] args) {
		Mono<String> mono = Mono.just("a");
		// Flux from Mono
		Flux<String> flux = Flux.from(mono);
		// Flux can emit 0 or 1 item as well like Mono
		flux.subscribe(
				(event) -> System.out.println("Received: " + event)
		);

		// Flux to Mono
		Flux<Integer> flux1 = Flux.just(1, 2, 3);

		flux1
		//		.next()
		//		.filter(i -> i > 2)
				.subscribe(System.out::println);

		System.out.println();

		flux1
				.filter(i -> i > 2)
				.next() // This would be used to convert flux to mono
				.subscribe(System.out::println);
	}
}
