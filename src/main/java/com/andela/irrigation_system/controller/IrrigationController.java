package com.andela.irrigation_system.controller;

import com.andela.irrigation_system.model.PlotOfLandConfig;
import com.andela.irrigation_system.model.dto.PaginatedIrrigationConfigs;
import com.andela.irrigation_system.model.dto.PlotOfLandDto;
import com.andela.irrigation_system.persistence.entity.IrrigationConfig;
import com.andela.irrigation_system.services.IrrigationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.andela.irrigation_system.model.mapper.IrrigationMapper.toDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${api.version.key}/irrigation")
public class IrrigationController {
    private final IrrigationService irrigationService;

    @GetMapping
    public ResponseEntity<PaginatedIrrigationConfigs> getLog(@RequestParam(value = "id", required = false) Long id,
                                                             @RequestParam(value = "page", required = false) Integer page,
                                                             @RequestParam(value = "size", required = false) Integer size,
                                                             @RequestParam(value = "sort", required = false) String sort,
                                                             @RequestParam(value = "name", required = false) String name) {
        return ResponseEntity.ok(irrigationService.getPaginatedTasks(page, size, sort, id, name));
    }

    private PaginatedIrrigationConfigs single(IrrigationConfig config) {
        return PaginatedIrrigationConfigs.builder().total(1L).item(toDto(config)).build();
    }

    @SneakyThrows
    @PostMapping
    public ResponseEntity<PlotOfLandDto> create(@RequestBody PlotOfLandConfig request) {
        return ResponseEntity.ok(irrigationService.save(request));
    }

    @SneakyThrows
    @PutMapping
    public ResponseEntity<PlotOfLandDto> update(@RequestBody PlotOfLandConfig request) {
        return ResponseEntity.ok(irrigationService.update(request));
    }
}
