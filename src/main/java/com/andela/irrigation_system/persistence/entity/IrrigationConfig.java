package com.andela.irrigation_system.persistence.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "irrigation_config")
@Data
@BatchSize(size = 10)
@EntityListeners(AuditingEntityListener.class)
public class IrrigationConfig {

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

	@ManyToOne(optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "plot_of_land_id")
	private PlotOfLand plotOfLand;

	@Column(nullable = false)
	@Basic(fetch = FetchType.LAZY)
	private String cron;

	@Column(nullable = false)
	@Basic(fetch = FetchType.LAZY)
	private BigDecimal waterAmount;

	@Column(nullable = false)
	@Basic(fetch = FetchType.LAZY)
	private Integer minutes;
}
