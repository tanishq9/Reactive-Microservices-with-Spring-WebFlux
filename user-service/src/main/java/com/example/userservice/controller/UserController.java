package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("all")
	public Flux<UserDto> all() {
		return this.userService.getAllUsers();
	}

	@GetMapping("{id}")
	public Mono<ResponseEntity<UserDto>> getUserById(@PathVariable int id) {
		return this.userService.getUserById(id)
				.map(ResponseEntity::ok)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping
	public Mono<UserDto> insertUser(@RequestBody Mono<UserDto> userDtoMono) {
		return this.userService.insertUser(userDtoMono);
	}

	@PutMapping("{id}")
	public Mono<ResponseEntity<UserDto>> updateUserById(@PathVariable int id,
	                                                    @RequestBody Mono<UserDto> userDtoMono) {
		return this.userService.updateUser(id, userDtoMono)
				.map(ResponseEntity::ok)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@DeleteMapping("{id}")
	public Mono<Void> deleteUser(@PathVariable int id) {
		return this.userService.deleteUser(id);
	}
}
