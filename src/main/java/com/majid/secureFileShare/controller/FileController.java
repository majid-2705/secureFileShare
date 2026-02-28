package com.majid.secureFileShare.controller;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.majid.secureFileShare.dataTransferObject.FileRequest;
import com.majid.secureFileShare.model.FileRecord;
import com.majid.secureFileShare.service.FileService;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;
    FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    //@ModelAttribute combines form data (like uploadedBy) and the MultipartFile into single object
    public String uploadFile(@ModelAttribute FileRequest fileRequest) throws IndexOutOfBoundsException, IOException {

        fileService.uploadFile(fileRequest);
        return "File uploaded successfully " + fileRequest.file().getOriginalFilename();
    }
}
