package com.example.HelloSpringWebFlux.controller;

import com.example.HelloSpringWebFlux.dto.MultiplyRequestDto;
import com.example.HelloSpringWebFlux.dto.Response;
import com.example.HelloSpringWebFlux.service.ReactiveMathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("reactive-math")
public class ReactiveMathController {
	@Autowired
	private ReactiveMathService reactiveMathService;

	@GetMapping("square/{input}")
	public Mono<Response> findSquare(@PathVariable int input) {
		return this.reactiveMathService.findSquare(input)
				.defaultIfEmpty(new Response(-1));
	}

	@GetMapping("table/{input}")
	public Flux<Object> multiplicationTable(@PathVariable int input) {
		return this.reactiveMathService.multiplicationTable(input);
	}

	// web-browser / calling server would be subscribing to the reactive stream
	// we would always we returning publisher of some type like mono or flux

	@GetMapping(value = "table/{input}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Object> multiplicationTableStream(@PathVariable int input) {
		return this.reactiveMathService.multiplicationTable2(input);
	}

	@PostMapping(value = "multiply")
	public Mono<Response> multiply(@RequestBody Mono<MultiplyRequestDto> multiplyRequestDtoMono) {
		return this.reactiveMathService.multiply(multiplyRequestDtoMono);
	}
}
