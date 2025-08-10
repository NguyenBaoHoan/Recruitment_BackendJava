package com.example.jobhunter.controller;

import com.example.jobhunter.dto.PortfolioImageDTO;
import com.example.jobhunter.domain.UserPortfolioImage;
import com.example.jobhunter.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping("/description")
    public Map<String, Object> upsertDescription(@RequestParam Long userId,
                                                 @RequestParam String description) {
        var p = portfolioService.upsertDescription(userId, description);
        return Map.of("description", p.getDescription());
    }

    @PostMapping("/images")
    public Map<String, Object> uploadImages(@RequestParam Long userId,
                                            @RequestParam("files") List<MultipartFile> files) throws Exception {
        List<UserPortfolioImage> saved = portfolioService.addImages(userId, files);
        var list = saved.stream().map(PortfolioImageDTO::from).collect(Collectors.toList());
        return Map.of("images", list);
    }

    @GetMapping("/{userId}")
    public Map<String, Object> getPortfolio(@PathVariable Long userId) {
        var map = portfolioService.getPortfolio(userId);
        @SuppressWarnings("unchecked")
        List<UserPortfolioImage> imgs = (List<UserPortfolioImage>) map.get("images");
        var dto = imgs.stream().map(PortfolioImageDTO::from).collect(Collectors.toList());
        return Map.of(
                "description", map.get("description"),
                "images", dto
        );
    }

    @DeleteMapping("/images/{imageId}")
    public Map<String, Object> deleteImage(@RequestParam Long userId,
                                           @PathVariable Long imageId) throws Exception {
        portfolioService.deleteImage(userId, imageId);
        return Map.of("deleted", true);
    }
}