import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class SchedulersDemo {
	public static void main(String[] args) throws InterruptedException {
		Flux<Integer> flux = Flux
				.just(1, 2, 3, 4, 5)
				.subscribeOn(Schedulers.boundedElastic()) // all operations everything would be scheduled as per the scheduler mentioned
				.map(i -> {
					// Assume it to be some network call / blocking library
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return i;
				});
		// .publishOn(Schedulers.boundedElastic()); operations below would be scheduled as per scheduler mentioned

		// Everything by default gets executed in the current/calling thread, because of that we would be seeing blocking behaviour due to I/O calls (blocking library), Thread.sleep(), i.e. this would block the current thread.
		// If we want to have async behaviour then we should use apt. scheduler with apt. methods like boundedElastic() (for n/w, time-consuming calls), parallel (CPU intensive) tasks, etc.

		long start = System.currentTimeMillis();

		int count = 50;
		while (count-- > 0) {
			flux.subscribe(event -> System.out.println("Current thread is: " + Thread.currentThread().getName()));
		}

		System.out.println("-------");

		flux.subscribe(event -> System.out.println("Current thread is: " + Thread.currentThread().getName()));

		long end = System.currentTimeMillis();

		System.out.println(end - start);

		Thread.sleep(10000);
	}
}