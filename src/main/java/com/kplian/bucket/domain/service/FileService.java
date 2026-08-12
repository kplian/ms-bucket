package com.kplian.bucket.domain.service;

import com.kplian.bucket.api.dto.request.FileRequestDTO;
import com.kplian.bucket.api.dto.request.FileUploadForm;
import com.kplian.bucket.api.service.I18nService;
import com.kplian.bucket.domain.exception.I18nBusinessException;
import com.kplian.bucket.domain.model.File;
import com.kplian.bucket.infrastructure.persistence.repository.FileRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class FileService {

    @Inject
    FileRepository fileRepository;

    @Inject
    I18nService i18nService;
    
    @Inject
    FileStorageService storageService;

    public List<File> findAll() {
        return fileRepository.find("deletedAt is null").list();
    }

    public File findById(UUID id) {
        return fileRepository.findByIdOptional(id)
                .orElseThrow(() -> new I18nBusinessException(
                        i18nService,
                        "error.file.not_found",
                        "FILE_NOT_FOUND",
                        id
                ));
    }

    public File create(FileRequestDTO dto, File file, String userId) {
        file.setAuditForCreate(userId != null ? userId : "system");
        fileRepository.persist(file);
        return file;
    }
    
    public File uploadFile(FileUploadForm form, String userId) {
        File file = new File();
        file.setModuleCode(form.moduleCode);
        file.setEntityName(form.entityName);
        file.setEntityId(form.entityId);
        file.setOriginalName(form.file.fileName());
        file.setFileName(UUID.randomUUID().toString() + "_" + form.file.fileName());
        file.setContentType(form.file.contentType());
        file.setFileSize(form.file.size());
        file.setBucketName(form.bucketName);
        // Default version
        file.setVersion(1);
        file.setSecurityLevelCode(form.securityLevelCode);
        
        // Generate Object Key
        String objectKey = form.moduleCode.toLowerCase() + "/" + form.entityName.toLowerCase() + "/" + form.entityId + "/" + file.getFileName();
        file.setObjectKey(objectKey);
        
        // Push to Storage
        try (InputStream inputStream = Files.newInputStream(form.file.filePath())) {
            storageService.uploadFile(form.bucketName, objectKey, inputStream, form.file.size(), form.file.contentType());
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to storage", e);
        }
        
        file.setAuditForCreate(userId != null ? userId : "system");
        fileRepository.persist(file);
        return file;
    }

    public File update(UUID id, File fileData, String userId) {
        File existing = findById(id);
        existing.setModuleCode(fileData.getModuleCode());
        existing.setEntityName(fileData.getEntityName());
        existing.setEntityId(fileData.getEntityId());
        existing.setFileName(fileData.getFileName());
        existing.setOriginalName(fileData.getOriginalName());
        existing.setContentType(fileData.getContentType());
        existing.setFileSize(fileData.getFileSize());
        existing.setBucketName(fileData.getBucketName());
        existing.setObjectKey(fileData.getObjectKey());
        existing.setVersion(fileData.getVersion());
        existing.setSecurityLevelCode(fileData.getSecurityLevelCode());

        existing.setAuditForUpdate(userId != null ? userId : "system");
        return existing;
    }

    public void delete(UUID id, String userId) {
        File file = findById(id);
        file.setAuditForDelete(userId != null ? userId : "system");
        
        // Physically delete from Storage
        try {
            storageService.deleteFile(file.getBucketName(), file.getObjectKey());
        } catch (Exception e) {
            // Depending on business logic, we could ignore or fail.
            // Failing if delete fails ensures DB and Storage remain in sync.
            throw new RuntimeException("Failed to delete file from storage", e);
        }
    }

    public String getPresignedUrl(UUID id) {
        File file = findById(id);
        int durationMinutes = "PUBLIC".equals(file.getSecurityLevelCode()) ? 60 : 5;
        return storageService.generatePresignedDownloadUrl(file.getBucketName(), file.getObjectKey(), durationMinutes);
    }

    public List<com.kplian.bucket.api.dto.response.FilePresignedUrlDTO> getBulkPresignedUrls(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        // Fetch all files in a single query
        List<File> files = fileRepository.find("id in ?1 and deletedAt is null", ids).list();
        Map<UUID, File> fileMap = files.stream().collect(Collectors.toMap(File::getId, f -> f));

        return ids.stream()
                .map(id -> {
                    File file = fileMap.get(id);
                    if (file == null) {
                        return new com.kplian.bucket.api.dto.response.FilePresignedUrlDTO(id, null);
                    }
                    try {
                        int durationMinutes = "PUBLIC".equals(file.getSecurityLevelCode()) ? 60 : 5;
                        String url = storageService.generatePresignedDownloadUrl(file.getBucketName(), file.getObjectKey(), durationMinutes);
                        return new com.kplian.bucket.api.dto.response.FilePresignedUrlDTO(id, url);
                    } catch (Exception e) {
                        // Return null or handle error for this specific ID if needed
                        return new com.kplian.bucket.api.dto.response.FilePresignedUrlDTO(id, null);
                    }
                })
                .toList();
    }
}
