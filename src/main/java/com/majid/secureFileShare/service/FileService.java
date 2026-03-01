package com.majid.secureFileShare.service;

import com.majid.secureFileShare.dataTransferObject.FileRequest;
import com.majid.secureFileShare.model.FileRecord;
import com.majid.secureFileShare.model.User;
import com.majid.secureFileShare.repository.FileRepository;
import com.majid.secureFileShare.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.Nullable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    FileService(FileRepository fileRepository,
                UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    @Transactional  /*transactional ensures that the db operations rolls back if
                    something goes wrong*/
    public void uploadFile (MultipartFile file, String email) throws IOException {

        String uploadDir= "C:\\Users\\Dell\\OneDrive\\Bureau\\Project ideas\\uploads\\";
        File directory = new File(uploadDir);
        if(!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = uploadDir + file.getOriginalFilename();
        try {

            File f = new File(directory, file.getOriginalFilename());
            if(f.exists()) {
                throw new RuntimeException("File in this name already exists");
            }
            //transferTo: takes the file bytes from the ram and write them to file system at the specified path
            file.transferTo(new File(filePath));
            FileRecord fileRecord = new FileRecord();
            fileRecord.setFileName(file.getOriginalFilename());
            fileRecord.setFileType(file.getContentType());;
            fileRecord.setFileSize(file.getSize());
            fileRecord.setFilePath(filePath);


            User currentUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            fileRecord.setUploadedBy(currentUser);
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

    public Resource downloadFile(Long id ) throws IOException {
      FileRecord fileRecord = fileRepository.findById(id)
              .orElseThrow(() -> new RuntimeException("File not found"));
      //convert the path (String) to a path
      Path path = Paths.get(fileRecord.getFilePath());
      //wrap the file into a resource
      return new UrlResource(path.toUri());
    }

    public Optional<FileRecord> findById(long id) {
        return fileRepository.findById(id);
    }
}
