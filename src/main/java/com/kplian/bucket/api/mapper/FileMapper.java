package com.kplian.bucket.api.mapper;

import com.kplian.bucket.api.dto.request.FileRequestDTO;
import com.kplian.bucket.api.dto.response.FileResponseDTO;
import com.kplian.bucket.domain.model.File;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class FileMapper {

    public File toEntity(FileRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        File entity = new File();
        entity.setModuleCode(dto.getModuleCode());
        entity.setEntityName(dto.getEntityName());
        entity.setEntityId(dto.getEntityId());
        entity.setFileName(dto.getFileName());
        entity.setOriginalName(dto.getOriginalName());
        entity.setContentType(dto.getContentType());
        entity.setFileSize(dto.getFileSize());
        entity.setBucketName(dto.getBucketName());
        entity.setObjectKey(dto.getObjectKey());
        entity.setVersion(dto.getVersion());
        entity.setSecurityLevelCode(dto.getSecurityLevelCode());
        return entity;
    }

    public FileResponseDTO toResponseDTO(File entity) {
        if (entity == null) {
            return null;
        }

        FileResponseDTO dto = new FileResponseDTO();
        dto.setId(entity.getId());
        dto.setModuleCode(entity.getModuleCode());
        dto.setEntityName(entity.getEntityName());
        dto.setEntityId(entity.getEntityId());
        dto.setFileName(entity.getFileName());
        dto.setOriginalName(entity.getOriginalName());
        dto.setContentType(entity.getContentType());
        dto.setFileSize(entity.getFileSize());
        dto.setBucketName(entity.getBucketName());
        dto.setObjectKey(entity.getObjectKey());
        dto.setVersion(entity.getVersion());
        dto.setSecurityLevelCode(entity.getSecurityLevelCode());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public List<FileResponseDTO> toResponseDTOs(List<File> entities) {
        if (entities == null) return null;
        return entities.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }
}
