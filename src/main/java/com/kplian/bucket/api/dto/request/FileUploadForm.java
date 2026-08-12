package com.kplian.bucket.api.dto.request;

import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import jakarta.ws.rs.core.MediaType;

import java.util.UUID;

public class FileUploadForm {

    @RestForm("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public FileUpload file;

    @RestForm
    @PartType(MediaType.TEXT_PLAIN)
    public String moduleCode;

    @RestForm
    @PartType(MediaType.TEXT_PLAIN)
    public String entityName;

    @RestForm
    @PartType(MediaType.TEXT_PLAIN)
    public UUID entityId;

    @RestForm
    @PartType(MediaType.TEXT_PLAIN)
    public String bucketName;
    
    @RestForm
    @PartType(MediaType.TEXT_PLAIN)
    public String securityLevelCode;

}
