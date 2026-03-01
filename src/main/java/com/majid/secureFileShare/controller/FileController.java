package com.majid.secureFileShare.controller;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.majid.secureFileShare.dataTransferObject.FileRequest;
import com.majid.secureFileShare.model.FileRecord;
import com.majid.secureFileShare.service.FileService;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;
    FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    //@ModelAttribute combines form data (like uploadedBy) and the MultipartFile into single object
    public String uploadFile(@RequestParam ("file") MultipartFile file) throws IndexOutOfBoundsException, IOException {

        // Get the authentication object for the current request
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        fileService.uploadFile(file, email);
        return "File uploaded successfully " + file.getOriginalFilename();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws IOException {
        Resource resource = fileService.downloadFile(id);
        FileRecord fileRecord = fileService.findById(id).get();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileRecord.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

}
