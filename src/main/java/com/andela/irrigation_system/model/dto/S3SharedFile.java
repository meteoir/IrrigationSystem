package com.andela.irrigation_system.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class S3SharedFile {
    private final String fileName;
    private final String presignedUrl;
}
