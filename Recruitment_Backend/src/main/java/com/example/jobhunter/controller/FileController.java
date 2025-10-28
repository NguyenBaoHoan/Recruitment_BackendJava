package com.example.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.jobhunter.dto.response.ResUploadFileDTO;
import com.example.jobhunter.service.FileService;
import com.example.jobhunter.util.anotation.ApiMessage;
import com.example.jobhunter.util.error.StorageException;
import com.example.jobhunter.service.UserService;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    @Value("${hoan.upload-file.base-uri}")

    private String baseURI;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/files") // Định nghĩa endpoint POST tại đường dẫn "/files"
    @ApiMessage("Upload single file") // Annotation để mô tả API, có thể dùng cho swagger hoặc custom annotation
    public ResponseEntity<ResUploadFileDTO> upload(
            @RequestParam(name = "file", required = false) MultipartFile file, // Nhận file upload từ request, có thể
                                                                               // null
            @RequestParam("folder") String folder // Nhận tên folder từ request (bắt buộc)
    ) throws URISyntaxException, IOException, StorageException {
        // skip validate
        if (file == null || file.isEmpty()) { // Kiểm tra nếu không có file hoặc file rỗng
            throw new StorageException("File is empty. Please upload a file."); // Ném exception nếu file rỗng
        }

        String fileName = file.getOriginalFilename(); // Lấy tên file gốc từ file upload

        // Danh sách các đuôi file cho phép upload
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");

        // Kiểm tra xem file có đúng định dạng cho phép không
        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));

        if (!isValid) { // Nếu file không hợp lệ (không đúng định dạng)
            throw new StorageException("Invalid file extension. only allows " + allowedExtensions.toString()); // Ném
                                                                                                               // exception
        }

        // Tạo thư mục chứa file nếu chưa tồn tại
        this.fileService.createDirectory(baseURI + folder);

        // Lưu file vào thư mục đã chỉ định
        String uploadFile = this.fileService.store(file, folder);

        // Tạo đối tượng phản hồi kết quả upload (gồm tên file đã upload và thời gian
        // upload)
        ResUploadFileDTO res = new ResUploadFileDTO(uploadFile, Instant.now());

        // Trả về response 200 OK cùng với đối tượng kết quả upload
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/files/upload-cv")
    public ResponseEntity<ResUploadFileDTO> uploadCV(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId
    ) throws URISyntaxException, IOException, StorageException {
        // Validate file
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));
        if (!isValid) {
            throw new StorageException("Invalid file extension. only allows " + allowedExtensions.toString());
        }

        // Lưu file vào thư mục uploads
        String folder = "uploads";
        this.fileService.createDirectory(baseURI + folder);
        String uploadFile = this.fileService.store(file, folder);

        // Lưu đường dẫn vào DB (ví dụ: bảng CV hoặc cập nhật trường cvPath của User)
        userService.saveCVPath(userId, uploadFile); // Uncomment this line and
        // inject userService if you want to save the CV path to the database

        ResUploadFileDTO res = new ResUploadFileDTO(uploadFile, Instant.now());
        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping("/files/delete-cv")
    public ResponseEntity<?> deleteCV(@RequestParam("userId") Long userId) {
        // Lấy đường dẫn CV từ DB
        String cvPath = userService.getCVPath(userId);
        if (cvPath != null) {
            // Xóa file vật lý
            File file = new File(baseURI + cvPath);
            if (file.exists()) file.delete();

            // Xóa đường dẫn trong DB
            userService.saveCVPath(userId, null);

            return ResponseEntity.ok("Đã xóa CV thành công!");
        }
        return ResponseEntity.badRequest().body("Không tìm thấy CV để xóa!");
    }

    @GetMapping("/files/user/{userId}/cv")
    public ResponseEntity<Map<String, String>> getCurrentUserCV(@PathVariable("userId") Long userId) {
        try {
            String cvPath = userService.getCVPath(userId);
            Map<String, String> response = new HashMap<>();
            
            if (cvPath != null && !cvPath.isEmpty()) {
                response.put("cvPath", cvPath);
                response.put("fileName", cvPath.substring(cvPath.lastIndexOf("/") + 1));
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "No CV found");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public void createUploadFolder(String folder) throws URISyntaxException {
        URI uri = new URI(folder);
        Path path = Paths.get(uri);
        File tmpDir = new File(path.toString());
        if (!tmpDir.isDirectory()) {
            try {
                Files.createDirectory(tmpDir.toPath());
                System.out.println(">>> CREATE NEW DIRECTORY SUCCESSFUL, PATH = " + folder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(">>> SKIP MAKING DIRECTORY, ALREADY EXISTS");
        }

    }

}
