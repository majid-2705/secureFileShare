package com.majid.secureFileShare.repository;

import com.majid.secureFileShare.model.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileRecord, Long> {


}
