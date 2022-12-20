package com.example.HelloSpringWebFlux.service;

import com.example.HelloSpringWebFlux.dto.MultiplyRequestDto;
import com.example.HelloSpringWebFlux.dto.Response;
import java.time.Duration;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReactiveMathService {

	public Mono<Response> findSquare(int input) {
		// do it lazily, i.e. emit data when subscribed and avoid using Mono.just() as that computes element to be emitted beforehand
		return Mono.fromSupplier(() -> new Response(input * input));
	}

	public Flux<Object> multiplicationTable(int input) {
		return Flux.create(
				responseFluxSink -> {
					int m = 1;
					while (m++ <= 10) {
						// emit element
						responseFluxSink.next(new Response(input * m));
					}
					responseFluxSink.complete();
				}
		).doOnNext(i -> {
			try {
				// adding 1 second processing time
				System.out.println("Emitted: " + i);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}

	public Flux<Object> multiplicationTable2(int input) {
		return Flux
				.create(
						responseFluxSink -> {
							int m = 1;
							while (m++ <= 10) {
								// emit element
								responseFluxSink.next(new Response(input * m));
							}
							responseFluxSink.complete();
						}
				)
				.delayElements(Duration.ofSeconds(1))
				.doOnNext(i -> {
					// adding 1 second processing time
					System.out.println("Emitted: " + i);
				});
	}

	public Mono<Response> multiply(Mono<MultiplyRequestDto> multiplyRequestDtoMono) {
		return multiplyRequestDtoMono
				.map(multiplyRequestDto -> new Response(multiplyRequestDto.getFirst() * multiplyRequestDto.getSecond()));
	}
}
