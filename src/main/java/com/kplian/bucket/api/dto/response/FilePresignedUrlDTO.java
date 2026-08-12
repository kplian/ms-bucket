package com.kplian.bucket.api.dto.response;

import java.util.UUID;

public class FilePresignedUrlDTO {
    public UUID id;
    public String presignedUrl;

    public FilePresignedUrlDTO() {}

    public FilePresignedUrlDTO(UUID id, String presignedUrl) {
        this.id = id;
        this.presignedUrl = presignedUrl;
    }
}
