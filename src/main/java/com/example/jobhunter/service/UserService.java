// File: service/UserService.java
package com.example.jobhunter.service;

import com.example.jobhunter.domain.Company;
import com.example.jobhunter.domain.User;
import com.example.jobhunter.dto.response.ResCreateUserDTO;
import com.example.jobhunter.dto.response.ResUserDTO;
import com.example.jobhunter.dto.response.ResultPaginationDTO;
import com.example.jobhunter.repository.CompanyRepository;
import com.example.jobhunter.repository.UserRepository;
import com.example.jobhunter.util.error.IdInvalidException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder; // <<< NEW IMPORT
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final CompanyRepository companyRepository;
  private final PasswordEncoder passwordEncoder; // <<< NEW FIELD

    // <<< MODIFIED CONSTRUCTOR >>>
    public UserService(
        UserRepository userRepository,
        CompanyRepository companyRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder; // Set the new field
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

  // <<< NEW METHOD ADDED >>>
  /**
   * Handles changing a user's password after verifying the old one.
   *
   * @param email The email of the logged-in user.
   * @param oldPassword The user's current password.
   * @param newPassword The new password to set.
   * @throws IdInvalidException if the user is not found or the old password is incorrect.
   */

    public void handleChangePassword(Long userId, String oldPassword, String newPassword) throws IdInvalidException {
        User currentUser = this.userRepository.findById(userId)
            .orElseThrow(() -> new IdInvalidException("Không tìm thấy người dùng với ID: " + userId));
        
        // Kiểm tra mật khẩu cũ có khớp không
        if (currentUser.getPassWord() == null || 
            !passwordEncoder.matches(oldPassword, currentUser.getPassWord())) {
            throw new IdInvalidException("Mật khẩu cũ không chính xác.");
        }

        // Cập nhật mật khẩu mới đã được mã hóa
        currentUser.setPassWord(this.passwordEncoder.encode(newPassword));
        this.userRepository.save(currentUser);
    }
}
