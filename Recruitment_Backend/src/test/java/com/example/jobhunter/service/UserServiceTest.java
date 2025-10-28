package com.example.jobhunter.service;

import com.example.jobhunter.domain.User;
import com.example.jobhunter.domain.Company;
import com.example.jobhunter.repository.UserRepository;
import com.example.jobhunter.repository.CompanyRepository;
import com.example.jobhunter.util.constant.GenderEnum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests - 5 Basic Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private Company testCompany;

    @BeforeEach
    void setUp() {
        testCompany = new Company();
        testCompany.setId(1L);
        testCompany.setName("Test Company");

        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John Doe");
        testUser.setEmail("john@example.com");
        testUser.setPassWord("password123");
        testUser.setAge(25);
        testUser.setGender(GenderEnum.MALE);
        testUser.setAddress("Hà Nội");
        testUser.setCompany(testCompany);
    }

    @Test
    @DisplayName("Test 1: Tạo user thành công với thông tin hợp lệ")
    void testCreateUser_Success() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.handleSaveUser(testUser);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Test 2: Kiểm tra email đã tồn tại")
    void testCheckEmailExists_True() {
        // Given
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        // When
        boolean result = userService.isEmailExist("john@example.com");

        // Then
        assertTrue(result);
        verify(userRepository, times(1)).existsByEmail("john@example.com");
    }

    @Test
    @DisplayName("Test 3: Fetch user theo ID thành công")
    void testFetchUserById_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        User result = userService.fetchOneUser(1L);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test 4: Update user thành công")
    void testUpdateUser_Success() {
        // Given
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("John Updated");
        updatedUser.setEmail("john@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When
        User result = userService.handleUpdateUser(updatedUser);

        // Then
        assertNotNull(result);
        assertEquals("John Updated", result.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Test 5: Delete user thành công")
    void testDeleteUser_Success() {
        // Given
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);

        // When
        userService.handleDeleteUser(userId);

        // Then
        verify(userRepository, times(1)).deleteById(userId);
    }
}