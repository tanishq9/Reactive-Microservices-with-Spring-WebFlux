import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class CustomSubscriber implements Subscriber<Object> {

	Subscription subscription;

	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
		this.subscription.request(Long.MAX_VALUE);
		// this.subscription.request(2);
	}

	@Override
	public void onNext(Object o) {
		System.out.println("Received: " + o);
	}

	@Override
	public void onError(Throwable throwable) {
		System.out.println("Error: " + throwable.getMessage());
	}

	@Override
	public void onComplete() {
		System.out.println("Completed");
	}
}
