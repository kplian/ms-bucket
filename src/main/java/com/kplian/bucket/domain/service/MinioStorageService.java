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
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.InputStream;
import java.net.URI;
import java.time.Duration;

@ApplicationScoped
@Named("minio")
@Typed(MinioStorageService.class)
public class MinioStorageService implements FileStorageService {

    @ConfigProperty(name = "storage.minio.access-key")
    String accessKey;

    @ConfigProperty(name = "storage.minio.secret-key")
    String secretKey;

    @ConfigProperty(name = "storage.minio.endpoint")
    String endpoint;

    @ConfigProperty(name = "storage.minio.external-endpoint", defaultValue = "")
    String externalEndpoint;

    private S3Client s3Client;
    private S3Presigner presigner;

    @PostConstruct
    public void init() {
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey));
        URI endpointUri = URI.create(endpoint);
        URI externalEndpointUri = (externalEndpoint != null && !externalEndpoint.trim().isEmpty()) 
                ? URI.create(externalEndpoint) 
                : endpointUri;
                
        Region region = Region.US_EAST_1;
        S3Configuration s3Configuration = S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build();

        this.s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .endpointOverride(endpointUri)
                .serviceConfiguration(s3Configuration)
                .build();

        this.presigner = S3Presigner.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .endpointOverride(externalEndpointUri)
                .serviceConfiguration(s3Configuration)
                .build();
    }

    @Override
    public void uploadFile(String bucket, String key, InputStream inputStream, long contentLength, String contentType) {
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
        } catch (NoSuchBucketException e) {
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                s3Client.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
            } else {
                throw e;
            }
        }

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
