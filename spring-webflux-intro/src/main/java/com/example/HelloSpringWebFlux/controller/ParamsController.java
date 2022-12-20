package com.example.HelloSpringWebFlux.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("jobs")
public class ParamsController {

	@GetMapping("search")
	public Flux<Integer> searchJobs(@RequestHeader Map<String, String> headers,
	                                @RequestParam("count") int count,
	                                @RequestParam("page") int page) {
		System.out.println(headers);
		return Flux.just(count, page);
	}
}
