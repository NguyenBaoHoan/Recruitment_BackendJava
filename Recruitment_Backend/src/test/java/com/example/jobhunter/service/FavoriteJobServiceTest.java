package com.example.jobhunter.service;

import com.example.jobhunter.domain.FavoriteJob;
import com.example.jobhunter.domain.Job;
import com.example.jobhunter.domain.User;
import com.example.jobhunter.dto.response.ResFavoriteJobDTO;
import com.example.jobhunter.repository.FavoriteJobRepository;
import com.example.jobhunter.repository.JobRepository;
import com.example.jobhunter.repository.UserRepository;
import com.example.jobhunter.util.constant.StatusEnum;
import com.example.jobhunter.util.error.IdInvalidException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FavoriteJobService Unit Tests - 5 Basic Tests")
class FavoriteJobServiceTest {

    @Mock
    private FavoriteJobRepository favoriteJobRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private FavoriteJobService favoriteJobService;

    private User testUser;
    private Job testJob;
    private FavoriteJob testFavoriteJob;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John Doe");
        testUser.setEmail("john@example.com");

        testJob = new Job();
        testJob.setId(1L);
        testJob.setName("Java Developer");
        testJob.setSalary("1500.0");
        testJob.setLocation("Hà Nội");
        testJob.setStatus(StatusEnum.ACTIVE);

        testFavoriteJob = new FavoriteJob();
        testFavoriteJob.setId(1L);
        testFavoriteJob.setUser(testUser);
        testFavoriteJob.setJob(testJob);
    }

    @Test
    @DisplayName("Test 1: Thêm job vào danh sách yêu thích thành công")
    void testAddToFavorites_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(jobRepository.findById(1L)).thenReturn(Optional.of(testJob));
        when(favoriteJobRepository.findByUserIdAndJobId(1L, 1L)).thenReturn(Arrays.asList()); // Empty list
        when(favoriteJobRepository.save(any(FavoriteJob.class))).thenReturn(testFavoriteJob);

        // When & Then
        assertDoesNotThrow(() -> favoriteJobService.addToFavorites(1L, 1L));
        verify(favoriteJobRepository, times(1)).save(any(FavoriteJob.class));
    }

    @Test
    @DisplayName("Test 2: Thêm job đã yêu thích - ném exception")
    void testAddToFavorites_AlreadyExists_ThrowsException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(jobRepository.findById(1L)).thenReturn(Optional.of(testJob));
        when(favoriteJobRepository.findByUserIdAndJobId(1L, 1L)).thenReturn(Arrays.asList(testFavoriteJob)); // Non-empty list

        // When & Then
        Exception exception = assertThrows(
            IdInvalidException.class,
            () -> favoriteJobService.addToFavorites(1L, 1L)
        );

        assertEquals("Job đã được yêu thích trước đó", exception.getMessage());
        verify(favoriteJobRepository, never()).save(any(FavoriteJob.class));
    }
@Test
@DisplayName("Test 3: Xóa job khỏi danh sách yêu thích thành công")
void testRemoveFromFavorites_Success() {
    // Given
    User testUser = new User();
    testUser.setId(2L);
    testUser.setName("Test User");

    Job testJob = new Job();
    testJob.setId(3L);
    testJob.setName("Test Job");

    when(userRepository.findById(2L)).thenReturn(Optional.of(testUser));
    when(jobRepository.findById(3L)).thenReturn(Optional.of(testJob));
    when(favoriteJobRepository.findByUserIdAndJobId(2L, 3L))
            .thenReturn(Arrays.asList(testFavoriteJob));
    doNothing().when(favoriteJobRepository).deleteByUserIdAndJobId(2L, 3L);

    // When & Then
    assertDoesNotThrow(() -> favoriteJobService.removeFromFavorites(2L, 3L));
    verify(favoriteJobRepository, times(1)).deleteByUserIdAndJobId(2L, 3L);
}

    @Test
    @DisplayName("Test 4: Kiểm tra job có được yêu thích không - True")
    void testIsJobFavorited_True() {
        // Given
        when(favoriteJobRepository.findByUserIdAndJobId(1L, 1L)).thenReturn(Arrays.asList(testFavoriteJob));

        // When
        boolean result = favoriteJobService.isJobFavorited(1L, 1L);

        // Then
        assertTrue(result);
        verify(favoriteJobRepository, times(1)).findByUserIdAndJobId(1L, 1L);
    }

}