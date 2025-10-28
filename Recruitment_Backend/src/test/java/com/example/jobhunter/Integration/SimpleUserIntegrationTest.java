package com.example.jobhunter.Integration;

import com.example.jobhunter.domain.User;
import com.example.jobhunter.repository.UserRepository;
import com.example.jobhunter.util.constant.GenderEnum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Simple User Integration Tests")
class SimpleUserIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Test 1: Tạo và lưu user vào database")
    void testCreateUser() {
        // Given
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassWord("password123");
        user.setAge(25);
        user.setGender(GenderEnum.MALE);

        // When
        User savedUser = userRepository.save(user);

        // Then
        assertNotNull(savedUser.getId());
        assertEquals("Test User", savedUser.getName());
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals(25, savedUser.getAge());
    }

    @Test
    @DisplayName("Test 2: Tìm user theo email")
    void testFindUserByEmail() {
        // Given
        User user = new User();
        user.setName("Email Test");
        user.setEmail("email@test.com");
        user.setPassWord("password");
        user.setAge(30);
        userRepository.save(user);

        // When
        User foundUser = userRepository.findByEmail("email@test.com");

        // Then
        assertNotNull(foundUser);
        assertEquals("Email Test", foundUser.getName());
    }

    @Test
    @DisplayName("Test 3: Kiểm tra email tồn tại")
    void testEmailExists() {
        // Given
        User user = new User();
        user.setName("Exists Test");
        user.setEmail("exists@test.com");
        user.setPassWord("password");
        user.setAge(28);
        userRepository.save(user);

        // When & Then
        assertTrue(userRepository.existsByEmail("exists@test.com"));
        assertFalse(userRepository.existsByEmail("notexists@test.com"));
    }

    @Test
    @DisplayName("Test 4: Update user")
    void testUpdateUser() {
        // Given
        User user = new User();
        user.setName("Original User");
        user.setEmail("original@test.com");
        user.setPassWord("password");
        user.setAge(25);
        User savedUser = userRepository.save(user);

        // When
        savedUser.setName("Updated User");
        savedUser.setAge(30);
        User updatedUser = userRepository.save(savedUser);

        // Then
        assertEquals("Updated User", updatedUser.getName());
        assertEquals(30, updatedUser.getAge());
    }

    @Test
    @DisplayName("Test 5: Delete user")
    void testDeleteUser() {
        // Given
        User user = new User();
        user.setName("Delete Test");
        user.setEmail("delete@test.com");
        user.setPassWord("password");
        user.setAge(25);
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getId();

        // When
        userRepository.deleteById(userId);

        // Then
        assertFalse(userRepository.findById(userId).isPresent());
    }
}