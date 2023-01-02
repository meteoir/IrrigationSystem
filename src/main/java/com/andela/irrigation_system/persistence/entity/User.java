package com.andela.irrigation_system.persistence.entity;

import com.andela.irrigation_system.model.Role;
import com.andela.irrigation_system.model.UserStatus;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Getter
@Setter
@BatchSize(size = 10)
@EntityListeners(AuditingEntityListener.class)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@CreatedDate
	@Column(name = "created")
	@Basic(fetch = FetchType.LAZY)
	private Instant createdAt;

	@LastModifiedDate
	@Column(name = "updated")
	@Basic(fetch = FetchType.LAZY)
	private Instant lastUpdatedAt;

	@Column(nullable = false)
	@Basic(fetch = FetchType.LAZY)
	private String name;

	@Column(nullable = false)
	@Basic(fetch = FetchType.LAZY)
	private String email;

	@Column(nullable = false)
	@Basic(fetch = FetchType.LAZY)
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(nullable = false)
	@Basic(fetch = FetchType.LAZY)
	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@Column(nullable = false)
	@Basic(fetch = FetchType.LAZY)
	private BigDecimal multiplier;
}
