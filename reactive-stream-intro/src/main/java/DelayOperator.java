import java.time.Duration;
import reactor.core.publisher.Flux;

public class DelayOperator {
	public static void main(String[] args) throws InterruptedException {
		Flux.range(1, 100) // 32 is default buffer size
				.log() // we can notice around 75% elements (24) are requested at a time before they are all drained
				.delayElements(Duration.ofMillis(100)) // it drains element one by one with interval of 1 seconds
				.subscribe(new CustomSubscriber());

		// 32 elements are pulled first (default buffer size)
		// When 75% of 32 elements are drained i.e 24 (and 8 i.e. 25% are left) then next 75% i.e. 24 elements are pulled, maximum number of elements in buffer can be 32 only.
		Thread.sleep(12000);
	}
}
