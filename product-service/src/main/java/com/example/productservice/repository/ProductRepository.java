package com.example.productservice.repository;

import com.example.productservice.entity.Product;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

// <Entity class, primary key type>
@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
	// > min and < max (exclusive)
	// Flux<Product> findByPriceBetween(int price1, int price2);

	Flux<Product> findByPriceBetween(Range<Integer> range);
}
