package com.example.HelloSpringWebFlux.service;

import com.example.HelloSpringWebFlux.dto.Response;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;

@Service
public class MathService {
	public Response findSquare(int number) {
		return new Response(number * number);
	}

	public List<Response> multiplicationTable(int number) {
		return IntStream.rangeClosed(1, 10)
				.peek(i -> {
					try {
						// adding 1 second processing time
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				})
				.peek(i -> System.out.println("Element processed: " + i))
				.mapToObj(i -> new Response(number * i))
				.collect(Collectors.toList());
	}
}
