package com.kplian.bucket.domain.service;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.InputStream;
import java.net.URI;
import java.time.Duration;

@ApplicationScoped
@Named("r2")
@Typed(CloudflareR2StorageService.class)
public class CloudflareR2StorageService implements FileStorageService {

    @ConfigProperty(name = "storage.r2.access-key")
    String accessKey;

    @ConfigProperty(name = "storage.r2.secret-key")
    String secretKey;

    @ConfigProperty(name = "storage.r2.endpoint")
    String endpoint;

    private S3Client s3Client;
    private S3Presigner presigner;

    @PostConstruct
    public void init() {
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey));
        URI endpointUri = URI.create(endpoint);
        Region region = Region.of("auto");

        this.s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .endpointOverride(endpointUri)
                .build();

        this.presigner = S3Presigner.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .endpointOverride(endpointUri)
                .build();
    }

    @Override
    public void uploadFile(String bucket, String key, InputStream inputStream, long contentLength, String contentType) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, contentLength));
    }

    @Override
    public void deleteFile(String bucket, String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

    @Override
    public String generatePresignedDownloadUrl(String bucket, String key, int durationInMinutes) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(durationInMinutes))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);
        return presignedGetObjectRequest.url().toString();
    }
}
