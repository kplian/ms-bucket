package com.kplian.bucket.domain.service;

import java.io.InputStream;

public interface FileStorageService {
    void uploadFile(String bucket, String key, InputStream inputStream, long contentLength, String contentType);
    void deleteFile(String bucket, String key);
    String generatePresignedDownloadUrl(String bucket, String key, int durationInMinutes);
}
