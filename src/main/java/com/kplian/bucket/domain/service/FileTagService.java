package com.kplian.bucket.domain.service;

import com.kplian.bucket.api.dto.request.FileTagRequestDTO;
import com.kplian.bucket.api.service.I18nService;
import com.kplian.bucket.domain.exception.I18nBusinessException;
import com.kplian.bucket.domain.model.File;
import com.kplian.bucket.domain.model.FileTag;
import com.kplian.bucket.infrastructure.persistence.repository.FileRepository;
import com.kplian.bucket.infrastructure.persistence.repository.FileTagRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class FileTagService {

    @Inject
    FileTagRepository fileTagRepository;

    @Inject
    FileRepository fileRepository;

    @Inject
    I18nService i18nService;

    public List<FileTag> findAll() {
        return fileTagRepository.find("deletedAt is null").list();
    }

    public FileTag findById(UUID id) {
        return fileTagRepository.findByIdOptional(id)
                .orElseThrow(() -> new I18nBusinessException(
                        i18nService,
                        "error.file_tag.not_found",
                        "FILE_TAG_NOT_FOUND",
                        id
                ));
    }

    public FileTag create(FileTagRequestDTO dto, FileTag fileTag) {
        File file = fileRepository.findByIdOptional(dto.getFileId())
                .orElseThrow(() -> new I18nBusinessException(
                        i18nService,
                        "error.file.not_found",
                        "FILE_NOT_FOUND",
                        dto.getFileId()
                ));
        fileTag.setFile(file);
        fileTag.setAuditForCreate(getCurrentUser());
        fileTagRepository.persist(fileTag);
        return fileTag;
    }

    public FileTag update(UUID id, FileTagRequestDTO dto, FileTag fileTagData) {
        FileTag existing = findById(id);

        File file = fileRepository.findByIdOptional(dto.getFileId())
                .orElseThrow(() -> new I18nBusinessException(
                        i18nService,
                        "error.file.not_found",
                        "FILE_NOT_FOUND",
                        dto.getFileId()
                ));

        existing.setFile(file);
        existing.setTagCode(fileTagData.getTagCode());

        existing.setAuditForUpdate(getCurrentUser());

        return existing;
    }

    public void delete(UUID id) {
        FileTag fileTag = findById(id);
        fileTag.setAuditForDelete(getCurrentUser());
    }

    private String getCurrentUser() {
        return "system";
    }
}
