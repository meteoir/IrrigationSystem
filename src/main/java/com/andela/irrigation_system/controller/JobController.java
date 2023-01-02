package com.andela.irrigation_system.controller;

import com.andela.irrigation_system.job.BatchJobLauncher;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/job")
@RequiredArgsConstructor
public class JobController {
    private final BatchJobLauncher launcher;

    @PostMapping("/{job}")
    public ResponseEntity<String> startJob(@PathVariable("job") String job) {
        BatchStatus batchStatus = launcher.launch(job);
        HttpStatus status = batchStatus.isUnsuccessful() ? EXPECTATION_FAILED : OK;
        return new ResponseEntity<>(batchStatus.name(), status);
    }
}
