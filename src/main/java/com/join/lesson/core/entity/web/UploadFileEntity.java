package com.join.lesson.core.entity.web;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "upload_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UploadFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upload_file_id")
    private Long id;

    @Column
    private String fileName;

    @Column
    private String saveFileName;

    @Column
    private String filePath;

    @Column
    private String contentType;

    @Column
    private long size;

    @Builder
    public UploadFileEntity(String fileName, String saveFileName, String filePath, String contentType, long size) {
        this.fileName = fileName;
        this.saveFileName = saveFileName;
        this.filePath = filePath;
        this.contentType = contentType;
        this.size = size;
    }
}
