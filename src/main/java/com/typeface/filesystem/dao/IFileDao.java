package com.typeface.filesystem.dao;

import com.typeface.filesystem.domain.FileMetadata;

import java.util.List;
import java.util.Optional;

public interface IFileDao {
    String save(FileMetadata fileMetadata);

    List<FileMetadata> findAll();

    Optional<FileMetadata> findById(String fileId);

    void update(String fileId, FileMetadata fileMetadata);

    boolean delete(String fileId);
}
