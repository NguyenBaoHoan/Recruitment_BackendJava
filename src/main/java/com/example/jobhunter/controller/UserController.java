package com.example.jobhunter.controller;

import java.util.List;
import java.util.Optional;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import org.hibernate.query.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jobhunter.domain.User;
import com.example.jobhunter.dto.response.*;
import com.example.jobhunter.dto.request.*;
import com.example.jobhunter.service.UserService;
import com.example.jobhunter.util.anotation.ApiMessage;
import com.example.jobhunter.util.error.IdInvalidException;
import com.example.jobhunter.dto.request.ReqChangePasswordDTO;
import com.example.jobhunter.util.error.SecurityUtil;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // create one user
    @PostMapping("/create")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@RequestBody User postManUser) throws IdInvalidException {
        boolean existUser = this.userService.isEmailExist(postManUser.getEmail());
        if (existUser) {
            throw new IdInvalidException(
                "Email" + postManUser.getEmail() + "Email này đã được sử dụng!!!");
        }
        String hashPassword = this.passwordEncoder.encode(postManUser.getPassWord());
        postManUser.setPassWord((hashPassword));
        User newUser = userService.handleSaveUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(newUser));
    }

    // delete by id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> DeleteUser(@PathVariable("id") long id) throws IdInvalidException {
        if (id > 1500) {
            throw new IdInvalidException("ID khong lon hon 1501");
        }

        userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResUserDTO> fetchUserById(@PathVariable("id") long id) {
        User newUser = userService.fetchOneUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(userService.convertToResUserDTO(newUser));
    }

    @GetMapping
    public ResponseEntity<ResultPaginationDTO> fetchAllUser(
            @Filter Specification<User> spec,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.fetchAllUser(spec, pageable));
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUserById(@RequestBody User user) {
        User ericUser = userService.handleUpdateUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(ericUser);
    }

    @PostMapping("/change-password")
    @ApiMessage("Đổi mật khẩu thành công")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ReqChangePasswordDTO req) throws IdInvalidException {
        // Lấy email của người dùng đang đăng nhập từ token bảo mật
        String email = SecurityUtil.getCurrentUserLogin()
            .orElseThrow(() -> new IdInvalidException("Token không hợp lệ, không tìm thấy email người dùng."));
        
        // Gọi service để xử lý logic đổi mật khẩu
        this.userService.handleChangePassword(email, req.getOldPassword(), req.getNewPassword());
        
        return ResponseEntity.ok("Đổi mật khẩu thành công!");
    }
}