package com.typeface.filesystem.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileService {
    ResponseEntity<?> upload(MultipartFile file) throws IOException;

    ResponseEntity<?> getAllFiles();

    ResponseEntity<?> getFileById(String fileId) throws IOException;

    ResponseEntity<?> update(String fileId, MultipartFile file, String fileName) throws IOException;

    ResponseEntity<?> delete(String fileId) throws IOException;
}
