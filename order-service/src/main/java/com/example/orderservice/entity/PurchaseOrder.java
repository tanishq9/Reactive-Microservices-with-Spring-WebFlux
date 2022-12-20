package com.example.orderservice.entity;

import com.example.orderservice.dto.OrderStatus;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Entity // Spring data jpa creates a table automatically using this Entity annotation unlike in case of spring reactive db libraries
public class PurchaseOrder {

	@Id // used to denote primary key in spring jpa for table
	@GeneratedValue
	private Integer orderId;
	private String productId;
	private Integer userId;
	private Integer amount;
	private OrderStatus orderStatus;
}
