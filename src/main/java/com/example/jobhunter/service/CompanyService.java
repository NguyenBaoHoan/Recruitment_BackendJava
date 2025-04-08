package com.example.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.example.jobhunter.controller.UserController;
import com.example.jobhunter.domain.Company;
import com.example.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {

    private final UserController userController;
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository, UserController userController){
        this.companyRepository = companyRepository;
        this.userController = userController;
    }

    public List<Company> fetchAllCompany(){
        return companyRepository.findAll();
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
