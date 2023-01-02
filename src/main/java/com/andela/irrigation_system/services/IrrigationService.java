package com.andela.irrigation_system.services;

import com.andela.irrigation_system.config.Permissions;
import com.andela.irrigation_system.model.PlotOfLandConfig;
import com.andela.irrigation_system.model.dto.PaginatedIrrigationConfigs;
import com.andela.irrigation_system.model.dto.PlotOfLandDto;
import com.andela.irrigation_system.model.mapper.IrrigationMapper;
import com.andela.irrigation_system.persistence.IrrigationConfigRepository;
import com.andela.irrigation_system.persistence.entity.IrrigationConfig;
import com.andela.irrigation_system.persistence.entity.IrrigationZone;
import com.andela.irrigation_system.persistence.entity.PlotOfLand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.andela.irrigation_system.model.mapper.IrrigationMapper.toDto;
import static com.andela.irrigation_system.model.mapper.IrrigationMapper.toEntity;
import static com.andela.irrigation_system.persistence.spec.IrrigationConfigSpec.configByNameSpec;

@Slf4j
@Service
@RequiredArgsConstructor
@PreAuthorize("#authentication.principal.name == '" + Permissions.SERVICE_ACCOUNT + "'")
public class IrrigationService {
	private final IrrigationConfigRepository repository;
	private final IrrigationSensorGateway sensorGateway;

	//todo: move to openapi spec
	@Transactional
	public PlotOfLandDto save(PlotOfLandConfig request) {
		return IrrigationMapper.toDto(repository.save(toEntity(request)));
	}

	@Transactional
	@PreAuthorize("hasAnyRole('EDIT_TRACKING')")
	public PlotOfLandDto update(PlotOfLandConfig request) {
		return repository.findById(request.getId())
				.map(e -> {
					e.setMinutes(request.getMinutes());
					e.setWaterAmount(request.getWaterAmount());
					e.setCron(request.getCron());
					e.setPlotOfLand(new PlotOfLand()
							.setName(request.getName())
							.setIrrigationZone(new IrrigationZone().setCoordinates(request.getCoordinates())));
					return IrrigationMapper.toDto(e);
				}).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Configuration entry not found, id: " + request.getId()));
	}

	public PaginatedIrrigationConfigs getPaginatedTasks(Integer page, Integer size, String sort, Long id, String name) {
		Pageable request = Stream.of(id, page, size).allMatch(Objects::isNull)
				? Pageable.unpaged()
				: PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sort));
		Page<IrrigationConfig> result = repository.findAll(configByNameSpec(id, name), request);
		return PaginatedIrrigationConfigs.builder()
				.total(result.getTotalElements())
				.items(result.getContent().stream().map(IrrigationMapper::toDto).collect(Collectors.toList()))
				.build();
	}

	public IrrigationConfig getById(Long logId) {
		return repository.getReferenceById(logId);
	}

	public List<IrrigationConfig> getAll() {
		return repository.findAll();
	}

	public void deleteById(Long logId) {
		repository.deleteById(logId);
	}

	public void irrigate() {
		getAll().forEach(c -> {
			try {
				sensorGateway.irrigate(toDto(c));
			} catch (Exception e) {
				log.error("Alert message in case of SensorGateway error");
//				sendSlackAlert();
			}
//			c.updateStatus(Status.IRRIGATED) //todo
		});
	}
}
