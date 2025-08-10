package com.example.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jobhunter.domain.Job;
import com.example.jobhunter.domain.Skills;
import com.example.jobhunter.domain.SkillRepository; // Sửa import này
import com.example.jobhunter.dto.request.ReqCreateJobDTO;
import com.example.jobhunter.dto.response.ResultPaginationDTO;
import com.example.jobhunter.dto.response.job.ResCreateJobDTO;
import com.example.jobhunter.dto.response.job.ResUpdateJobDTO;
import com.example.jobhunter.repository.JobRepository;
import com.example.jobhunter.util.error.IdInvalidException;

@Service
@Transactional
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private SkillRepository skillRepository;

    /**
     * Đăng ký job mới với thông tin chi tiết
     */
    public ResCreateJobDTO registerNewJob(ReqCreateJobDTO request) {
        // Validate input
        validateJobRequest(request);

        // Tạo job entity từ request
        Job job = new Job();
        job.setName(request.getName());
        job.setSalary(request.getSalary());
        job.setLocation(request.getLocation());
        job.setJobType(request.getJobType());
        job.setEducationLevel(request.getEducationLevel());
        job.setDescription(request.getDescription());
        job.setRequirements(request.getRequirements());

        // Convert benefits list thành string
        if (request.getBenefits() != null && !request.getBenefits().isEmpty()) {
            String benefitsString = String.join("\n", request.getBenefits());
            job.setBenefits(benefitsString);
        }

        job.setWorkAddress(request.getWorkAddress());
        job.setStartDate(request.getStartDate());
        job.setEndDate(request.getEndDate());
        job.setActive(true);
        job.setJobStatus(request.getJobStatus());

        // Xử lý skills
        if (request.getSkillIds() != null && !request.getSkillIds().isEmpty()) {
            List<Skills> skills = request.getSkillIds().stream()
                    .map(skillId -> skillRepository.findById(skillId)
                            .orElseThrow(() -> new IdInvalidException("Skill không tồn tại với ID: " + skillId)))
                    .collect(Collectors.toList());
            job.setSkills(skills);
        }

        // Lưu job
        Job savedJob = jobRepository.save(job);

        // Convert thành response DTO
        return convertToResCreateJobDTO(savedJob);
    }

    /**
     * Validate request data
     */
    private void validateJobRequest(ReqCreateJobDTO request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IdInvalidException("Tên công việc không được để trống");
        }

    }

    // Các method hiện tại
    public ResultPaginationDTO fetchAllJob(Specification<Job> spec, Pageable pageable) {
        Page<Job> jobPage = jobRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(jobPage.getNumber() + 1);
        mt.setPageSize(jobPage.getSize());
        mt.setPages(jobPage.getTotalPages());
        mt.setTotal(jobPage.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(jobPage.getContent());
        return rs;
    }

    public ResCreateJobDTO handleSaveJob(Job job) {
        // check job is exist
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Skills> dbskills = skillRepository.findAllById(reqSkills);
            job.setSkills(dbskills);
        }
        Job curJob = jobRepository.save(job);
        return convertToResCreateJobDTO(curJob);
    }

    public ResUpdateJobDTO handleUpdateJob(Long id, Job job) {
        Optional<Job> curJobOpt = fetchOneJob(id);
        if (!curJobOpt.isPresent()) {
            return null;
        }

        Job curJob = curJobOpt.get();
        curJob.setName(job.getName());
        curJob.setLocation(job.getLocation());
        curJob.setSalary(job.getSalary());
        curJob.setEducationLevel(job.getEducationLevel());
        curJob.setDescription(job.getDescription());
        curJob.setRequirements(job.getRequirements());
        curJob.setBenefits(job.getBenefits());
        curJob.setWorkAddress(job.getWorkAddress());
        curJob.setStartDate(job.getStartDate());
        curJob.setEndDate(job.getEndDate());
        curJob.setActive(job.isActive());

        // Handle skills similar to create
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Skills> dbskills = skillRepository.findAllById(reqSkills);
            curJob.setSkills(dbskills);
        }

        Job updatedJob = jobRepository.save(curJob);
        return convertToResUpdateJobDTO(updatedJob);
    }

    public Optional<Job> fetchOneJob(Long id) {
        return jobRepository.findById(id);
    }

    public void handleDeleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    private ResCreateJobDTO convertToResCreateJobDTO(Job job) {
        ResCreateJobDTO rs = new ResCreateJobDTO();
        rs.setId(job.getId());
        rs.setName(job.getName());
        rs.setLocation(job.getLocation());
        rs.setSalary(job.getSalary());
        rs.setEducationLevel(job.getEducationLevel());
        rs.setJobType(job.getJobType());
        rs.setDescription(job.getDescription());
        rs.setRequirements(job.getRequirements());
        rs.setBenefits(job.getBenefits());
        rs.setWorkAddress(job.getWorkAddress());
        rs.setStartDate(job.getStartDate());
        rs.setEndDate(job.getEndDate());
        rs.setActive(job.isActive());
        rs.setCreatedAt(job.getCreatedAt());
        rs.setCreatedBy(job.getCreatedBy());

        if (job.getSkills() != null) {
            List<String> skillNames = job.getSkills()
                    .stream().map(x -> x.getName())
                    .collect(Collectors.toList());
            rs.setSkills(skillNames);
        }
        return rs;
    }

    private ResUpdateJobDTO convertToResUpdateJobDTO(Job job) {
        ResUpdateJobDTO rs = new ResUpdateJobDTO();
        rs.setId(job.getId());
        rs.setName(job.getName());
        rs.setLocation(job.getLocation());
        rs.setSalary(job.getSalary());
        rs.setEducationLevel(job.getEducationLevel());
        rs.setJobType(job.getJobType());
        rs.setDescription(job.getDescription());
        rs.setStartDate(new java.sql.Date(job.getStartDate().getTime()));
        rs.setEndDate(new java.sql.Date(job.getEndDate().getTime()));
        rs.setActive(job.isActive());
        rs.setCreatedAt(job.getCreatedAt());
        rs.setCreatedBy(job.getCreatedBy());

        if (job.getSkills() != null) {
            List<String> skillNames = job.getSkills()
                    .stream().map(x -> x.getName())
                    .collect(Collectors.toList());
            rs.setSkills(skillNames);
        }
        return rs;
    }
}
