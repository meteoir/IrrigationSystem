package com.andela.irrigation_system.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class UserForm {
	private String name;
	private String email;
	private Role role;
	private UserStatus status;
	private BigDecimal multiplier;

	// other related for future implementation
	private boolean verified;
	private String phoneNumber;
	private Level level;
	private boolean archived;
	private String birthDate;
	private List<String> documents;
}