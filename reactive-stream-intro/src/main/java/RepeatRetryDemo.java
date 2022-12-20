import java.time.Duration;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

public class RepeatRetryDemo {
	public static void main(String[] args) throws InterruptedException {
		// repeat: resubscribe after complete signal
		Flux.range(1, 5)
				.doOnSubscribe(s -> System.out.println(s))
				.doOnComplete(() -> System.out.println("Completed - 1"))
				.repeat(2)
				.doOnComplete(() -> System.out.println("Completed - 2"))
				.subscribe(new CustomSubscriber());

		// retry: resubscribe after error signal
		Flux.range(1, 5)
				.map(i -> i / 0)
				.doOnError(e -> System.out.println(e.getMessage()))
				.retry(2)
				.subscribe(new CustomSubscriber());

		Flux.range(1, 5)
				.map(i -> i / 0)
				.doOnError(e -> System.out.println(e.getMessage()))
				.retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(3)))
				.subscribe(new CustomSubscriber());

		Thread.sleep(10000);
	}
}
