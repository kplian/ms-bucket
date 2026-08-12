package com.kplian.bucket.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class FileRequestDTO {

    @NotBlank(message = "{dto.file.module_code.required}")
    @Size(max = 50)
    private String moduleCode;

    @NotBlank(message = "{dto.file.entity_name.required}")
    @Size(max = 100)
    private String entityName;

    @NotNull(message = "{dto.file.entity_id.required}")
    private UUID entityId;

    @NotBlank(message = "{dto.file.file_name.required}")
    @Size(max = 255)
    private String fileName;

    @NotBlank(message = "{dto.file.original_name.required}")
    @Size(max = 255)
    private String originalName;

    @NotBlank(message = "{dto.file.content_type.required}")
    @Size(max = 100)
    private String contentType;

    @NotNull(message = "{dto.file.file_size.required}")
    private Long fileSize;

    @NotBlank(message = "{dto.file.bucket_name.required}")
    @Size(max = 100)
    private String bucketName;

    @NotBlank(message = "{dto.file.object_key.required}")
    @Size(max = 255)
    private String objectKey;

    @NotNull(message = "{dto.file.version.required}")
    private Integer version;

    @NotBlank(message = "{dto.file.security_level_code.required}")
    @Size(max = 50)
    private String securityLevelCode;

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getSecurityLevelCode() {
        return securityLevelCode;
    }

    public void setSecurityLevelCode(String securityLevelCode) {
        this.securityLevelCode = securityLevelCode;
    }
}
