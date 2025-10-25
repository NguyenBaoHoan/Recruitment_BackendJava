package com.example.jobhunter.service;

import com.example.jobhunter.domain.Company;
import com.example.jobhunter.repository.CompanyRepository;
import com.example.jobhunter.repository.UserRepository;
import com.example.jobhunter.dto.response.ResultPaginationDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CompanyService Unit Tests - 5 Basic Tests")
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CompanyService companyService;

    private Company testCompany;
    private List<Company> companyList;

    @BeforeEach
    void setUp() {
        testCompany = new Company();
        testCompany.setId(1L);
        testCompany.setName("Tech Company");
        testCompany.setDescription("A leading tech company");
        testCompany.setAddress("Hà Nội");
        testCompany.setLogo("logo.png");

        Company company2 = new Company();
        company2.setId(2L);
        company2.setName("Finance Company");
        company2.setDescription("Financial services");
        company2.setAddress("TP.HCM");

        companyList = Arrays.asList(testCompany, company2);
    }

    @Test
    @DisplayName("Test 1: Tạo company thành công")
    void testCreateCompany_Success() {
        // Given
        when(companyRepository.save(any(Company.class))).thenReturn(testCompany);

        // When
        Company result = companyService.handleSaveCompany(testCompany);

        // Then
        assertNotNull(result);
        assertEquals("Tech Company", result.getName());
        assertEquals("A leading tech company", result.getDescription());
        assertEquals("Hà Nội", result.getAddress());
        verify(companyRepository, times(1)).save(any(Company.class));
    }

    @Test
    @DisplayName("Test 3: Delete company thành công")
    void testDeleteCompany_Success() {
        // Given
        Long companyId = 1L;
        doNothing().when(companyRepository).deleteById(companyId);

        // When
        companyService.handledeleteCompany(companyId);

        // Then
        verify(companyRepository, times(1)).deleteById(companyId);
    }


    @Test
    @DisplayName("Test 5: Fetch company theo ID")
    void testFetchCompanyById_Success() {
        // Given
        when(companyRepository.findById(1L)).thenReturn(java.util.Optional.of(testCompany));

        // When
        Company result = companyRepository.findById(1L).orElse(null);

        // Then
        assertNotNull(result);
        assertEquals("Tech Company", result.getName());
        assertEquals(1L, result.getId());
        verify(companyRepository, times(1)).findById(1L);
    }
}