package com.ddng.utilsapi.modules.file.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileMetaData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String fileUrl;

    @Column(unique = true)
    private String filePath;

    public FileMetaData(String fileUrl, String filePath) {
        this.fileUrl = fileUrl;
        this.filePath = filePath;
    }
}
