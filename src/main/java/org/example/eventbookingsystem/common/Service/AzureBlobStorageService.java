package org.example.eventbookingsystem.common.Service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class AzureBlobStorageService {    private final BlobContainerClient containerClient;

    public AzureBlobStorageService(
            @Value("${azure.storage.connection-string}") String connectionString,
            @Value("${azure.storage.container-name}") String containerName) {
        log.info("Initializing Azure Blob Storage Service with container: {}", containerName);


        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();
            this.containerClient = blobServiceClient.getBlobContainerClient(containerName);
            

            if (!containerClient.exists()) {
                log.warn("Container '{}' does not exist! Creating container...", containerName);
                containerClient.create();
                log.info("Container '{}' created successfully", containerName);
            }
            log.info("Azure Blob Storage Service initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize Azure Blob Storage Service", e);
            throw new RuntimeException("Failed to initialize Azure Blob Storage", e);
        }
    }

    private String getExtension(String filename) {
        if (filename == null || filename.isEmpty() || !filename.contains(".")) {
            return ".jpg";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
    
    private String getContentType(String extension) {
        return switch (extension.toLowerCase()) {
            case ".jpg", ".jpeg" -> "image/jpeg";
            case ".png" -> "image/png";
            case ".gif" -> "image/gif";
            case ".bmp" -> "image/bmp";
            case ".webp" -> "image/webp";
            default -> "application/octet-stream";
        };
    }

    public String storeImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            log.error("File is empty or null");
            throw new IOException("File is empty or null");
        }
        
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            log.error("Original filename is null");
            throw new IOException("Original filename is null");
        }
        
        log.info("Processing image upload: {}, size: {} bytes", originalFilename, file.getSize());
        
        String extension = getExtension(originalFilename);
        String filename = UUID.randomUUID().toString() + extension;
        String contentType = getContentType(extension);
        
        log.info("Generated blob name: {}, content type: {}", filename, contentType);

        try {

            BlobClient blobClient = containerClient.getBlobClient(filename);
            log.info("Created blob client for: {}", blobClient.getBlobUrl());


            BlobHttpHeaders headers = new BlobHttpHeaders()
                    .setContentType(contentType);
            

            log.info("Uploading file to blob storage...");
            blobClient.upload(file.getInputStream(), file.getSize(), true);
            blobClient.setHttpHeaders(headers);
            

            String blobUrl = blobClient.getBlobUrl();
            if (blobUrl == null || blobUrl.isEmpty()) {
                log.error("Failed to get blob URL after upload");
                throw new IOException("Failed to get blob URL after upload");
            }
            
            log.info("Successfully uploaded image to: {}", blobUrl);
            return blobUrl;
        } catch (Exception e) {
            log.error("Failed to upload image to Azure Blob Storage", e);
            throw new IOException("Failed to upload image to Azure Blob Storage: " + e.getMessage(), e);
        }
    }

}
