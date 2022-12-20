import reactor.core.publisher.Flux;

public class MapVsFlatMap {
	public static void main(String[] args) throws InterruptedException {
		Flux<Integer> flux = Flux.create(fluxSink -> {
			fluxSink.next(1);
			fluxSink.next(2);
			fluxSink.next(3);
			fluxSink.next(4);
			fluxSink.next(5);
			fluxSink.next(6);
			fluxSink.next(7);
			fluxSink.next(8);
			fluxSink.complete();
		});

		// map changes item type from T to V in the input Flux
		flux.map(
				integer -> {
					return integer * 10 + "a";
				}
		).subscribe(new CustomSubscriber());

		// flatMap changes item type from T to Flux<V>/any publisher in the input Flux (This transformation happens asynchronously)
		// All these Flux<V> publishers are then flattened into a single Flux i.e. Flux<V>
		flux
				//.map(
				.flatMap(
						(
								integer -> {
									return Flux.create(fluxSink -> {
										fluxSink.next(integer + "a");
										fluxSink.next("b");
										fluxSink.next("c");
										fluxSink.next("d");
										fluxSink.next("e");
									});
								}
						)
				)
				//.delayElements(Duration.ofSeconds(1))
				.subscribe(new CustomSubscriber());

		// Thread.sleep(10000);


		/*Flux<Integer> flux1 = Flux.just(1, 2, 3, 4, 5)
				.map(i -> i * 10) // Flux<Integer> to Flux<Integer>
				.map(i -> {
					return Flux.just(1, 2, 3, 4, 5); // Flux<Integer> to Flux<Integer>
				});*/
				// .map(i -> i);

		//flux1.subscribe(new CustomSubscriber());
	}
}
