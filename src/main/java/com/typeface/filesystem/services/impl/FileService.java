package com.typeface.filesystem.services.impl;

import com.typeface.filesystem.dao.IFileDao;
import com.typeface.filesystem.domain.FileMetadata;
import com.typeface.filesystem.exception.AppException;
import com.typeface.filesystem.services.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

@Service
public class FileService implements IFileService {

    String folderPath = "/Users/atif.md/Desktop/File_Storage/";

    @Autowired
    IFileDao fileDao;

    @Override
    public ResponseEntity<?> upload(MultipartFile inputFile) throws IOException {
        if (inputFile.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        String filePath = folderPath + inputFile.getOriginalFilename();
        FileMetadata file = FileMetadata.builder()
                .name(inputFile.getOriginalFilename())
                .type(inputFile.getContentType())
                .size(inputFile.getSize())
                .path(filePath)
                .createdAt(System.currentTimeMillis())
                .build();


        inputFile.transferTo(new File(filePath));

        String fileId = fileDao.save(file);
        return ResponseEntity.ok("File uploaded with id: " + fileId);
    }

    @Override
    public ResponseEntity<?> getAllFiles() {
        List<FileMetadata> savedFiles = fileDao.findAll();
        if (savedFiles.isEmpty()) {
            return ResponseEntity.ok("No files found");
        }

        return ResponseEntity.ok(savedFiles);
    }

    @Override
    public ResponseEntity<?> getFileById(String fileId) throws IOException {
        FileMetadata fileMetadata = fileDao.findById(fileId).orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "File not found with id: " + fileId));
        byte[] fileBytes = Files.readAllBytes(new File(fileMetadata.getPath()).toPath());
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(fileMetadata.getType()))
                .body(fileBytes);
    }

    @Override
    public ResponseEntity<?> update(String fileId, MultipartFile inputFile, String fileName) throws IOException {
        FileMetadata fileMetadata = fileDao.findById(fileId).orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "File not found with id: " + fileId));

        if ((Objects.isNull(inputFile) || inputFile.isEmpty()) && Objects.isNull(fileName)) {
            return ResponseEntity.badRequest().body("No file or name provided for update");
        }

        String newFilePath;
        if (Objects.nonNull(fileName)) {
            fileMetadata = fileMetadata.toBuilder()
                    .name(fileName)
                    .updatedAt(System.currentTimeMillis())
                    .build();
        }

        if (Objects.nonNull(inputFile) && !inputFile.isEmpty()) {
            newFilePath = folderPath + (Objects.nonNull(fileName) ? fileName : inputFile.getOriginalFilename());
            fileMetadata = fileMetadata.toBuilder()
                    .name(inputFile.getOriginalFilename())
                    .type(inputFile.getContentType())
                    .size(inputFile.getSize())
                    .path(newFilePath)
                    .updatedAt(System.currentTimeMillis())
                    .build();

            fileDao.delete(fileId);
            inputFile.transferTo(new File(newFilePath));
        }
        fileDao.update(fileId, fileMetadata);

        return ResponseEntity.ok("File updated with id: " + fileId);
    }

    @Override
    public ResponseEntity<?> delete(String fileId) throws IOException {
        boolean isDeleted = fileDao.delete(fileId);
        if (!isDeleted) {
            return ResponseEntity.badRequest().body("File not found with id: " + fileId);
        }
        return ResponseEntity.ok("File deleted with id: " + fileId);
    }


}
