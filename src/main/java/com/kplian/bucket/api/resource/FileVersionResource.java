package com.kplian.bucket.api.resource;

import com.kplian.bucket.api.dto.request.FileVersionRequestDTO;
import com.kplian.bucket.api.dto.response.FileVersionResponseDTO;
import com.kplian.bucket.api.mapper.FileVersionMapper;
import com.kplian.bucket.domain.model.FileVersion;
import com.kplian.bucket.domain.service.FileVersionService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
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

import java.util.List;
import java.util.UUID;

@Path("/file-versions")
@ApplicationScoped
@Tag(name = "FileVersions", description = "File version management API")
public class FileVersionResource {

    @Inject
    FileVersionService fileVersionService;

    @Inject
    FileVersionMapper fileVersionMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all file versions")
    public Response getAll() {
        List<FileVersion> fileVersions = fileVersionService.findAll();
        List<FileVersionResponseDTO> dtos = fileVersionMapper.toResponseDTOs(fileVersions);
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get file version by ID")
    public Response getById(
            @Parameter(description = "FileVersion ID", required = true) @PathParam("id") UUID id) {
        FileVersion fileVersion = fileVersionService.findById(id);
        FileVersionResponseDTO dto = fileVersionMapper.toResponseDTO(fileVersion);
        return Response.ok(dto).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new file version")
    public Response create(@Valid FileVersionRequestDTO requestDTO) {
        FileVersion fileVersion = fileVersionMapper.toEntity(requestDTO);
        FileVersion created = fileVersionService.create(requestDTO, fileVersion);
        FileVersionResponseDTO responseDTO = fileVersionMapper.toResponseDTO(created);
        return Response.status(Response.Status.CREATED).entity(responseDTO).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update a file version")
    public Response update(
            @Parameter(description = "FileVersion ID", required = true) @PathParam("id") UUID id,
            @Valid FileVersionRequestDTO requestDTO) {
        FileVersion fileVersion = fileVersionMapper.toEntity(requestDTO);
        FileVersion updated = fileVersionService.update(id, requestDTO, fileVersion);
        FileVersionResponseDTO responseDTO = fileVersionMapper.toResponseDTO(updated);
        return Response.ok(responseDTO).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a file version")
    public Response delete(
            @Parameter(description = "FileVersion ID", required = true) @PathParam("id") UUID id) {
        fileVersionService.delete(id);
        return Response.noContent().build();
    }
}
