package com.kplian.bucket.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class FileTagRequestDTO {

    @NotNull(message = "{dto.file_tag.file_id.required}")
    private UUID fileId;

    @NotBlank(message = "{dto.file_tag.tag_code.required}")
    @Size(max = 50)
    private String tagCode;

    public UUID getFileId() {
        return fileId;
    }

    public void setFileId(UUID fileId) {
        this.fileId = fileId;
    }

    public String getTagCode() {
        return tagCode;
    }

    public void setTagCode(String tagCode) {
        this.tagCode = tagCode;
    }
}
