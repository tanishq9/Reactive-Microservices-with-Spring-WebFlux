import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import reactor.core.publisher.Flux;

public class FluxDemo {
	public static void main(String[] args) {
		List<String> list = Arrays.asList("a", "b", "c");
		Flux<?> flux = Flux.fromIterable(list);
		flux.subscribe(
				(event) -> System.out.println(event)
		);

		Stream<String> stream = list.stream();
		stream.forEach(System.out::println);
		// stream.forEach(System.out::println);
		// Exception in thread "main" java.lang.IllegalStateException: stream has already been operated upon or closed

		Flux<?> flux1 = Flux.fromStream(Stream.of(1, 2, 3));

		flux1.subscribe(System.out::println);
		// flux1.subscribe(System.out::println);
		// Caused by: java.lang.IllegalStateException: stream has already been operated upon or closed

		// Use supplier of stream instead
		flux1 = Flux.fromStream(() -> list.stream());

		flux1
				.log()
				.map(str -> str.toString().toUpperCase())
				//.log()
				.subscribe();
		// flux1.subscribe();
	}
}
