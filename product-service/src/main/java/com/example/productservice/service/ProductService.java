package com.example.productservice.service;

import com.example.productservice.dto.ProductDto;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public Flux<ProductDto> getAll() {
		return this.productRepository
				.findAll()
				.map(EntityDtoUtil::toDto);
	}

	public Mono<ProductDto> getProductById(String id) {
		return this.productRepository
				.findById(id)
				.map(EntityDtoUtil::toDto);
	}

	public Mono<ProductDto> insertProductDto(Mono<ProductDto> productDtoMono) {
		return productDtoMono
				.map(EntityDtoUtil::toEntity)
				.flatMap(this.productRepository::insert)
				.map(EntityDtoUtil::toDto);
		// map: t to u conversion for mono<t>
		// flatmap: when another publisher like mono or flux is returned in response, flatMap would internally subscribe to these publisher so as to trigger them
		// also flatmap returns a Mono<R> in above case, whereas map would have returned Mono<Mono<R>>
	}

	// Updating id of a given product
	public Mono<ProductDto> updateProduct(String id, Mono<ProductDto> productDtoMono) {
		return this.productRepository
				.findById(id) // emits Mono<Product>
				.flatMap(
						product -> productDtoMono
								.map(EntityDtoUtil::toEntity) // this emits a Mono as well, using flatmap only this mono would be subscribed and value would be emitted
								.doOnNext(e -> e.setId(id))) // emits Mono<Product>
				.flatMap(item -> this.productRepository.save(item)) // emits Mono<Product>
				.map(EntityDtoUtil::toDto); // emits Mono<ProductDto>
	}

	// this wont work as something has to be returned as well from reactive pipeline, until we subscribe nothing would work in reactive programming
	/*public void deleteProduct(String id) {
		this.productRepository.deleteById(id);
	}*/

	public Mono<Void> deleteProduct(String id) {
		return this.productRepository.deleteById(id);
	}

	public Flux<ProductDto> getProductsWithinPriceRange(int min, int max) {
		return this.productRepository
				.findByPriceBetween(Range.closed(min, max))
				.map(EntityDtoUtil::toDto);
	}
}
