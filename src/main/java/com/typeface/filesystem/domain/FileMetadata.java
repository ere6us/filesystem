package com.typeface.filesystem.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileMetadata {
    private String id;
    private String name;
    private String type;
    private long size;
    private String path;
    private long createdAt;
    private Long updatedAt;
}
