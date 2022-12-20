package com.example.orderservice.controller;

import com.example.orderservice.dto.PurchaseOrderRequestDto;
import com.example.orderservice.dto.PurchaseOrderResponseDto;
import com.example.orderservice.service.OrderFulfillmentService;
import com.example.orderservice.service.OrderQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("order")
public class PurchaseOrderController {
	@Autowired
	private OrderFulfillmentService orderFulfillmentService;

	@Autowired
	private OrderQueryService orderQueryService;

	@PostMapping
	public Mono<ResponseEntity<PurchaseOrderResponseDto>> order(@RequestBody Mono<PurchaseOrderRequestDto> purchaseOrderRequestDtoMono) {
		return this.orderFulfillmentService
				.processOrder(purchaseOrderRequestDtoMono)
				.map(ResponseEntity::ok)
				.onErrorReturn(WebClientResponseException.class, ResponseEntity.badRequest().build())
				.onErrorReturn(WebClientRequestException.class, ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
	}

	@GetMapping("user/{userId}")
	public Flux<PurchaseOrderResponseDto> getOrdersByUserId(@PathVariable int userId) {
		return this.orderQueryService.getAllOrders(userId);
	}
}
