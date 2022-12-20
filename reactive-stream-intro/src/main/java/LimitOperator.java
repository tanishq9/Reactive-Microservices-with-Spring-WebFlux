import reactor.core.publisher.Flux;

public class LimitOperator {
	public static void main(String[] args) {
		Flux.range(1, 100)
				.log()
				.limitRate(100, 100)
				.map(i -> i * 10)
				.subscribe();

	}
}
