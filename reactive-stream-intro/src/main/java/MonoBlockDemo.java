import java.util.function.Supplier;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class MonoBlockDemo {
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		// The execution thread (main or any) would be blocked until the stream is consumed, not a good practice, would the thread be blocked for other operations as well?
		String output = getStringMono()
				// Schedulers.boundedElastic() lets to asynchronously consume the stream
				.subscribeOn(Schedulers.boundedElastic())
				.doOnNext((string) -> System.out.println(Thread.currentThread()))
				.block();
		System.out.println(output);
		long end = System.currentTimeMillis();
		System.out.println("Time taken: " + (end - start));


		start = System.currentTimeMillis();
		output = getStringMono()
				.doOnNext((string) -> System.out.println(Thread.currentThread()))
				.block();
		System.out.println(output);
		end = System.currentTimeMillis();
		System.out.println("Time taken: " + (end - start));


		start = System.currentTimeMillis();
		output = getStringMono()
				.doOnNext((string) -> System.out.println(Thread.currentThread()))
				.block();
		System.out.println(output);
		end = System.currentTimeMillis();
		System.out.println("Time taken: " + (end - start));
	}

	// By default all reactive streams are executed on main/current thread and because of that we will be seeing blocking behaviour
	static Mono<String> getStringMono() {
		Supplier<String> stringSupplier = () -> {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// System.out.println("Tanishq");
			return "Tanishq";
		};
		Mono<String> mono = Mono
				.fromSupplier(stringSupplier)
				.map(string -> string.toUpperCase());
		return mono;
	}
}
