package com.kplian.bucket.api.mapper;

import com.kplian.bucket.api.dto.request.FileTagRequestDTO;
import com.kplian.bucket.api.dto.response.FileTagResponseDTO;
import com.kplian.bucket.domain.model.FileTag;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class FileTagMapper {

    public FileTag toEntity(FileTagRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        FileTag entity = new FileTag();
        entity.setTagCode(dto.getTagCode());
        return entity;
    }

    public FileTagResponseDTO toResponseDTO(FileTag entity) {
        if (entity == null) {
            return null;
        }

        FileTagResponseDTO dto = new FileTagResponseDTO();
        dto.setId(entity.getId());
        if (entity.getFile() != null) {
            dto.setFileId(entity.getFile().getId());
            dto.setFileName(entity.getFile().getFileName());
        }
        dto.setTagCode(entity.getTagCode());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public List<FileTagResponseDTO> toResponseDTOs(List<FileTag> entities) {
        if (entities == null) return null;
        return entities.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }
}
