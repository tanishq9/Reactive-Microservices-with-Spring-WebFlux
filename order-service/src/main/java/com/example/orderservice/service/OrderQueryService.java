package com.example.orderservice.service;

import com.example.orderservice.dto.PurchaseOrderResponseDto;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
public class OrderQueryService {

	@Autowired
	private OrderRepository orderRepository;

	public Flux<PurchaseOrderResponseDto> getAllOrders(int userId) {
		// Below stream would be invoked even when NOT subscribed
		Flux.fromIterable(orderRepository.findByUserId(userId));

		// This stream would be lazily invoked i.e. when subscribed
		return Flux.fromStream(() -> orderRepository
				.findByUserId(userId).stream()) // this is a blocking operation, should be executed as part of reactive pipeline which would be executed by boundedElastic thread pool, don't do outside reactive pipeline AND eagerly
				.map(EntityDtoUtil::getPurchaseOrderResponseDto)
				.subscribeOn(Schedulers.boundedElastic());
	}
}
