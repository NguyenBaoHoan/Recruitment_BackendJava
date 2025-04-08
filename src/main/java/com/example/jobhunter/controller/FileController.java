package com.example.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.jobhunter.service.FileService;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class FileController {
    private final FileService fileService;

    @Value("${hoan.upload-file.base-uri}")

    private String baseURI;

    public FileController(FileService fileService) {
        this.fileService = fileService;

    }

    @PostMapping("/files")
    public String upload(@RequestParam("file") MultipartFile file,
            @RequestParam("folder") String folder) throws URISyntaxException, IOException {
        // skip validate
        // create a directory if not exist
        this.fileService.createDirectory(baseURI + folder);
        // store file

        fileService.store(file, folder);
        return file.getOriginalFilename() + " " + folder;
    }

}
