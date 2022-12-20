package com.example.HelloSpringWebFlux.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MultiplyRequestDto {

	int first;
	int second;

	public MultiplyRequestDto(int first, int second) {
		this.first = first;
		this.second = second;
	}
}
