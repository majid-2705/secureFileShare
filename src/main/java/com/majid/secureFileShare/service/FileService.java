package com.majid.secureFileShare.service;

import com.majid.secureFileShare.model.FileRecord;
import com.majid.secureFileShare.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class FileService {

    private final FileRepository fileRepository;

    FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public FileRecord saveFile(FileRecord file) {
        return fileRepository.save(file);
    }

    public List<FileRecord> getAllFiles() {
        return fileRepository.findAll();
    }
}
