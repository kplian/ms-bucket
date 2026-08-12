package com.kplian.bucket.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class FileVersionRequestDTO {

    @NotNull(message = "{dto.file_version.file_id.required}")
    private UUID fileId;

    @NotNull(message = "{dto.file_version.version.required}")
    private Integer version;

    @NotBlank(message = "{dto.file_version.object_key.required}")
    @Size(max = 255)
    private String objectKey;

    public UUID getFileId() {
        return fileId;
    }

    public void setFileId(UUID fileId) {
        this.fileId = fileId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }
}
