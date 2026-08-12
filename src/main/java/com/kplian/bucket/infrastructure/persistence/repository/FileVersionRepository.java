package com.kplian.bucket.infrastructure.persistence.repository;

import com.kplian.bucket.domain.model.FileVersion;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class FileVersionRepository implements PanacheRepository<FileVersion> {

    public Optional<FileVersion> findByIdOptional(UUID id) {
        return find("id = ?1 and deletedAt is null", id).firstResultOptional();
    }
}
