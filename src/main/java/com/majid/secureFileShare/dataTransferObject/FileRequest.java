package com.majid.secureFileShare.dataTransferObject;

import com.majid.secureFileShare.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public record FileRequest(MultipartFile file) {
}
