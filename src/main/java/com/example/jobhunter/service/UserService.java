package com.example.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.jobhunter.domain.Company;
import com.example.jobhunter.domain.User;
import com.example.jobhunter.dto.response.ResCreateUserDTO;
import com.example.jobhunter.dto.response.ResUserDTO;
import com.example.jobhunter.dto.response.ResultPaginationDTO;
import com.example.jobhunter.dto.response.ResultPaginationDTO.Meta;
import com.example.jobhunter.repository.CompanyRepository;
import com.example.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;
    @Autowired
    @Lazy
    private final CompanyRepository companyRepository;

    public UserService(UserRepository userRepository,
            CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;

    }

    public User handleSaveUser(User user) {
        if (user.getCompany() != null) {
            Optional<Company> CompanyOptional = companyRepository.findById(user.getCompany().getId());
            if (CompanyOptional.isPresent()) {
                user.setCompany(CompanyOptional.get());
            }
        }
        return userRepository.save(user);
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    public void handleDeleteUser(long id) {
        userRepository.deleteById(id);
    }

    public User fetchOneUser(long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageUser.getNumber() + 1);
        mt.setPageSize(pageUser.getSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);
        // remove sensitive data. you can use foreach
        List<ResUserDTO> listUser = pageUser.getContent()
                .stream().map(item -> {
                    ResUserDTO.CompanyUser companyUser = null;
                    if (item.getCompany() != null) {
                        companyUser = new ResUserDTO.CompanyUser();
                        companyUser.setId(item.getCompany().getId());
                        companyUser.setName(item.getCompany().getName());
                    }
                    return new ResUserDTO(
                            item.getId(),
                            item.getName(),
                            item.getEmail(),
                            item.getGender(),
                            item.getAddress(),
                            item.getAge(),
                            item.getUpdateAt(),
                            item.getCreatedAt(),
                            companyUser);
                })
                .collect(Collectors.toList());

        rs.setResult(listUser);

        return rs;
    }

    public String getCVPath(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            return userOpt.get().getCvPath();
        }
        return null;
    }

    public void saveCVPath(Long userId, String cvPath) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setCvPath(cvPath);
            userRepository.save(user);
        }
    }

    // update

    public User handleUpdateUser(User reqUser) {
        User currentUser = this.fetchOneUser(reqUser.getId());
        if (currentUser != null) {
            currentUser.setName(reqUser.getName());
            currentUser.setEmail(reqUser.getEmail());
            currentUser.setPassWord(reqUser.getPassWord());
            // check company
            if (reqUser.getCompany() != null) {
                Optional<Company> CompanyOptional = companyRepository.findById(reqUser.getCompany().getId());
                if (CompanyOptional.isPresent()) {
                    currentUser.setCompany(CompanyOptional.get());
                }
            }
            currentUser = userRepository.save(currentUser);
        }
        return currentUser;
    }

    public User handleGetUserByEmail(String username) {
        return this.userRepository.findByEmail(username);
    }

    public boolean isEmailExist(String username) {
        return userRepository.existsByEmail(username);
    }

    // return data without showing password
    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        ResCreateUserDTO.CompanyUser res2 = new ResCreateUserDTO.CompanyUser();

        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        if (user.getCompany() != null) {
            res2.setId(user.getCompany().getId());
            res2.setName(user.getCompany().getName());
            res.setCompany(res2);
        }
        return res;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        ResUserDTO.CompanyUser res2 = new ResUserDTO.CompanyUser();
        if (user.getCompany() != null) {
            res2.setId(user.getCompany().getId());
            res2.setName(user.getCompany().getName());
            res.setCompanyUser(res2);
        }
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());

        return res;
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByEmail(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }
}
