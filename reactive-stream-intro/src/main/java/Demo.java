import java.util.stream.Stream;

public class Demo {
	public static void main(String[] args) {
		Stream<Integer> stream = Stream.of(1);
		stream = stream.map(integer -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return integer * 2;
		});

		System.out.println(stream);

		// Nothing happens unless we connect a terminal operator
		// In fact stream should be lazy, we should not invoke/publish anything if any subscriber hasn't subscribed to the stream.
		stream.forEach(System.out::println);
	}
}
