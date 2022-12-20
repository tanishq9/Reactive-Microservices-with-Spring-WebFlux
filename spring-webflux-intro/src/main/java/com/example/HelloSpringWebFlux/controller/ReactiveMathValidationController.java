package com.example.HelloSpringWebFlux.controller;

import com.example.HelloSpringWebFlux.dto.Response;
import com.example.HelloSpringWebFlux.exception.InputValidationException;
import com.example.HelloSpringWebFlux.service.ReactiveMathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("reactive-math")
public class ReactiveMathValidationController {

	@Autowired
	private ReactiveMathService mathService;

	@GetMapping("square/{input}/throw")
	public Mono<Response> findSquare(@PathVariable int input) {
		if (input < 10 || input > 20) {
			throw new InputValidationException(input);
		}
		return this.mathService.findSquare(input);
	}

	@GetMapping("square/{input}/mono-error")
	public Mono<Response> monoError(@PathVariable int input) {
		return Mono.just(input)
				.handle((integer, responseSynchronousSink) ->
				{
					if (integer < 10 || integer > 20) {
						responseSynchronousSink.error(new InputValidationException(input));
					} else {
						responseSynchronousSink.next(integer);
					}
				})
				.cast(Integer.class)
				.flatMap(i -> this.mathService.findSquare(i));
	}
}
