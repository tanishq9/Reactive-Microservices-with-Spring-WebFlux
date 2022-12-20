import java.util.concurrent.atomic.AtomicInteger;
import reactor.core.publisher.Flux;

public class FluxGenerate {
	public static void main(String[] args) {
		// Flux.generate() is kind of a loop wherein inside generate what we mention is executed until we somehow stop it (complete/error) either from server side (some break logic) or from downstream (take operator).
		AtomicInteger atomicInteger = new AtomicInteger(5);
		Flux
				.generate(
						synchronousSink -> {
							System.out.println("Emitting: " + atomicInteger);
							synchronousSink.next(atomicInteger);
							atomicInteger.getAndAdd(-1);
							if (atomicInteger.get() < 1) {
								synchronousSink.complete();
							}
						}
				)
				.log()
				//.take(2)
				// .log()
				.subscribe(new CustomSubscriber());

		System.out.println("--------------");

		// Drawback of above counter approach is that it is declared outside and if someone can get reference of this atomicInteger and increment it outside
		// How to avoid this?
		// We can maintain state of variable/counter using another implementation of generate method
		Flux
				.generate(
						() -> {
							return 5; // supplier, to initialise value of counter (state)
						},
						(state, sink) -> { // bi-function, return updated value of state
							System.out.println("Emitting: " + state);
							sink.next(state);
							if (state <= 1) {
								sink.complete();
							}
							return state - 1;
						}
				)
				.log()
				.subscribe(new CustomSubscriber());
	}
}
