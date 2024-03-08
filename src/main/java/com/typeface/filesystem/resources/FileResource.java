package com.typeface.filesystem.resources;

import com.typeface.filesystem.exception.AppException;
import com.typeface.filesystem.services.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileResource {

    @Autowired
    IFileService fileService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getAllFiles() {
        try {
            return fileService.getAllFiles();
        } catch (AppException e) {
            return ResponseEntity
                    .status(e.getStatus())
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("Oops! Server error during file retrieval.");
        }
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            return fileService.upload(file);
        } catch (AppException e) {
            return ResponseEntity
                    .status(e.getStatus())
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("Oops! Server error during file upload.");
        }
    }

    @GetMapping(value = "/{fileId}")
    public ResponseEntity<?> getFileById(@PathVariable("fileId") String fileId) {
        try {
            return fileService.getFileById(fileId);
        } catch (AppException e) {
            return ResponseEntity
                    .status(e.getStatus())
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("Oops! Server error during file retrieval.");
        }
    }

    @PutMapping(value = "/{fileId}", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<?> updateFile(@PathVariable("fileId") String fileId,
                                        @RequestParam(value = "file", required = false) MultipartFile file,
                                        @RequestParam(value = "name", required = false) String fileName) {
        try {
            return fileService.update(fileId, file, fileName);
        } catch (AppException e) {
            return ResponseEntity
                    .status(e.getStatus())
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("Oops! Server error during file update.");
        }
    }

    @DeleteMapping(value = "/{fileId}", produces = "application/json")
    public ResponseEntity<?> deleteFile(@PathVariable("fileId") String fileId) {
        try {
            return fileService.delete(fileId);
        } catch (AppException e) {
            return ResponseEntity
                    .status(e.getStatus())
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("Oops! Server error during file deletion.");
        }
    }
}
