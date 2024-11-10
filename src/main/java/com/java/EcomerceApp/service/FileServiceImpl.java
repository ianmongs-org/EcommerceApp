package com.java.EcomerceApp.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService{
    @Value("${project.image}")
    private String imageUploadDir;

    public String uploadImage(String path, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be null or empty");
        }

        // Get the original file name
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("Invalid file name");
        }

        // Generate a unique file name
        String randomUUID = UUID.randomUUID().toString();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = randomUUID.concat(fileExtension);

        // Define the relative path within the project
        String projectDirectory = System.getProperty("user.dir"); // Base directory of the project
        File directory = new File(imageUploadDir);

        // Create the directory if it doesn't exist
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Create the full file path
        String filePath = imageUploadDir + File.separator + fileName;

        // Copy the file to the target location
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        // Return the generated file name
        return fileName;
    }
}
