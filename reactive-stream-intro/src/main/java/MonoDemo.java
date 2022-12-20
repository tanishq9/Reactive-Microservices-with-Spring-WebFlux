import java.util.concurrent.Callable;
import reactor.core.publisher.Mono;

public class MonoDemo {
	public static void main(String[] args) {
		// publisher
		Mono<Integer> mono = Mono.just(1);
		System.out.println(mono);

		// in order for publisher to emit data, we have to subscribe
		mono.subscribe(integer -> System.out.println("Received: " + integer));

		// onNext(), onError(), onComplete() stream methods would be invoked by the publisher, we can mention behaviour
		Mono<Integer> mono1 = Mono.just(2)
				.doOnNext(integer -> System.out.println("Sent: " + integer))
				.doOnSuccess(integer -> System.out.println("Completed: " + integer));

		mono1.subscribe();

		// we can also decide what would happen in case of next, error and completion for the subscriber
		mono.subscribe(
				integer -> System.out.println("Received: " + integer),
				error -> System.out.println("Error: " + error.getMessage()),
				() -> System.out.println("Completed")
		);
	}
}
