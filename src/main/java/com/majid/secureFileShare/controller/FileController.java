package com.majid.secureFileShare.controller;


import com.majid.secureFileShare.dataTransferObject.FileRequest;
import com.majid.secureFileShare.model.FileRecord;
import com.majid.secureFileShare.service.FileService;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;
    FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    //@ModelAttribute combines form data (like uploadedBy) and the MultipartFile into single object
    public String uploadFile(@ModelAttribute FileRequest fileRequest) throws IndexOutOfBoundsException {

        FileRecord fileRecord = new FileRecord();
        fileRecord.setFileName(fileRequest.file().getOriginalFilename());
        fileRecord.setFileType(fileRequest.file().getContentType());;
        fileRecord.setFileSize(fileRequest.file().getSize());
        fileRecord.setFilePath("to do later");
        fileRecord.setUploadedBy(fileRequest.uploadedBy());
        fileRecord.setUploadTime(LocalDateTime.now());
        fileService.saveFile(fileRecord);
        return "File uploaded successfully " + fileRequest.file().getOriginalFilename();
    }
}
