package com.example.productservice.service;

import com.example.productservice.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DataSetupService implements CommandLineRunner {
	@Autowired
	private ProductService productService;

	@Override
	public void run(String... args) throws Exception {
		ProductDto p1 = new ProductDto("4k-tv", 1000);
		ProductDto p2 = new ProductDto("slr-camera", 750);
		ProductDto p3 = new ProductDto("iphone", 800);
		ProductDto p4 = new ProductDto("headphone", 100);

		Flux.just(p1, p2, p3, p4)
				//.map(p -> this.productService.insertProductDto(Mono.just(p)))
				.flatMap(p -> this.productService.insertProductDto(Mono.just(p)))
				.subscribe(System.out::println);
	}
}
