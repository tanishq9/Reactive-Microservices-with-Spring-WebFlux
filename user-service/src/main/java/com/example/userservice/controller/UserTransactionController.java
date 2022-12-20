package com.example.userservice.controller;

import com.example.userservice.dto.TransactionRequestDto;
import com.example.userservice.dto.TransactionResponseDto;
import com.example.userservice.entity.UserTransaction;
import com.example.userservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("user/transaction")
public class UserTransactionController {

	@Autowired
	private TransactionService transactionService;

	@PostMapping
	public Mono<TransactionResponseDto> createTransaction(@RequestBody Mono<TransactionRequestDto> requestDtoMono) {
		return requestDtoMono
				.flatMap(transactionRequestDto -> transactionService.createTransaction(transactionRequestDto));
	}

	@GetMapping
	public Flux<UserTransaction> getAllUserTransactions(@RequestParam int userId) {
		return transactionService.getAllTransactions(userId);
	}
}
