package com.example.productservice.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data // getters and setters
@ToString // for printing purpose
public class Product {
	@Id
	private String id;
	private String description;
	private Integer price;
}
