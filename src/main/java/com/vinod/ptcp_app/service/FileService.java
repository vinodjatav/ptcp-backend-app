package com.vinod.ptcp_app.service;

import com.vinod.ptcp_app.entity.FileEntity;
import com.vinod.ptcp_app.entity.Student;
import com.vinod.ptcp_app.entity.User;
import com.vinod.ptcp_app.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    public FileEntity storeFile(MultipartFile file, Long uploadedBy, Long studentId) throws IOException {
        // Create upload directory if not exists
        // Local storage directory
        String UPLOAD_DIR = "uploads/";
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Generate unique filename
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);

        // Save file to disk
        Files.write(filePath, file.getBytes());

        // Save file path in DB
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFilePath(filePath.toString());
        fileEntity.setUploadedBy(new User(uploadedBy));
        fileEntity.setStudent(new Student(studentId));

        return fileRepository.save(fileEntity);
    }

    public Optional<FileEntity> getFileById(Long id) {
        return fileRepository.findById(id);
    }

    public List<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }
}
