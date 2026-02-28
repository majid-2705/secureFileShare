package com.majid.secureFileShare.service;

import com.majid.secureFileShare.dataTransferObject.FileRequest;
import com.majid.secureFileShare.model.FileRecord;
import com.majid.secureFileShare.repository.FileRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FileService {

    private final FileRepository fileRepository;

    FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Transactional  /*transactional ensures that the db operations rolls back if
                    something goes wrong*/
    public void uploadFile (FileRequest fileRequest) throws IOException {

        String uploadDir= "C:\\Users\\Dell\\OneDrive\\Bureau\\Project ideas\\uploads\\";
        File directory = new File(uploadDir);
        if(!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = uploadDir + fileRequest.file().getOriginalFilename();
        try {

            fileRequest.file().transferTo(new File(filePath));
            FileRecord fileRecord = new FileRecord();
            fileRecord.setFileName(fileRequest.file().getOriginalFilename());
            fileRecord.setFileType(fileRequest.file().getContentType());;
            fileRecord.setFileSize(fileRequest.file().getSize());
            fileRecord.setFilePath(filePath);
            fileRecord.setUploadedBy(fileRequest.uploadedBy());
            fileRecord.setUploadTime(LocalDateTime.now());
            fileRepository.save(fileRecord);
        } catch (Exception e) {
            new File(filePath).delete();
            throw e;
        }


    }

    public List<FileRecord> getAllFiles() {
        return fileRepository.findAll();
    }
}
