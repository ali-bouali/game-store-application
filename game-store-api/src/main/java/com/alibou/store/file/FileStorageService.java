package com.alibou.store.file;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static java.io.File.separator;
import static java.lang.System.currentTimeMillis;

// Inspired from the book social network repo
@Service
@Slf4j
public class FileStorageService {


    @Value("${application.file.uploads.photos-output-path}")
    private String fileUploadPath;
    // add the folder name parameter
    public String saveFile(
            @Nonnull MultipartFile sourceFile,
            @Nonnull String resourceId,
            @Nonnull String folderName
    ) {
        final String fileUploadSubPath = folderName + separator + resourceId;
        return uploadFile(sourceFile, fileUploadSubPath);
    }

    private String uploadFile(
            @Nonnull MultipartFile sourceFile,
            @Nonnull String fileUploadSubPath
    ) {
        final String finalUploadPath = fileUploadPath + separator + fileUploadSubPath;
        final File targetFolder = new File(finalUploadPath);

        if (!targetFolder.exists()) {
            final boolean folderCreated = targetFolder.mkdirs();
            if (!folderCreated) {
                log.warn("Failed to create the target folder: {}", targetFolder);
                return null;
            }
        }
        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        // Change the currentTimeMillis() with a randomUUID()
        final String targetFilePath = finalUploadPath + separator + UUID.randomUUID().toString() + "." + fileExtension;
        final Path targetPath = Paths.get(targetFilePath);
        try {
            Files.write(targetPath, sourceFile.getBytes());
            log.info("File saved to: {}", targetFilePath);
            return targetFilePath;
        } catch (IOException e) {
            log.error("File was not saved", e);
        }
        return null;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        final int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }
}
