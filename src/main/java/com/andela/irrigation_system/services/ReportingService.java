package com.andela.irrigation_system.services;

import com.andela.irrigation_system.config.Permissions;
import com.andela.irrigation_system.model.dto.S3SharedFile;
import com.andela.irrigation_system.persistence.entity.IrrigationConfig;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.batch.item.Chunk;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.ZoneOffset.UTC;

@Slf4j
@Service
@RequiredArgsConstructor
@PreAuthorize("#authentication.principal.name == '" + Permissions.SERVICE_ACCOUNT + "'")
public class ReportingService {
    private final S3Service s3Service;

    public S3SharedFile getReportRef(String fileName) {
        return s3Service.generateDownloadUrl(fileName);
    }

    public void delete(String fileName) {
        s3Service.deleteObject(fileName);
    }

    public <T> void prepareReport(Chunk<? extends IrrigationConfig> logs) {
        Map<Long, ? extends List<? extends IrrigationConfig>> userLogs = logs.getItems().stream()
                .collect(Collectors.groupingBy(c -> c.getPlotOfLand().getId(), Collectors.toList()));
        s3Service.upload("report_" + LocalDateTime.now() + ".csv", prepareCsvReport(userLogs));
    }

    @SneakyThrows
    private static byte[] prepareCsvReport(Map<Long, ? extends List<? extends IrrigationConfig>> logs) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(baos, UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.RFC4180)) {

            csvPrinter.printRecord("Irrigation area", "Amount of water", "Total irrigation count");

            logs.forEach((key, value) -> {
                Map<Instant, List<IrrigationConfig>> logEntries = value.stream()
                        .collect(Collectors.groupingBy(IrrigationConfig::getCreatedAt, Collectors.toList()));

                logEntries.forEach((date, log) -> {
                    printValue(csvPrinter, key);
                    printValue(csvPrinter, date);
                    printValue(csvPrinter, log.stream().map(IrrigationConfig::getMinutes).reduce(0, Integer::sum));
                });
            });

            csvPrinter.flush();
            return baos.toByteArray();
        }
    }

    @SneakyThrows
    private static <T> void printValue(CSVPrinter csvPrinter, T value) {
        csvPrinter.print(value);
    }
}
