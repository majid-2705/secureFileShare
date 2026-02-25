package com.majid.secureFileShare.dataTransferObject;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public record FileRequest(MultipartFile file, String uploadedBy) {
}
