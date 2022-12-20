import reactor.core.publisher.Flux;

public class FluxCreate {
	public static void main(String[] args) {
		Flux
				.create(
						fluxSink -> {
							// When we are creating Flux via FluxSink, we have more control over Flux that when we want to do next(), error() and complete() unlike previous lectures where we have created Flux from an array, stream or using just.
							fluxSink.next(1);
							fluxSink.complete();
						}
				)
				.log()
				.subscribe(System.out::println);

		Flux
				.create(
						fluxSink -> {
							int num = 0;
							do {
								num += 1;
								System.out.println("Emitting: " + num);
								fluxSink.next(num);
							} while (num <= 5 && !fluxSink.isCancelled());
							fluxSink.complete();
						}
				)
				.log()
				.take(2) // cancels and issues complete signal to downstream
				.log()
				.subscribe(new CustomSubscriber());
	}
}
