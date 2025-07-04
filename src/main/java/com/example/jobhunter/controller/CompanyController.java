package com.example.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.jobhunter.domain.Company;
import com.example.jobhunter.domain.User;
import com.example.jobhunter.dto.response.ResultPaginationDTO;
import com.example.jobhunter.repository.CompanyRepository;
import com.example.jobhunter.repository.UserRepository;
import com.example.jobhunter.service.CompanyService;
import com.example.jobhunter.service.UserService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/companys")

public class CompanyController {
    private final CompanyService companyService;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyController(CompanyService companyService, CompanyRepository companyRepository,
            UserRepository userRepository) {
        this.companyService = companyService;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<ResultPaginationDTO> getAllCompany(
            @Filter Specification<Company> spec,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.fetchAllCompany(spec, pageable));
    }

    @PostMapping("/create")
    public ResponseEntity<Company> createNewCompany(@Valid @RequestBody Company com) {
        Company newCompany = companyService.handleSaveCompany(com);
        return ResponseEntity.status(HttpStatus.OK).body(newCompany);
    }

    @PutMapping("/update")
    public ResponseEntity<Company> putMethodName(@Valid @RequestBody Company c) {
        return ResponseEntity.ok(companyService.handleUpdateCompany(c));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCompany(@PathVariable("id") Long id) {
        Optional<Company> company = companyRepository.findById(id);
        if (company.isPresent()) {
            Company com = company.get();
            List<User> users = userRepository.findByCompany(com);
            this.userRepository.deleteAll(users);

        }
        companyService.handledeleteCompany(id);
    }

}
