package com.typeface.filesystem.dao.impl;

import com.typeface.filesystem.dao.IFileDao;
import com.typeface.filesystem.domain.FileMetadata;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Repository
public class FileDao implements IFileDao {

    Map<String, FileMetadata> files = new HashMap<>();

    @Override
    public String save(FileMetadata fileMetadata) {
        String fileId = UUID.randomUUID().toString();
        files.put(fileId, fileMetadata.toBuilder().id(fileId).build());
        return fileId;
    }

    @Override
    public List<FileMetadata> findAll() {
        List<FileMetadata> savedFiles = new ArrayList<>();
        files.forEach((k, v) -> {
            savedFiles.add(v);
        });
        return savedFiles;
    }

    @Override
    public Optional<FileMetadata> findById(String fileId) {
        return Optional.ofNullable(files.get(fileId));
    }

    @Override
    public void update(String fileId, FileMetadata fileMetadata) {
        files.put(fileId, fileMetadata.toBuilder().id(fileId).build());
    }

    @Override
    public boolean delete(String fileId) {
        return findById(fileId).map(file -> {
            files.remove(fileId);
            try {
                Files.delete(Path.of(file.getPath()));
            } catch (IOException e) {
                throw new RuntimeException("Error deleting file");
            }
            return true;
        }).orElse(false);
    }
}
