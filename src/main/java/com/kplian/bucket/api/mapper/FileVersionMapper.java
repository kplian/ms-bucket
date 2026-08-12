package com.kplian.bucket.api.mapper;

import com.kplian.bucket.api.dto.request.FileVersionRequestDTO;
import com.kplian.bucket.api.dto.response.FileVersionResponseDTO;
import com.kplian.bucket.domain.model.FileVersion;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class FileVersionMapper {

    public FileVersion toEntity(FileVersionRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        FileVersion entity = new FileVersion();
        entity.setVersion(dto.getVersion());
        entity.setObjectKey(dto.getObjectKey());
        return entity;
    }

    public FileVersionResponseDTO toResponseDTO(FileVersion entity) {
        if (entity == null) {
            return null;
        }

        FileVersionResponseDTO dto = new FileVersionResponseDTO();
        dto.setId(entity.getId());
        if (entity.getFile() != null) {
            dto.setFileId(entity.getFile().getId());
            dto.setFileName(entity.getFile().getFileName());
        }
        dto.setVersion(entity.getVersion());
        dto.setObjectKey(entity.getObjectKey());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public List<FileVersionResponseDTO> toResponseDTOs(List<FileVersion> entities) {
        if (entities == null) return null;
        return entities.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }
}
