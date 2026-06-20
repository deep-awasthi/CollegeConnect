package com.collegeconnect.backend.service;

import com.collegeconnect.backend.dto.DocumentResponse;
import com.collegeconnect.backend.model.Document;
import com.collegeconnect.backend.model.User;
import com.collegeconnect.backend.repository.DocumentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private AuthService authService;

    @Value("${app.upload.dir}")
    private String uploadDir;

    private Path fileStorageLocation;

    @PostConstruct
    public void init() {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Transactional
    public DocumentResponse uploadDocument(String title, String description, String department, MultipartFile file) {
        User currentUser = authService.getCurrentUserEntity();

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        // Create a unique filename
        String fileExtension = "";
        int extensionIndex = originalFileName.lastIndexOf(".");
        if (extensionIndex > 0) {
            fileExtension = originalFileName.substring(extensionIndex);
        }
        String storedFileName = UUID.randomUUID().toString() + fileExtension;

        try {
            // Check for invalid path characters
            if (originalFileName.contains("..")) {
                throw new IllegalArgumentException("Filename contains invalid path sequence: " + originalFileName);
            }

            // Copy file to target location
            Path targetLocation = this.fileStorageLocation.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            Document document = Document.builder()
                    .user(currentUser)
                    .title(title)
                    .description(description)
                    .fileName(originalFileName)
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .filePath(targetLocation.toString())
                    .department(department)
                    .build();

            Document saved = documentRepository.save(document);
            return mapToDocumentResponse(saved);

        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + originalFileName + ". Please try again!", ex);
        }
    }

    public List<DocumentResponse> getAllDocuments(String department) {
        List<Document> documents;
        if (department != null && !department.trim().isEmpty()) {
            documents = documentRepository.findByDepartmentOrderByCreatedAtDesc(department);
        } else {
            documents = documentRepository.findAllByOrderByCreatedAtDesc();
        }
        return documents.stream().map(this::mapToDocumentResponse).collect(Collectors.toList());
    }

    public Document getDocumentEntityById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document not found with id: " + id));
    }

    public Resource loadFileAsResource(Long id) {
        Document document = getDocumentEntityById(id);
        try {
            Path filePath = Paths.get(document.getFilePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found at: " + document.getFilePath());
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found matching ID: " + id, ex);
        }
    }

    private DocumentResponse mapToDocumentResponse(Document d) {
        return DocumentResponse.builder()
                .id(d.getId())
                .userId(d.getUser().getId())
                .username(d.getUser().getUsername())
                .title(d.getTitle())
                .description(d.getDescription())
                .fileName(d.getFileName())
                .fileType(d.getFileType())
                .fileSize(d.getFileSize())
                .department(d.getDepartment())
                .createdAt(d.getCreatedAt())
                .build();
    }
}
