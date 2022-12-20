package com.example.userservice.util;

import com.example.userservice.dto.TransactionRequestDto;
import com.example.userservice.dto.TransactionResponseDto;
import com.example.userservice.dto.TransactionStatus;
import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.User;
import com.example.userservice.entity.UserTransaction;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.beans.BeanUtils;

public class EntityDtoUtil {

	public static UserDto toDto(User user) {
		UserDto dto = new UserDto();
		BeanUtils.copyProperties(user, dto);
		return dto;
	}

	public static User toEntity(UserDto dto) {
		User user = new User();
		BeanUtils.copyProperties(dto, user);
		return user;
	}

	public static UserTransaction toEntity(TransactionRequestDto dto) {
		UserTransaction userTransaction = new UserTransaction();
		userTransaction.setUserId(dto.getUserId());
		userTransaction.setAmount(dto.getAmount());
		userTransaction.setTransactionDate(LocalDateTime.now());
		return userTransaction;
	}

	public static TransactionResponseDto toDto(TransactionRequestDto dto, TransactionStatus transactionStatus) {
		TransactionResponseDto transactionResponseDto = new TransactionResponseDto();
		transactionResponseDto.setUserId(dto.getUserId());
		transactionResponseDto.setAmount(dto.getAmount());
		transactionResponseDto.setTransactionStatus(transactionStatus);
		return transactionResponseDto;
	}
}
