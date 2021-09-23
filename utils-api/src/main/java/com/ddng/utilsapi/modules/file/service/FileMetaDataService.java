package com.ddng.utilsapi.modules.file.service;

import com.ddng.utilsapi.infra.exception.FileMetaDataNotFoundException;
import com.ddng.utilsapi.modules.file.domain.FileMetaData;
import com.ddng.utilsapi.modules.file.repository.FileMetaDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileMetaDataService {

    private final FileMetaDataRepository fileMetaDataRepository;

    @Transactional
    public FileMetaData saveMetaData(String fileUrl, String filePath) {
        return fileMetaDataRepository.save(new FileMetaData(fileUrl, filePath));
    }

    public FileMetaData getMetaData(String fileUrl) {
        return fileMetaDataRepository.findByFileUrl(fileUrl).orElseThrow(() -> new FileMetaDataNotFoundException());
    }

    @Transactional
    public void deleteMetaData(Long id) {
        fileMetaDataRepository.deleteById(id);
    }
}
