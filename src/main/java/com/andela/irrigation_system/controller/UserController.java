package com.andela.irrigation_system.controller;

import com.andela.irrigation_system.model.UserForm;
import com.andela.irrigation_system.model.dto.PaginatedUserDto;
import com.andela.irrigation_system.model.dto.UserDto;
import com.andela.irrigation_system.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.andela.irrigation_system.services.UserService.single;
import static java.util.Objects.nonNull;
import static org.mockito.ArgumentMatchers.isNull;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${api.version.key}/user")
public class UserController {

	private final UserService userService;

	@GetMapping("/{id}")
	public ResponseEntity<PaginatedUserDto> getUsers(
			@PathVariable(value = "id", required = false) String id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "filter", required = false) String filter
	) {
		return ResponseEntity.ok(
				nonNull(id)
						? single(userService.getById(id))
						: userService.getPaginatedUsers(page, size, sort, filter));
	}

	@SneakyThrows
	@PostMapping
	public ResponseEntity<UserDto> createUser(@RequestBody UserForm request) {
		return ResponseEntity.ok(userService.create(request));
	}

	@SneakyThrows
	@PutMapping
	public ResponseEntity<UserDto> updateUser(@RequestBody UserDto request) {
		return ResponseEntity.ok(userService.update(request));
	}

	@SneakyThrows
	@DeleteMapping
	public ResponseEntity<String> deleteUser(@RequestBody UserDto request) {
		userService.delete(request.getId());
		return ResponseEntity.ok().build();
	}
}
