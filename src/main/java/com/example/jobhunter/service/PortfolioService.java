package com.example.jobhunter.service;

import com.example.jobhunter.domain.UserPortfolio;
import com.example.jobhunter.domain.UserPortfolioImage;
import com.example.jobhunter.repository.UserPortfolioImageRepository;
import com.example.jobhunter.repository.UserPortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final UserPortfolioRepository portfolioRepo;
    private final UserPortfolioImageRepository imageRepo;

    @Value("${hoan.upload-file.base-uri}")
    private String baseUri; // ví dụ: file:C:/Users/ADMIN/uploads/ hoặc file:///C:/Users/ADMIN/uploads/

    private Path resolveUploadRoot() {
        String cleaned = baseUri.trim();
        // Chuẩn hoá: loại bỏ prefix file:
        if (cleaned.startsWith("file:")) {
            cleaned = cleaned.substring(5);
        }
        // Trường hợp còn // hoặc /// ở đầu
        cleaned = cleaned.replaceFirst("^/+", "");
        Path p = Paths.get(cleaned).toAbsolutePath().normalize();
        return p;
    }

    public UserPortfolio upsertDescription(Long userId, String desc) {
        UserPortfolio p = portfolioRepo.findByUserId(userId).orElseGet(() -> {
            UserPortfolio np = new UserPortfolio();
            np.setUserId(userId);
            return np;
        });
        p.setDescription(desc);
        return portfolioRepo.save(p);
    }

    public List<UserPortfolioImage> addImages(Long userId, List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) return List.of();
        if (files.size() > 12) {
            throw new IllegalStateException("Tối đa 12 ảnh");
        }

        Path uploadRoot = resolveUploadRoot();                // C:/Users/ADMIN/uploads
        Path portfolioRoot = uploadRoot.resolve("portfolio"); // .../uploads/portfolio
        Path userDir = portfolioRoot.resolve(String.valueOf(userId));
        Files.createDirectories(userDir);

        int startIndex = imageRepo.countByUserId(userId); // cần method countByUserId trong repo (nếu chưa có, thêm)
        List<UserPortfolioImage> saved = new ArrayList<>();
        int i = 0;

        for (MultipartFile mf : files) {
            if (mf.isEmpty()) continue;
            String original = Objects.requireNonNull(mf.getOriginalFilename());
            String ext = "";
            int dot = original.lastIndexOf('.');
            if (dot > 0) ext = original.substring(dot + 1).toLowerCase();
            if (!List.of("jpg","jpeg","png","webp").contains(ext)) {
                throw new IllegalStateException("Định dạng không hỗ trợ: " + ext);
            }
            String storedName = System.currentTimeMillis() + "-" + UUID.randomUUID() + "-" + original;
            Path target = userDir.resolve(storedName);
            Files.copy(mf.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            UserPortfolioImage img = new UserPortfolioImage();
            img.setUserId(userId);
            img.setFileName(original);
            // Lưu relative path để FE build URL: /uploads/{filePath}
            img.setFilePath("portfolio/" + userId + "/" + storedName);
            img.setOrderIndex(startIndex + i);
            saved.add(imageRepo.save(img));
            i++;
        }
        return saved;
    }

    public Map<String, Object> getPortfolio(Long userId) {
        Map<String, Object> res = new HashMap<>();
        UserPortfolio p = portfolioRepo.findByUserId(userId).orElse(null);
        List<UserPortfolioImage> images =
                imageRepo.findByUserIdOrderByOrderIndexAscCreatedAtAsc(userId);
        res.put("description", p != null ? p.getDescription() : "");
        res.put("images", images);
        return res;
    }

    public void deleteImage(Long userId, Long imageId) throws IOException {
        UserPortfolioImage img = imageRepo.findById(imageId)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy ảnh."));
        if (!Objects.equals(img.getUserId(), userId)) {
            throw new SecurityException("Không có quyền xóa ảnh này");
        }
        // Xóa file vật lý
        Path uploadRoot = resolveUploadRoot();
        Path physical = uploadRoot.resolve(img.getFilePath().replace("/", File.separator));
        try {
            Files.deleteIfExists(physical);
        } catch (Exception ignored) {}
        imageRepo.delete(img);
    }
}