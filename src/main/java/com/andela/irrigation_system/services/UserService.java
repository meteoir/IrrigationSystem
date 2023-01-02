package com.andela.irrigation_system.services;

import com.andela.irrigation_system.config.Permissions;
import com.andela.irrigation_system.model.UserForm;
import com.andela.irrigation_system.model.dto.PaginatedUserDto;
import com.andela.irrigation_system.model.dto.UserDto;
import com.andela.irrigation_system.model.mapper.UserMapper;
import com.andela.irrigation_system.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.andela.irrigation_system.persistence.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
@PreAuthorize("#authentication.principal.name == '" + Permissions.SERVICE_ACCOUNT + "'")
public class UserService {

	private final UserRepository repository;

	public UserDto create(UserForm request) {
		User user = UserMapper.toEntity(request);
		repository.findOne(Example.of(user)).ifPresent(u -> {
			throw new IllegalStateException();
		});
		return UserMapper.toDto(repository.save(user));
	}

	@Transactional
	public UserDto update(UserDto request) {
		return repository.findById(request.getId())
				.map(e -> {
					e.setStatus(request.getStatus());
					return UserMapper.toDto(e);
				}).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Client application not found, id: " + request.getId()));
	}

	public PaginatedUserDto getPaginatedUsers(Integer page, Integer size, String sort, String filter) {
		Pageable request = Stream.of(page, size).allMatch(Objects::isNull)
				? Pageable.unpaged()
				: PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sort));
		Page<User> result = isNull(filter)
				? repository.findAll(request)
				: repository.findAllByEmailLike(request, "%" + filter + "%");
		return PaginatedUserDto.builder()
				.total(result.getTotalElements())
				.items(result.getContent().stream().map(UserMapper::toDto).collect(Collectors.toList()))
				.build();
	}

	public static PaginatedUserDto single(UserDto user) {
		return PaginatedUserDto.builder().total(1L).item(user).build();
	}

	public UserDto getById(String id) {
		return UserMapper.toDto(repository.getOne(Long.valueOf(id)));
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public Optional<User> findByEmail(String email) {
		return repository.findByEmail(email);
	}

	public boolean existsByEmail(String email) {
		return repository.existsByEmail(email);
	}
}
