package com.andela.irrigation_system.persistence.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;

@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "plot_of_land")
@Data
@BatchSize(size = 10)
@EntityListeners(AuditingEntityListener.class)
public class PlotOfLand {

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

	@Column(name = "name")
	private String name;

	@Column(nullable = false)
	@OneToMany(mappedBy = "plotOfLand", fetch = FetchType.EAGER, cascade = ALL, orphanRemoval = true)
	private Set<IrrigationConfig> configs = new HashSet<>();

	@Column(name = "geohash")
	private String geohash;

	@Type(JsonBinaryType.class)
	@Column(name = "irrigation_zone", columnDefinition = "jsonb")
	private IrrigationZone irrigationZone;
}
