import java.util.function.Supplier;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class MonoFromSupplierDemo {
	public static void main(String[] args) throws InterruptedException {
		long start = System.currentTimeMillis();
		getStringMono()
				// Schedulers.boundedElastic() lets to asynchronously execute
				.subscribeOn(Schedulers.boundedElastic())
				.subscribe(
						(event) -> System.out.println(Thread.currentThread())
				);
		long end = System.currentTimeMillis();
		System.out.println("Time taken: " + (end - start));

		start = System.currentTimeMillis();
		getStringMono()
				// Schedulers.boundedElastic() lets to asynchronously consume the stream
				.subscribeOn(Schedulers.boundedElastic())
				.subscribe(
						(event) -> System.out.println(Thread.currentThread())
				);
		end = System.currentTimeMillis();
		System.out.println("Time taken: " + (end - start));

		start = System.currentTimeMillis();
		getStringMono()
				// Schedulers.boundedElastic() lets to asynchronously consume the stream
				.subscribeOn(Schedulers.boundedElastic())
				.subscribe(
						(event) -> System.out.println(Thread.currentThread())
				);
		end = System.currentTimeMillis();
		System.out.println("Time taken: " + (end - start));

		// Added so that the main thread doesn't sleep as using Schedulers.boundedElastic(), we have delegated execution to other threads
		Thread.sleep(7000);
		// Instead of above, we can do .block() on stream which would block the thread until it receives the output, this is not how we should be doing things, just for demonstration
	}

	// By default all reactive streams are executed on main/current thread and because of that we will be seeing blocking behaviour
	static Mono<String> getStringMono() {
		Supplier<String> stringSupplier = () -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Tanishq");
			return "Tanishq";
		};
		Mono<String> mono = Mono
				.fromSupplier(stringSupplier)
				.map(string -> string.toUpperCase());
		return mono;
	}
}
