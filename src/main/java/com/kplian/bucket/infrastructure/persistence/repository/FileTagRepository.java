package com.kplian.bucket.infrastructure.persistence.repository;

import com.kplian.bucket.domain.model.FileTag;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class FileTagRepository implements PanacheRepository<FileTag> {

    public Optional<FileTag> findByIdOptional(UUID id) {
        return find("id = ?1 and deletedAt is null", id).firstResultOptional();
    }
}
