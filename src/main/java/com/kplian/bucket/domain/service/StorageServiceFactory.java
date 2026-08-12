package com.kplian.bucket.domain.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public class StorageServiceFactory {

    private static final Logger LOG = Logger.getLogger(StorageServiceFactory.class);

    @ConfigProperty(name = "storage.type", defaultValue = "aws")
    String storageType;

    @Inject
    AwsS3StorageService awsService;

    @Inject
    CloudflareR2StorageService r2Service;

    @Inject
    MinioStorageService minioService;

    @Produces
    @ApplicationScoped
    public FileStorageService produceStorageService() {
        LOG.debugf("Selecting storage service implementation for type: %s", storageType);
        
        return switch (storageType.toLowerCase()) {
            case "aws" -> awsService;
            case "r2" -> r2Service;
            case "minio" -> minioService;
            default -> {
                LOG.warnf("Unknown storage type: %s. Falling back to AWS.", storageType);
                yield awsService;
            }
        };
    }
}
