package com.kplian.bucket.api.resource;

import com.kplian.bucket.api.dto.request.BulkPresignedUrlRequest;
import com.kplian.bucket.api.dto.response.FilePresignedUrlDTO;
import com.kplian.bucket.api.dto.request.FileRequestDTO;
import com.kplian.bucket.api.dto.request.FileUploadForm;
import com.kplian.bucket.api.dto.response.FileResponseDTO;
import com.kplian.bucket.api.mapper.FileMapper;
import com.kplian.bucket.domain.model.File;
import com.kplian.bucket.domain.service.FileService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import jakarta.ws.rs.HeaderParam;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/files")
@ApplicationScoped
@Tag(name = "Files", description = "File management API")
public class FileResource {

    @Inject
    FileService fileService;

    @Inject
    FileMapper fileMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all files")
    public Response getAll() {
        List<File> files = fileService.findAll();
        List<FileResponseDTO> dtos = fileMapper.toResponseDTOs(files);
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get file by ID")
    public Response getById(
            @Parameter(description = "File ID", required = true) @PathParam("id") UUID id) {
        File file = fileService.findById(id);
        FileResponseDTO dto = fileMapper.toResponseDTO(file);
        return Response.ok(dto).build();
    }
    
    @GET
    @Path("/{id}/presigned-url")
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(summary = "Get a pre-signed S3 download URL for a file")
    public Response getPresignedUrl(@Parameter(description = "File ID", required = true) @PathParam("id") UUID id) {
        String url = fileService.getPresignedUrl(id);
        return Response.ok(url).build();
    }

    @POST
    @Path("/presigned-urls-batch")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get multiple pre-signed download URLs at once")
    public Response getBulkPresignedUrls(BulkPresignedUrlRequest request) {
        List<FilePresignedUrlDTO> urls = fileService.getBulkPresignedUrls(request.ids);
        return Response.ok(urls).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new file record without object upload")
    public Response create(@Valid FileRequestDTO requestDTO, @HeaderParam("X-User-Id") String userId) {
        File file = fileMapper.toEntity(requestDTO);
        File created = fileService.create(requestDTO, file, userId);
        FileResponseDTO responseDTO = fileMapper.toResponseDTO(created);
        return Response.status(Response.Status.CREATED).entity(responseDTO).build();
    }
    
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Upload a file through Multipart to S3 and save metadata")
    public Response uploadFile(@BeanParam FileUploadForm form, @HeaderParam("X-User-Id") String userId) {
        System.out.println("DEBUG - Received Upload Request from user: " + userId);
        File created = fileService.uploadFile(form, userId);
        FileResponseDTO responseDTO = fileMapper.toResponseDTO(created);
        return Response.status(Response.Status.CREATED).entity(responseDTO).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update a file")
    public Response update(
            @Parameter(description = "File ID", required = true) @PathParam("id") UUID id,
            @Valid FileRequestDTO requestDTO,
            @HeaderParam("X-User-Id") String userId) {
        File file = fileMapper.toEntity(requestDTO);
        File updated = fileService.update(id, file, userId);
        FileResponseDTO responseDTO = fileMapper.toResponseDTO(updated);
        return Response.ok(responseDTO).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a file completely (DB and S3)")
    public Response delete(
            @Parameter(description = "File ID", required = true) @PathParam("id") UUID id,
            @HeaderParam("X-User-Id") String userId) {
        fileService.delete(id, userId);
        return Response.noContent().build();
    }
}
