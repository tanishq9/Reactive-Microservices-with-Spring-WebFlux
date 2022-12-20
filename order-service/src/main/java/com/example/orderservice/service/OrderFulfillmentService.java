package com.example.orderservice.service;

import com.example.orderservice.client.ProductClient;
import com.example.orderservice.client.UserClient;
import com.example.orderservice.dto.PurchaseOrderRequestDto;
import com.example.orderservice.dto.PurchaseOrderResponseDto;
import com.example.orderservice.dto.RequestContext;
import com.example.orderservice.dto.TransactionRequestDto;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.util.EntityDtoUtil;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

@Service
public class OrderFulfillmentService {

	@Autowired
	private ProductClient productClient;

	@Autowired
	private UserClient userClient;

	@Autowired
	private OrderRepository orderRepository;

	public Mono<PurchaseOrderResponseDto> processOrder(Mono<PurchaseOrderRequestDto> purchaseOrderRequestDtoMono) {
		return purchaseOrderRequestDtoMono
				.map(purchaseOrderRequestDto -> new RequestContext(purchaseOrderRequestDto)) // emits Mono<RequestContext>
				.flatMap(requestContext -> {
							return productClient
									.getProductById(requestContext.getPurchaseOrderRequestDto().getProductId())
									// update request context and return it
									.doOnNext(productDto -> {
										requestContext.setProductDto(productDto);
										requestContext.setTransactionRequestDto(new TransactionRequestDto());
										requestContext.getTransactionRequestDto().setUserId(requestContext.getPurchaseOrderRequestDto().getUserId());
										requestContext.getTransactionRequestDto().setAmount(requestContext.getProductDto().getPrice());
									})
									.retryWhen(Retry.fixedDelay(2, Duration.ofMillis(100)))
									.thenReturn(requestContext);
						}
				) // emits Mono<RequestContext>
				.flatMap(requestContext -> {
					return userClient
							.performTransaction(requestContext.getTransactionRequestDto())
							// update request context and return it
							.doOnNext(
									transactionResponseDto -> {
										requestContext.setTransactionResponseDto(transactionResponseDto);
									}
							)
							.retryWhen(Retry.fixedDelay(2, Duration.ofMillis(100)))
							.thenReturn(requestContext);
				}) // emits Mono<RequestContext>
				.map(EntityDtoUtil::getPurchaseOrder)
				.map(purchaseOrder -> this.orderRepository.save(purchaseOrder)) // blocking driver
				.map(EntityDtoUtil::getPurchaseOrderResponseDto)
				.subscribeOn(Schedulers.boundedElastic()); // above operations would be executed by boundedElastic thread pool, to be used in case of blocking operations in reactive pipeline, ideal to schedule it using boundedElastic
	}
}
