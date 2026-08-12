package com.kplian.bucket.api.resource;

import com.kplian.bucket.api.dto.request.FileTagRequestDTO;
import com.kplian.bucket.api.dto.response.FileTagResponseDTO;
import com.kplian.bucket.api.mapper.FileTagMapper;
import com.kplian.bucket.domain.model.FileTag;
import com.kplian.bucket.domain.service.FileTagService;
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

@Path("/file-tags")
@ApplicationScoped
@Tag(name = "FileTags", description = "File tag management API")
public class FileTagResource {

    @Inject
    FileTagService fileTagService;

    @Inject
    FileTagMapper fileTagMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all file tags")
    public Response getAll() {
        List<FileTag> fileTags = fileTagService.findAll();
        List<FileTagResponseDTO> dtos = fileTagMapper.toResponseDTOs(fileTags);
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get file tag by ID")
    public Response getById(
            @Parameter(description = "FileTag ID", required = true) @PathParam("id") UUID id) {
        FileTag fileTag = fileTagService.findById(id);
        FileTagResponseDTO dto = fileTagMapper.toResponseDTO(fileTag);
        return Response.ok(dto).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new file tag")
    public Response create(@Valid FileTagRequestDTO requestDTO) {
        FileTag fileTag = fileTagMapper.toEntity(requestDTO);
        FileTag created = fileTagService.create(requestDTO, fileTag);
        FileTagResponseDTO responseDTO = fileTagMapper.toResponseDTO(created);
        return Response.status(Response.Status.CREATED).entity(responseDTO).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update a file tag")
    public Response update(
            @Parameter(description = "FileTag ID", required = true) @PathParam("id") UUID id,
            @Valid FileTagRequestDTO requestDTO) {
        FileTag fileTag = fileTagMapper.toEntity(requestDTO);
        FileTag updated = fileTagService.update(id, requestDTO, fileTag);
        FileTagResponseDTO responseDTO = fileTagMapper.toResponseDTO(updated);
        return Response.ok(responseDTO).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a file tag")
    public Response delete(
            @Parameter(description = "FileTag ID", required = true) @PathParam("id") UUID id) {
        fileTagService.delete(id);
        return Response.noContent().build();
    }
}
