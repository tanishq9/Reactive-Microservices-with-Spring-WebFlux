import java.util.function.Function;
import reactor.core.publisher.Flux;

public class OperatorDemo {
	public static void main(String[] args) {
		// handle - filter + map
		Flux.range(1, 20)
				.handle(((integer, synchronousSink) -> {
							if (integer == 7) {
								synchronousSink.complete();
								// synchronousSink.next(integer); // filter
							} else {
								synchronousSink.next(integer + "a"); // map
							}
						})
				)
				.subscribe(new CustomSubscriber());

		Flux
				.generate(
						() -> 1,
						(intParam, synchronousSink) -> {
							synchronousSink.next(intParam);
							return intParam + 1;
						}
				)
				.map(Object::toString)
				.log()
				.handle((string, sink) -> {
					sink.next(string);
					if (string.toLowerCase().equals("5")) {
						sink.complete();
					}
				})
				.log()
				.subscribe();

		// Transform operator
		Flux
				.just(1, 2, 3)
				// transforms the stream flux from one form to another
				.transform(applyIntTransformFlux())
				.doOnDiscard(Integer.class, (integer -> System.out.println("Discarded: " + integer)))
				.subscribe(new CustomSubscriber());
	}

	private static Function<Flux<Integer>, Flux<Integer>> applyIntTransformFlux() {
		return flux -> flux
				.filter(it -> it >= 2)
				.map(it -> it * 2);
	}
}
