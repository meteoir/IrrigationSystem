package com.andela.irrigation_system.persistence;

import com.andela.irrigation_system.persistence.entity.IrrigationConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IrrigationConfigRepository extends JpaRepository<IrrigationConfig, Long>, JpaSpecificationExecutor<IrrigationConfig> {
}

