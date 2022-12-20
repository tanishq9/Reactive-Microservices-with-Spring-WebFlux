import org.reactivestreams.Subscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CustomSubscriberDemo {
	public static void main(String[] args) {
		Mono<Integer> mono = Mono.just(1);
		Subscriber<Object> subscriber = new CustomSubscriber();
		mono.log().subscribe(subscriber);

		Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5);
		flux.log().subscribe(subscriber);
	}
}
