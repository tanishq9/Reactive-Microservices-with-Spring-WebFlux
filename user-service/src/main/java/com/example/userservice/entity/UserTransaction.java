package com.example.userservice.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@ToString
@NoArgsConstructor
// table name would be UserTransaction
public class UserTransaction {

	@Id // to indicate primary key
	private Integer id;
	private Integer userId;
	private Integer amount;
	private LocalDateTime transactionDate;

	public UserTransaction(int userId, int amount) {
		this.userId = userId;
		this.amount = amount;
		this.transactionDate = LocalDateTime.now();
	}
}
