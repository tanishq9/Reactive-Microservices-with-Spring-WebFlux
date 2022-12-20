package com.example.userservice.service;

import com.example.userservice.dto.TransactionRequestDto;
import com.example.userservice.dto.TransactionResponseDto;
import com.example.userservice.dto.TransactionStatus;
import com.example.userservice.entity.UserTransaction;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.repository.UserTransactionRepository;
import com.example.userservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserTransactionRepository userTransactionRepository;

	public Mono<TransactionResponseDto> createTransaction(TransactionRequestDto transactionRequestDto) {
		// find user balance
		return this.userRepository
				/*.findById(transactionRequestDto.getUserId())
				.filter(user -> user.getBalance() >= transactionRequestDto.getAmount())
				// deduct balance
				.doOnNext(user -> user.setBalance(user.getBalance() - transactionRequestDto.getAmount())) // this emits modified Mono<User*/
				.updateUserBalance(transactionRequestDto.getUserId(), transactionRequestDto.getAmount())
				.filter(Boolean::booleanValue) // only if user record is updated then update transaction table
				.map(user -> EntityDtoUtil.toEntity(transactionRequestDto))
				.flatMap(userTransactionRepository::save) // emits Mono<UserTransaction>
				.map(userTransaction -> EntityDtoUtil.toDto(transactionRequestDto, TransactionStatus.APPROVED))
				.defaultIfEmpty(EntityDtoUtil.toDto(transactionRequestDto, TransactionStatus.DECLINED)); // if no signal is emitted from reactive pipeline
	}

	public Flux<UserTransaction> getAllTransactions(int userId) {
		return userTransactionRepository.findByUserIdOrderByTransactionDateDesc(userId);
	}
}