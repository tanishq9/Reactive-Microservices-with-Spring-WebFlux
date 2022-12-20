package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	public Flux<UserDto> getAllUsers() {
		return this.userRepository
				.findAll()
				.map(EntityDtoUtil::toDto);
	}

	public Mono<UserDto> getUserById(int id) {
		return this.userRepository
				.findById(id)
				.map(EntityDtoUtil::toDto);
	}

	public Mono<UserDto> insertUser(Mono<UserDto> userDtoMono) {
		return userDtoMono.map(EntityDtoUtil::toEntity)
				.flatMap(userRepository::save)
				.map(EntityDtoUtil::toDto);
	}

	public Mono<UserDto> updateUser(int id, Mono<UserDto> userDtoMono) {
		return this.userRepository
				.findById(id) // emits Mono<User>
				// update user found
				.flatMap(user -> userDtoMono.map(EntityDtoUtil::toEntity)) // flatMap would subscribe to this mono returned and emit Mono<User>
				.flatMap(userRepository::save) // emits Mono<User>
				.map(EntityDtoUtil::toDto);
	}

	public Mono<Void> deleteUser(int id) {
		return this.userRepository
				.deleteById(id);
	}
}
