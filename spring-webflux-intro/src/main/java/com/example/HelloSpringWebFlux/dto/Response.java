package com.example.HelloSpringWebFlux.dto;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class Response {
	int output;
	Date date = new Date();

	public Response(int output) {
		this.output = output;
	}
}
