package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDto {

	private Integer userId;
	private Integer amount;
	private TransactionStatus transactionStatus;
}
