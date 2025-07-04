package com.example.jobhunter.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.example.jobhunter.controller.UserController;
import com.example.jobhunter.domain.Company;
import com.example.jobhunter.domain.User;
import com.example.jobhunter.dto.response.ResultPaginationDTO;
import com.example.jobhunter.dto.response.ResultPaginationDTO.Meta;
import com.example.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {

    private final UserController userController;
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository, UserController userController){
        this.companyRepository = companyRepository;
        this.userController = userController;
    }

    public ResultPaginationDTO fetchAllCompany(Specification<Company> spec, Pageable pageable){
        Page<Company> pageCompany = this.companyRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageCompany.getNumber() + 1);
        mt.setPageSize(pageCompany.getSize());

        mt.setPages(pageCompany.getTotalPages());
        mt.setTotal(pageCompany.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageCompany.getContent());
        
        return rs;
    }

    public Company handleSaveCompany(Company company){
        return companyRepository.save(company);
    }
    public Company fetchOneCompany(Long id){
        Optional<Company> c = companyRepository.findById(id);
        if(c.isPresent())
        {
            return c.get();
        }
        return null;
    }
    public Company handleUpdateCompany(Company c){
        Company curCompany = fetchOneCompany(c.getId());
        if(curCompany != null){
            curCompany.setName(c.getName());
            curCompany.setDescription(c.getDescription());
            curCompany.setAddress(c.getAddress());
            curCompany.setLogo(c.getLogo());

            curCompany = companyRepository.save(curCompany);
        }
        return curCompany;
    }
    public void handledeleteCompany(Long id){
        companyRepository.deleteById(id);
    }
}
