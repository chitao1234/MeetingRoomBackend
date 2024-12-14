package cn.xidian.meetingroom.controller;

import cn.xidian.meetingroom.service.LogService;
import cn.xidian.meetingroom.util.IpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/files")
public class FileController extends BaseController {

    private static final Set<String> ALLOWED_CONTENT_TYPES = new HashSet<>(Arrays.asList(
        "image/jpeg",
        "image/png",
        "image/gif",
        "image/webp"
    ));

    private static final long MAX_FILE_SIZE = DataSize.parse("5MB").toBytes();

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private final LogService logService;
    private final HttpServletRequest request;

    public FileController(LogService logService, HttpServletRequest request) {
        this.logService = logService;
        this.request = request;
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }

    @PostMapping("/upload")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type) {
        try {
            // Validate file type parameter
            if (!isValidTypeParameter(type)) {
                return ResponseEntity.badRequest()
                    .body("Invalid type parameter. Must be either 'avatar' or 'room'");
            }

            // Validate file
            String validationError = validateFile(file);
            if (validationError != null) {
                return ResponseEntity.badRequest().body(validationError);
            }

            // Generate secure filename using content hash
            String secureFilename = generateSecureFilename(file, type);
            
            // Create type-specific subdirectory
            Path targetLocation = Paths.get(uploadDir, type.toLowerCase()).resolve(secureFilename);
            Files.createDirectories(targetLocation.getParent());

            // Safely copy file
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }

            // Verify file content type after upload
            String detectedType = Files.probeContentType(targetLocation);
            if (!ALLOWED_CONTENT_TYPES.contains(detectedType)) {
                Files.delete(targetLocation);
                return ResponseEntity.badRequest()
                    .body("Invalid file content detected after upload");
            }

            // Create audit log
            String details = String.format("Uploaded %s image: %s (size: %d bytes)", 
                type, file.getOriginalFilename(), file.getSize());
            logService.createLog(getCurrentUserId(), "FILE_UPLOAD", details, 
                IpUtil.getIpAddressBytes(request));

            // Return the relative path to the file
            String fileUrl = String.format("/uploads/%s/%s", type.toLowerCase(), secureFilename);
            return ResponseEntity.ok().body(new FileUploadResponse(fileUrl));

        } catch (IOException ex) {
            return ResponseEntity.internalServerError()
                .body("Failed to process file upload: " + ex.getMessage());
        }
    }

    private boolean isValidTypeParameter(String type) {
        return "avatar".equalsIgnoreCase(type) || "room".equalsIgnoreCase(type);
    }

    private String validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            return "File is empty";
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            return "File size exceeds maximum limit of 5MB";
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            return "Invalid file type. Only JPEG, PNG, GIF and WebP images are allowed";
        }

        String filename = file.getOriginalFilename();
        if (filename == null) {
            return null;
        }
        filename = StringUtils.cleanPath(filename);
        if (filename.contains("..")) {
            return "Invalid file path sequence";
        }

        return null;
    }

    private String generateSecureFilename(MultipartFile file, String type) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(file.getBytes());
            String originalExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String hashString = bytesToHex(hash);
            return String.format("%s-%s.%s", type, hashString.substring(0, 32), originalExtension);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate secure filename", e);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

class FileUploadResponse {
    private final String url;
    
    public FileUploadResponse(String url) {
        this.url = url;
    }
    
    public String getUrl() {
        return url;
    }
} 