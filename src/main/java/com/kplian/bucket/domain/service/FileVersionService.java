package com.kplian.bucket.domain.service;

import com.kplian.bucket.api.dto.request.FileVersionRequestDTO;
import com.kplian.bucket.api.service.I18nService;
import com.kplian.bucket.domain.exception.I18nBusinessException;
import com.kplian.bucket.domain.model.File;
import com.kplian.bucket.domain.model.FileVersion;
import com.kplian.bucket.infrastructure.persistence.repository.FileRepository;
import com.kplian.bucket.infrastructure.persistence.repository.FileVersionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class FileVersionService {

    @Inject
    FileVersionRepository fileVersionRepository;

    @Inject
    FileRepository fileRepository;

    @Inject
    I18nService i18nService;

    public List<FileVersion> findAll() {
        return fileVersionRepository.find("deletedAt is null").list();
    }

    public FileVersion findById(UUID id) {
        return fileVersionRepository.findByIdOptional(id)
                .orElseThrow(() -> new I18nBusinessException(
                        i18nService,
                        "error.file_version.not_found",
                        "FILE_VERSION_NOT_FOUND",
                        id
                ));
    }

    public FileVersion create(FileVersionRequestDTO dto, FileVersion fileVersion) {
        File file = fileRepository.findByIdOptional(dto.getFileId())
                .orElseThrow(() -> new I18nBusinessException(
                        i18nService,
                        "error.file.not_found",
                        "FILE_NOT_FOUND",
                        dto.getFileId()
                ));
        fileVersion.setFile(file);
        fileVersion.setAuditForCreate(getCurrentUser());
        fileVersionRepository.persist(fileVersion);
        return fileVersion;
    }

    public FileVersion update(UUID id, FileVersionRequestDTO dto, FileVersion fileVersionData) {
        FileVersion existing = findById(id);

        File file = fileRepository.findByIdOptional(dto.getFileId())
                .orElseThrow(() -> new I18nBusinessException(
                        i18nService,
                        "error.file.not_found",
                        "FILE_NOT_FOUND",
                        dto.getFileId()
                ));

        existing.setFile(file);
        existing.setVersion(fileVersionData.getVersion());
        existing.setObjectKey(fileVersionData.getObjectKey());

        existing.setAuditForUpdate(getCurrentUser());

        return existing;
    }

    public void delete(UUID id) {
        FileVersion fileVersion = findById(id);
        fileVersion.setAuditForDelete(getCurrentUser());
    }

    private String getCurrentUser() {
        return "system";
    }
}
